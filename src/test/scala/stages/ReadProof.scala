package cpu.stages.functional_units.integer
import cpu.interfaces.{ReadInterface}
import util._
import isa.{SourceSelect}
import cpu.stages.ReadStage

import spinal.core._
import spinal.core.Formal._
import spinal.lib.{master, slave}
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._
import scala.util.Random

class DelayProofDUT extends Component {
  val io = new Bundle {
    val ri = slave(Flushable(new ReadInterface))
    val ro = master(Flushable(new ReadInterface))
  }

  val delay = new Delay(new ReadInterface)
  delay << io.ri
  delay >-> io.ro
  for ((slot, i) <- io.ri.slots.zipWithIndex) {
    assume(slot.data === slot.idx)
    if (i < 4) {
      assume(slot.sel === SourceSelect.GPR || slot.sel === SourceSelect.NONE)
    } else {
      assume(slot.sel === SourceSelect.NONE)
    }
  }
  val init = initstate()
  when(init){
    assume(clockDomain.isResetActive)
  }.otherwise{
    assume(!clockDomain.isResetActive)
  }
  for ((slot, i) <- io.ro.slots.zipWithIndex) {
    when(!init) {
      when(io.ro.valid) {
        assert(slot.data === slot.idx)
      }
    }
  }
}

class DelayProofVerilogFormal extends AnyFlatSpec with should.Matchers {
  behavior of "DelayProofDUT"

  it should "create verilog" in {
    val config = SpinalConfig(
      defaultConfigForClockDomains =
        ClockDomainConfig(resetKind = SYNC, resetActiveLevel = HIGH),
      targetDirectory = "formal"
    ).withoutEnumString()
    config.includeFormal.generateSystemVerilog(new DelayProofDUT)
  }
}

class ReadProofDUT extends Component {
  val io = new Bundle {
    val ri = slave(Flushable(new ReadInterface))
    val ro = master(Flushable(new ReadInterface))
  }

  val read = new ReadStage
  read.io.bhrb_rp(0).data := 0
  read.io.xer_rp(0).data := 0
  for (idx <- 0 until 2) {
    read.io.comb_rp(idx).data := 0
    read.io.spr_rp(idx).data := 0
    read.io.cr_rp(idx).data := 0
    read.io.fpscr_rp(idx).data := 0
    read.io.vr_rp(idx).data := 0
    read.io.vsr_rp(idx).data := 0
    read.io.fpr_rp(idx).data := 0

    val delayed = RegInit(U(0, 5 bits))
    delayed := read.io.gpr_rp(idx).idx
    read.io.gpr_rp(idx).data := delayed.resized
  }

  read <-< io.ri
  read >-> io.ro
  val init = initstate()
  for ((slot, i) <- io.ri.slots.zipWithIndex) {
    assume(slot.data === 0)
    when(init) {
      assume(slot.sel === SourceSelect.NONE)
      assume(slot.idx === 0)
    }.otherwise {
      if (i < 4) {
        assume(slot.sel === SourceSelect.GPR || slot.sel === SourceSelect.NONE)
        assume((slot.idx & ~U(31, slot.idx.getWidth bits)) === 0)
      } else {
        assume(slot.sel === SourceSelect.NONE)
        assume(slot.idx === 0)
      }
    }

    when(io.ri.valid && !io.ri.ready){
      assume(stable(slot.sel))
      assume(stable(slot.idx))
      assume(stable(slot.data))
    }
  }
  when(io.ri.valid && !io.ri.ready){
    assume(stable(io.ri.valid))
  }

  //assume(io.ro.ready)

  when(init) {
    assume(clockDomain.isResetActive)
  }.otherwise {
    assume(!clockDomain.isResetActive)
  }
  for ((slot, i) <- io.ro.slots.zipWithIndex) {
    when(!init) {
      when(io.ro.valid && io.ro.ready) {
        when(slot.sel === SourceSelect.GPR) {
          assert(slot.data(4 downto 0) === slot.idx(4 downto 0))
        }
      }
    }
  }
}

class ReadProofVerilogFormal extends AnyFlatSpec with should.Matchers {
  behavior of "ReadProofDUT"

  it should "create verilog" in {
    val config = SpinalConfig(
      defaultConfigForClockDomains =
        ClockDomainConfig(resetKind = SYNC, resetActiveLevel = HIGH),
      targetDirectory = "formal"
    ).withoutEnumString()
    config.includeFormal.generateSystemVerilog(new ReadProofDUT)
  }
}
