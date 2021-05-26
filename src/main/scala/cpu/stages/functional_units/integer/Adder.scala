package cpu.stages.functional_units.integer

import cpu.interfaces.{DecoderData, ReadInterface}
import isa.MnemonicEnums

import spinal.core._
import spinal.lib._

// contains implementation of POWER adder class which is derived from a Chisel module

// adder class should also contain ChiselEnum objects that help interprest
// args1,2, and 3 that it recieves.

class Adder(val wid: Int) extends Component {
  val io = new Bundle {
    val a = in UInt(wid bits)
    val b = in UInt(wid bits)
    val carry_in = in Bool()

    val invert_a = in Bool()
    val o = out UInt(wid bits)
    val carry_out = out Bool()
    val carry_out_32 = out Bool
    val overflow_out = out Bool
  }
  val adder_a = UInt((wid+1) bits)
  val adder_b = UInt((wid+1) bits)
  val adder_o = UInt((wid+2) bits)

  when(io.invert_a) {
    adder_a := Cat(~io.a, io.carry_in).asUInt
  }.otherwise {
    adder_a := Cat(io.a, io.carry_in).asUInt
  }
  adder_b := Cat(io.b, io.carry_in).asUInt

  // Add, and widen for carry bit
  adder_o := adder_a +^ adder_b

  io.o := adder_o(wid downto 1)
  io.carry_out := adder_o(wid+1)
  io.carry_out_32 := (io.a(32) ^ io.b(32)) =/= io.o(32)
  // sign change
  io.overflow_out := (io.a(wid-1) === io.b(wid-1)) && (io.o(wid-1) =/= io.a(wid-1))
}
