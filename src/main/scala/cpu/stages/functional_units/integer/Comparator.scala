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

    val o = out UInt(3 bits)
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
      b := a_32b.asUInt.resize(64).asSInt
    }.otherwise{
      a := a_32b.resize(64)
      b := a_32b.resize(64)
    }
  }.otherwise{
    a := io.a.asSInt
    b := io.b.asSInt
  }
  when(io.logical){
    when(a.asUInt < b.asUInt){
      io.o := 4
    }.elsewhen(a.asUInt > b.asUInt){
      io.o := 2
    }.otherwise{
      io.o := 1
    }
  }.otherwise{
    when(a < b){
      io.o := 4
    }.elsewhen(a > b){
      io.o := 2
    }.otherwise{
      io.o := 1
    }
  }

}