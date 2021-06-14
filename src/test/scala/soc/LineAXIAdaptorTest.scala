package soc.devices.memory_adaptor
import bus.BusTimerShared

import Console.{BLUE, GREEN, CYAN_B, RED, RESET, YELLOW}
import cpu.interfaces.{LineRequest, LineResponse}

import cpu.shared.memory_state.{
  TransactionStatus,
  TransactionType,
  TransactionSize
}
import spinal.lib.bus.amba4.axi.{
  Axi4Shared,
  Axi4SlaveFactory,
  Axi4Config,
  Axi4CrossbarFactory
}
import util._

import spinal.core._
import spinal.lib._
import spinal.sim._
import spinal.core.sim._
import spinal.lib.bus.amba4.axi._

import org.scalatest._
import flatspec._
import matchers._
import scala.collection.mutable.ListBuffer

class LineAXIDUT(implicit config: Axi4Config) extends Component {
  val io = new Bundle {
    val request  = slave(new LineRequest)
    val response = master(new LineResponse)
  }
  val adaptor = new LineAXIAdaptor(2)
  val ramSize = 16384
  val ram = Axi4SharedOnChipRam (
    dataWidth = 128,
    byteCount = ramSize,
    idWidth = 5
  )

  var initialData = new ListBuffer[Bits]
  val item = BigInt("00000003000000020000000100000000", 16)
  val increment = BigInt("00000004000000040000000400000004", 16)
  for(i <- 0 until (ramSize/16)){
    initialData += B(item + increment*i, 128 bits)
  }
  ram.ram.init(initialData)

  adaptor.io.request <> io.request
  adaptor.io.response <> io.response


  ram.io.axi << adaptor.io.axi
}

class LineAXIAdaptorTest extends AnyFlatSpec with should.Matchers {
  implicit val axiConfig = Axi4Config(addressWidth=64, dataWidth=128, idWidth=4, useId=true, useRegion=false, useBurst=true, useLock=false, useCache=false, useSize=true, useQos=false, useLen=true, useLast=true, useResp=true, useProt=false, useStrb=true)
  behavior of "LineAXIAdaptor"

  it should "create verilog" in {
    SpinalVerilog(new LineAXIAdaptor(id=2))
  }
  it should "drive an axi bus" in {
    SimConfig.withWave.doSim(new LineAXIDUT()) { dut =>
      dut.clockDomain.forkStimulus(period = 10)
      sleep(50)

      // Send a read request
      dut.io.request.ldst_req #= TransactionType.LOAD
      dut.io.request.size #= TransactionSize.WORD
      dut.io.request.data #= 0
      dut.io.request.byte_address #= 0x100
      while (!dut.io.request.ack.toBoolean) {
        dut.clockDomain.waitSampling(1)
      }
      dut.clockDomain.waitSampling(5)
      // dut.io.request.byte_address #= 0x101
      // while (!dut.io.request.ack.toBoolean) {
      //   dut.clockDomain.waitSampling(1)
      // }
      // dut.clockDomain.waitSampling(5)
      // dut.io.request.byte_address #= 0x10c
      // while (!dut.io.request.ack.toBoolean) {
      //   dut.clockDomain.waitSampling(1)
      // }
      // dut.clockDomain.waitSampling(5)
      // dut.io.request.byte_address #= 0x10d
      // while (!dut.io.request.ack.toBoolean) {
      //   dut.clockDomain.waitSampling(1)
      // }
      // dut.clockDomain.waitSampling(5)
    }
  }
}
