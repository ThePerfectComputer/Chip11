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
      if (i < 3) {
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

class ReadPipeDUT extends Component {
  val io = new Bundle {
    val pi = slave(Flushable(new ReadInterface))
    val po = master(Flushable(new ReadInterface))

    val in1 = in(new ReadInterface)
    val in2 = in(new ReadInterface)
  }

  val d1 = new Delay(new ReadInterface)
  val read = new ReadStage

  read << io.pi
  read >-> io.po
  // d1 << io.pi
  // d1 >-> io.po


  // Formal Stuff
  val sent1 = RegInit(False)
  val sent2 = RegInit(False)

  val received1 = RegInit(False)
  val received2 = RegInit(False)

  val init = initstate()
  when(init){
    assume(sent1 === False)
    assume(sent2 === False)
    assume(received1 === False)
    assume(received2 === False)
    assume(clockDomain.isResetActive)
  }.otherwise{
    assume(!clockDomain.isResetActive)
  }

  when(!init){
    when(!sent1 && io.pi.valid && io.pi.ready){
      assume(io.pi.payload === io.in1)
      sent1 := True
    }

    when(sent1 && !sent2 && io.pi.valid && io.pi.ready){
      assume(io.pi.payload === io.in2)
      sent2 := True
    }
  }
  assume(stable(io.in1.asBits))
  assume(stable(io.in2.asBits))
  //assume(io.in1 =/= io.in2)
  assume(io.in1.dec_data.cia < 256)
  assume(io.in2.dec_data.cia < 256)
  assume(io.po.flush === False)

  when(!init){
    when(sent1 && !received1){
      when(io.po.valid && io.po.ready){
        assert(io.po.payload.dec_data.cia === io.in1.dec_data.cia)
        for(i <- 0 until 4){
          assert(io.po.payload.slots(i).idx === io.in1.slots(i).idx)
          assert(io.po.payload.slots(i).sel === io.in1.slots(i).sel)
          if(i < 4){
            when(io.po.payload.slots(i).sel === SourceSelect.GPR){
              assert(io.po.payload.slots(i).data(4 downto 0) === io.in1.slots(i).idx(4 downto 0))
            }
          }
        }
        received1 := True
      }
    }
    when(sent2 && received1 && !received2){
      when(io.po.valid && io.po.ready){
        assert(io.po.payload.dec_data.cia === io.in2.dec_data.cia)
        received2 := True
      }
    }
    cover(received2)
  }
  cover(io.po.ready === False)
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
    config.includeFormal.generateSystemVerilog(new ReadPipeDUT)
  }
}
