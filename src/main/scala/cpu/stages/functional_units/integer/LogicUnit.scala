package cpu.stages.functional_units.integer

import cpu.interfaces.{DecoderData, ReadInterface}
import isa.MnemonicEnums

import spinal.core._
import spinal.lib._

// contains implementation of POWER LogicUnit class which is derived from a Chisel module

// LU class should also contain ChiselEnum objects that help interprest
// args1,2, and 3 that it recieves.

class LogicUnit(val wid: Int) extends Component {
  //and, andc, andi, nor, or, orc, ori, oris, xor, xori, xoris
  //nor = !a & !b
  //or = !(!a & !b)
  //andc = a & !b
  //orc = a | !b
  val io = new Bundle {
    val a = in UInt(wid bits)
    val b = in UInt(wid bits)

    val invert_a = in Bool()
    val invert_b = in Bool()
    val invert_o = in Bool()
    val xor  = in Bool()
    val o = out UInt(wid bits)
    val carry_out = out Bool()
  }

  val a = UInt((wid) bits)
  val b = UInt((wid) bits)
  val o = UInt((wid) bits)

  when(io.invert_a) {
    a := ~io.a
  }.otherwise {
    a := io.a
  }
  when(io.invert_b) {
    b := ~io.b
  }.otherwise {
    b := io.b
  }
  when (io.xor) {
    o := a ^ b
  }.otherwise {
    o := a & b
  }

  when (io.invert_o) {
    io.o := ~o
  }.otherwise {
    io.o := o
  }
  io.carry_out := False

}
