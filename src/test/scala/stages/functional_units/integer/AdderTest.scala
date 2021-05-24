package cpu.stages.functional_units.integer

import cpu.interfaces.regfile.{SourceSelect}
import cpu.interfaces.{ReadInterface, FunctionalUnit}
import isa.{FormEnums, MnemonicEnums, ISAPairings}
import cpu.uOps.functional_units.Integer.{IntegerFUSub, AdderSelectB, AdderCarryIn, AdderArgs, LogicSelectB, LogicArgs, MultiplierSelectB, MultiplierArgs, BranchArgs, ShifterSelectB, ShifterME, ShifterMB, ShifterArgs, ComparatorArgs, ComparatorSelectB}
import cpu.uOps.{FunctionalUnit, UOpsMapping}
import util.{PipeStage, PipeData}

import spinal.core._
//import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._
import scala.util.Random

class Stage1AdderTest extends AnyFlatSpec with should.Matchers {
  behavior of "Stage1"

  it should "add shit" in {
    SimConfig.withWave.doSim(new Stage1) { dut =>

      dut.pipeOutput.ready #= true
      dut.pipeOutput.flush #= false

      val ri = dut.pipeInput.payload
      val wi = dut.pipeOutput.payload.write_interface
      val uOpsMap = new UOpsMapping

      val op = uOpsMap.lookup(MnemonicEnums.add_o__dot_)
      ri.dec_data.uOps.functional_unit #= FunctionalUnit.INTEGER
      ri.dec_data.uOps.sub_function #= 1 // Adder
      ri.dec_data.uOps.args #= 1 // slot 2, no carry, no invert

      val r = new Random
      for(i <- 0 until 16){
        val mask = BigInt("ffffffffffffffff", 16)
        val a = BigInt(64, r)
        val b = BigInt(64, r)
        val o = (a+b) & mask

        ri.slots(0).data #= a
        ri.slots(1).data #= b

        sleep(10)
        assert(wi.slots(0).data.toBigInt == o)
      }

    }
  }
}
