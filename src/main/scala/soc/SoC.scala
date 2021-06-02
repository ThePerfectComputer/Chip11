package soc

import cpu.{CPU, CPUConfig}
import soc.devices.memory_adaptor.{MemoryAdaptor, MemBusToAXIShared}
import spinal.lib.bus.amba4.axi.{Axi4Config, Axi4CrossbarFactory, Axi4SharedOnChipRam}
//import soc.devices.{DualPortSram128, UART, Arbiter, AdaptorPeripheral}
import spinal.core.sim._
import soc.devices.{DualPortSram128}

import spinal.core._
import spinal.lib._
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

class SoC(val mem_file: String = null) extends Component {
  implicit val axiConfig = Axi4Config(
    addressWidth = 64,
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
  val fetch_adaptor = new MemoryAdaptor
  cpu.io.fetch_request <> fetch_adaptor.io.request
  cpu.io.fetch_response <> fetch_adaptor.io.response

  // use another adaptor to connect the CPU's loadstore unit to the RAM's read/write port
  val ldst_adaptor = new MemoryAdaptor
  cpu.io.ldst_request <> ldst_adaptor.io.request
  cpu.io.ldst_response <> ldst_adaptor.io.response

  val mbToAxi1 = new MemBusToAXIShared(0)
  val mbToAxi2 = new MemBusToAXIShared(1)
  mbToAxi1.io.membus <> fetch_adaptor.io.membus
  mbToAxi2.io.membus <> ldst_adaptor.io.membus

  val ramSize = 16384
  val ramDepth = ramSize/axiConfig.dataWidth
  val ram = Axi4SharedOnChipRam (
    dataWidth = axiConfig.dataWidth,
    byteCount = ramSize,
    idWidth = 5
  )
  ram.ram.simPublic()

  val initialData = InitData.getDataFromFile(ramDepth, axiConfig.dataWidth, mem_file)
  print(initialData)

  val axiCrossbar = Axi4CrossbarFactory()

  axiCrossbar.addSlaves(
    ram.io.axi -> (0x0, ramSize)
  )

  // I think this is defining what masters can access which slaves
  axiCrossbar.addConnections(
    mbToAxi1.io.axi -> List(ram.io.axi),
    mbToAxi2.io.axi -> List(ram.io.axi)
  )

  axiCrossbar.build()


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
