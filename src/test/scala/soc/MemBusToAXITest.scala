package soc.devices.memory_adaptor
import bus.BusTimerShared

import Console.{BLUE, GREEN, CYAN_B, RED, RESET, YELLOW}

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
import soc.interfaces.MemBus128
import util._

import spinal.core._
import spinal.lib._
import spinal.sim._
import spinal.core.sim._

import org.scalatest._
import flatspec._
import matchers._

class MemBusAXIDut(implicit config: Axi4Config) extends Component {
  val io = new Bundle {
    val membus = slave(new MemBus128)
    val timerAMatch = out Bool
    val timerBMatch = out Bool
  }
  val mbToAxi = new MemBusToAXIShared(2)
  val busTimer = new BusTimerShared(16)

  mbToAxi.io.membus <> io.membus

  busTimer.io.bus << mbToAxi.io.axi
  io.timerAMatch := busTimer.io.timerAMatch
  io.timerBMatch := busTimer.io.timerBMatch

}

class MemBusToAXITest extends AnyFlatSpec with should.Matchers {
  implicit val axiConfig = Axi4Config(addressWidth=64, dataWidth=128, idWidth=4, useId=true, useRegion=false, useBurst=true, useLock=false, useCache=false, useSize=true, useQos=false, useLen=true, useLast=true, useResp=true, useProt=false, useStrb=true)
  behavior of "MemBusToAXI"

  it should "create verilog" in {
    SpinalVerilog(new MemBusToAXIShared(id=2))
  }
  it should "drive an axi bus" in {
    SimConfig.withWave.doSim(new MemBusAXIDut()) { dut =>
      dut.clockDomain.forkStimulus(period = 10)
      sleep(50)
      def get_ack() = {
        while (!dut.io.membus.req_ack.toBoolean) {
          dut.clockDomain.waitSampling(1)
        }
        dut.io.membus.ldst_req #= TransactionType.NONE
      }

      dut.io.membus.ldst_req #= TransactionType.NONE
      dut.clockDomain.waitSampling(1)
      dut.io.membus.ldst_req #= TransactionType.LOAD
      dut.io.membus.byte_address #= 0x40
      dut.io.membus.access_size #= TransactionSize.WORD
      dut.clockDomain.waitSampling(1)
      get_ack()
      dut.clockDomain.waitSampling(10)
    }
  }
}
