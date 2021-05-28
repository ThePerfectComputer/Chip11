package cpu.stages.functional_units.integer

import cpu.interfaces.{DecoderData, ReadInterface}
import isa.MnemonicEnums

import spinal.core._
import spinal.lib._

// contains implementation of POWER LogicUnit class which is derived from a Chisel module

class Comparator(val wid: Int) extends Component {

  val io = new Bundle {
    val a = in UInt(wid bits)
    val b = in UInt(wid bits)

    val is_64b = in Bool()
    val logical = in Bool()
    val so_bit = in Bool

    val pipedata = out(new ComparatorPipeData)
  }

  val a = SInt(wid bits)
  val b = SInt((wid) bits)
  val l = Bool()

  val a_32b = SInt(32 bits)
  val b_32b = SInt(32 bits)
  a_32b := io.a(31 downto 0).asSInt
  b_32b := io.b(31 downto 0).asSInt



  when(!io.is_64b){
    when(io.logical){
      a := a_32b.asUInt.resize(64).asSInt
      b := b_32b.asUInt.resize(64).asSInt
    }.otherwise{
      a := a_32b.resize(64)
      b := b_32b.resize(64)
    }
  }.otherwise{
    a := io.a.asSInt
    b := io.b.asSInt
  }

  val adder_a = UInt(33 bits)
  val adder_b = UInt(33 bits)
  adder_a := Cat(a(31 downto 0), U(1)).asUInt
  adder_b := Cat(~b(31 downto 0), U(1)).asUInt

  val result = UInt(34 bits)
  result := adder_a +^ adder_b
  val not_eq = result(32 downto 1).orR

  io.pipedata.a := a(63 downto 32).asUInt
  io.pipedata.b := (~b(63 downto 32)).asUInt
  io.pipedata.carry := result(33)
  io.pipedata.logical := io.logical
  io.pipedata.lower_is_not_equal := not_eq
  io.pipedata.so_bit := io.so_bit

}

class ComparatorStage2 extends Component {
  val io = new Bundle {
    val pipedata = in(new ComparatorPipeData)

    val o = out UInt(4 bits)
  }

  val result = UInt(34 bits)
  val a = UInt(33 bits)
  val b = UInt(33 bits)
  a := Cat(io.pipedata.a, io.pipedata.carry).asUInt
  b := Cat(io.pipedata.b, io.pipedata.carry).asUInt
  result := a +^ b
  val not_eq = Bool
  not_eq := result(32 downto 1).orR | io.pipedata.lower_is_not_equal

  val cmp = UInt(3 bits)
  io.o := Cat(cmp, io.pipedata.so_bit).asUInt
  when(io.pipedata.logical){
    when(!result(33)){
      cmp := 4
    }.elsewhen(not_eq){
      cmp := 2
    }.otherwise{
      cmp := 1
    }
  }.otherwise{
    when(result(32)){
      cmp := 4
    }.elsewhen(not_eq){
      cmp := 2
    }.otherwise{
      cmp := 1
    }
  }
}
