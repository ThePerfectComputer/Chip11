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
import spinal.lib.bus.amba4.axi._

import org.scalatest._
import flatspec._
import matchers._

class MemBusAXIDut(implicit config: Axi4Config) extends Component {
  val io = new Bundle {
    val membus = slave(new MemBus128)
  }
  val mbToAxi = new MemBusToAXIShared(2)
  val busTimer = new BusTimerShared(16)
  val ramSize = 16384
  val ram = Axi4SharedOnChipRam (
    dataWidth = 128,
    byteCount = ramSize,
    idWidth = 5
  )

  mbToAxi.io.membus <> io.membus

  ram.io.axi << mbToAxi.io.axi

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
      def get_done() = {
        while (dut.io.membus.status.toEnum != TransactionStatus.DONE) {
          dut.clockDomain.waitSampling(1)
        }
        dut.io.membus.ldst_req #= TransactionType.NONE
      }
      def write_data(data: BigInt, mask: Int){
        for(i <- 0 until 16){
          val byte = (data >> i*8) & 0xff
          dut.io.membus.write_data(i) #= byte
          val maskBit = (mask >> i) & 1
          dut.io.membus.write_mask(i) #= (maskBit != 0)
        }
      }
      def read_data() = {
        var bigInt = BigInt(0)
        for(i <- 0 until 16){
          val byte = dut.io.membus.read_data(i).toBigInt
          bigInt |= (byte << i*8)
        }
        bigInt
      }

      def doRead(address: BigInt, size: TransactionSize.E=TransactionSize.QUADWORD) : BigInt ={
        dut.io.membus.ldst_req #= TransactionType.LOAD
        dut.io.membus.byte_address #= address
        dut.io.membus.access_size #= size
        dut.clockDomain.waitSampling(1)
        get_ack()
        dut.io.membus.ldst_req #= TransactionType.NONE
        dut.clockDomain.waitSampling(1)
        get_done()
        read_data()
      }
      def doWrite(address: BigInt, data: BigInt){
        dut.io.membus.ldst_req #= TransactionType.STORE
        dut.io.membus.byte_address #= address
        write_data(data, 0xffff)
        dut.io.membus.access_size #= TransactionSize.QUADWORD
        dut.clockDomain.waitSampling(1)
        get_ack()
        dut.io.membus.ldst_req #= TransactionType.NONE
      }

      dut.io.membus.ldst_req #= TransactionType.NONE
      dut.clockDomain.waitSampling(1)
      doWrite(0x10, BigInt("deadbeef", 16))
      doWrite(0x20, BigInt("12345678", 16))

      //dut.clockDomain.waitSampling(10)
      var res = doRead(0x10)
      println(f"Read $res%x")
      res = doRead(0x20)
      println(f"Read $res%x")
      dut.clockDomain.waitSampling(100)
    }
  }
}
