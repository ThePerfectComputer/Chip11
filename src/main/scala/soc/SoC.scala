package soc

import cpu.{CPU, CPUConfig}
import soc.devices.memory_adaptor.{MemoryAdaptor, MemBusToAXIShared, LineAXIAdaptor}
import spinal.lib.bus.amba4.axi.{Axi4Config, Axi4CrossbarFactory, Axi4SharedOnChipRam}
//import soc.devices.{DualPortSram128, UART, Arbiter, AdaptorPeripheral}
import spinal.core.sim._
import soc.devices.{DualPortSram128}
import bus.BusUART

import spinal.core._
import spinal.lib._
import spinal.lib.com.uart._
import scala.collection.mutable.ListBuffer

import java.nio.file.{Files, Paths}

object debug {
  val debug = false
  val uart = debug
  val arbiter = debug
}

object InitData {
  def getDataFromFile(depth: Int, width: Int, mem_file: String) {
    var initialData = new ListBuffer[BigInt]
    val bytesPerLine = width/8
    for (i <- 0 until depth)
      initialData += 0
    if (mem_file != null) {
      val filePath = Paths.get(mem_file)
      val arr = Files.readAllBytes(filePath)
      for ((byte, idx) <- arr.zipWithIndex) {
        val line_address = idx / bytesPerLine
        val byte_address = idx % bytesPerLine
        val data = initialData(line_address)
        val newData = BigInt(byte & 0xff) << (byte_address * 8)
        initialData(line_address) = data | newData
      }
    }

    initialData
  }
}

class SoCGen(val mem_file: String = null, memorySize:Int=16384) extends Component {
  implicit val axiConfig = Axi4Config(
    addressWidth = 32,
    dataWidth = 128,
    idWidth = 4,
    useId = true,
    useRegion = false,
    useBurst = true,
    useLock = false,
    useCache = false,
    useSize = true,
    useQos = false,
    useLen = true,
    useLast = true,
    useResp = true,
    useProt = false,
    useStrb = true
  )
  implicit val config = new CPUConfig()

  // instantiate the CPU and memory
  val cpu = new CPU

  // use an adaptor to connect the CPU's fetch unit to the RAM's read-only port
  //val fetch_adaptor = new MemoryAdaptor
  val fetch_adaptor = new LineAXIAdaptor(0)
  cpu.io.fetch_request <> fetch_adaptor.io.request
  cpu.io.fetch_response <> fetch_adaptor.io.response



  // use another adaptor to connect the CPU's loadstore unit to the RAM's read/write port
  // val ldst_adaptor = new MemoryAdaptor
  val ldst_adaptor = new LineAXIAdaptor(1)
  cpu.io.ldst_request <> ldst_adaptor.io.request
  cpu.io.ldst_response <> ldst_adaptor.io.response

  // val fetchMbToAxi = new MemBusToAXIShared(0)
  val ldstMbToAxi = new MemBusToAXIShared(1)
  // fetchMbToAxi.io.membus <> fetch_adaptor.io.membus
  // ldstMbToAxi.io.membus <> ldst_adaptor.io.membus

  val fetch_axi = fetch_adaptor.io.axi
  val ldst_axi = ldst_adaptor.io.axi

  val ramDepth = memorySize/axiConfig.dataWidth
  val ram = Axi4SharedOnChipRam (
    dataWidth = axiConfig.dataWidth,
    byteCount = memorySize,
    idWidth = 5
  )
  ram.ram.simPublic()

  val initialData = InitData.getDataFromFile(ramDepth, axiConfig.dataWidth, mem_file)
  print(initialData)



  def loadFromFile(fileName:String){
    val bytesPerLine = axiConfig.dataWidth/8
    val filePath = Paths.get(fileName)
    val arr = Files.readAllBytes(filePath)
    var data = BigInt(0)
    for((byte, idx) <- arr.zipWithIndex){
      val line_address = idx / bytesPerLine
      val byte_address = idx % bytesPerLine
      if(byte_address == 0) data = BigInt(0)
      val newData = BigInt(byte & 0xff) << (byte_address * 8)
      data = data | newData
      ram.ram.setBigInt(line_address, data)
    }
  }
}

class SoC(mem_file: String = null, memorySize:Int=16384) extends SoCGen(mem_file, memorySize) {

  val axiCrossbar = Axi4CrossbarFactory()

  axiCrossbar.addSlaves(
    ram.io.axi -> (0x0, memorySize)
  )

  // I think this is defining what masters can access which slaves
  axiCrossbar.addConnections(
    fetch_axi -> List(ram.io.axi),
    ldst_axi -> List(ram.io.axi)
  )

  axiCrossbar.build()

}

class SoCWithUART(mem_file: String = null, memorySize:Int=16384) extends SoCGen(mem_file, memorySize) {
  val io = new Bundle{
    val rx = in Bool
    val tx = out Bool
    // val clk = in Bool
    // val rst = in Bool
  }
  io.rx.setName("ftdi_txd")
  io.tx.setName("ftdi_rxd")
  noIoPrefix()


  val uartGenerics = UartCtrlGenerics(dataWidthMax=8)
  val uartBase = 0x20000000
  val uart = new BusUART(uartGenerics, rxFifoDepth=64, baseAddress=uartBase)
  io.tx := uart.io.uart.txd
  uart.io.uart.rxd := io.rx


  val axiCrossbar = Axi4CrossbarFactory()

  axiCrossbar.addSlaves(
    ram.io.axi -> (0x0, uartBase),
    uart.io.bus -> (uartBase, uartBase)
  )

  // I think this is defining what masters can access which slaves
  axiCrossbar.addConnections(
    fetch_axi -> List(ram.io.axi),
    ldst_axi -> List(ram.io.axi, uart.io.bus)
  )

  axiCrossbar.addPipelining(uart.io.bus)((crossbar, u) => {
    crossbar.readCmd >/> u.readCmd
    crossbar.readRsp <-/< u.readRsp
  })((crossbar, u) => {
    crossbar.writeCmd >/> u.writeCmd
    crossbar.writeData >/> u.writeData
    crossbar.writeRsp <-/< u.writeRsp
  })
  axiCrossbar.addPipelining(ram.io.axi)((crossbar, u) => {
    crossbar.sharedCmd >/> u.sharedCmd
    crossbar.readRsp <-/< u.readRsp
    crossbar.writeData >/> u.writeData
    crossbar.writeRsp <-/< u.writeRsp
  })

  axiCrossbar.build()

}
