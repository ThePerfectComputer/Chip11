package cpu.stages.functional_units.integer

import cpu.interfaces.{FunctionalUnit, ReadInterface}
import isa.{
  FormEnums,
  MnemonicEnums,
  ISAPairings,
  ReadSlotPacking,
  WriteSlotPacking
}
import cpu.uOps.functional_units.Integer.{
  IntegerFUSub,
  AdderSelectB,
  AdderCarryIn,
  AdderArgs,
  LogicSelectB,
  LogicArgs,
  MultiplierSelectB,
  MultiplierArgs,
  BranchArgs,
  ShifterSelectB,
  ShifterME,
  ShifterMB,
  ShifterArgs,
  ComparatorArgs,
  ComparatorSelectB
}
import cpu.uOps.{FunctionalUnit, UOpsMapping}
import util.{PipeStage, PipeData}

import spinal.core._
import spinal.core.Formal._
//import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._
import scala.util.Random

class BranchProofDUT extends Component {
  val io = new Bundle {
    val ctr_in = in UInt (64 bits)
  }
  val ri = new ReadInterface
  ri := ri.getZero
  ri.allowOverride

  val branchArgs = new BranchArgs
  ri.dec_data.uOps.args(1 downto 0).assignFromBits(branchArgs.asBits)
  val branchMod = new Branch
  val branchS2 = new BranchStage2
  branchS2.io.pipedata := branchMod.io.pipedata

  branchMod.io.ri := ri
  branchS2.io.dec_data := ri.dec_data

  branchArgs.conditional := True
  branchArgs.immediate_address := True

  val ctr = io.ctr_in

  ri.dec_data.insn := 0x4200fffc
  ri.slots(ReadSlotPacking.SPRPort1).data := io.ctr_in

  val ctr_o = branchS2.io.ctr_w.payload
  assert(branchS2.io.ctr_w.valid)
  assert(ctr_o === ((ctr - 1) & ~U(0, 64 bits)))

  val ctr_is_zero = (ctr - 1) === 0
  assert(branchS2.io.bc.branch_taken === !ctr_is_zero)
  cover(ctr_is_zero === True)

}

class BranchProofVerilogFormal extends AnyFlatSpec with should.Matchers {
  behavior of "BranchProofDUT"

  it should "create verilog" in {
    val config = SpinalConfig(defaultConfigForClockDomains=ClockDomainConfig(resetKind = SYNC, resetActiveLevel = HIGH),
      targetDirectory="formal"
    ).withoutEnumString()
    config.includeFormal.generateSystemVerilog(new BranchProofDUT)
  }
}
