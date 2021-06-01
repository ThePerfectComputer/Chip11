package soc.devices.memory_adaptor

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
import util._

import spinal.core._
import spinal.lib._
import spinal.sim._
import spinal.core.sim._

import org.scalatest._
import flatspec._
import matchers._

class MemBusToAXITest extends AnyFlatSpec with should.Matchers {
  behavior of "MemBusToAXI"

  it should "create verilog" in {
    implicit val axiConfig = Axi4Config(addressWidth=64, dataWidth=128, idWidth=4, useId=true, useRegion=false, useBurst=true, useLock=false, useCache=false, useSize=true, useQos=false, useLen=true, useLast=false, useResp=true, useProt=false, useStrb=true)
    SpinalVerilog(new MemBusToAXIShared(id=2))
  }
  it should "drive an axi bus" in {

  }
}
