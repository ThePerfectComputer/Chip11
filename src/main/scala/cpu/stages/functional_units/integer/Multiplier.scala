package cpu.stages.functional_units.integer

import cpu.interfaces.{DecoderData, ReadInterface}
import util.{PipeStage}
import isa.MnemonicEnums

import spinal.core._
import spinal.lib._

// contains implementation of POWER multiplier/divider class which is derived from a Chisel module

// M/D class should also contain ChiselEnum objects that help interprest
// args1,2, and 3 that it recieves.

class Multiplier(val wid: Int) extends Component {
  val io = new Bundle {
    val a = in UInt(wid bits)
    val b = in UInt(wid bits)

    val is_div = in Bool
    val word_operands = in Bool
    val is_unsigned = in Bool
    val output_high = in Bool
    val output_word = in Bool
    val shift_a = in Bool


    val o = out UInt(wid bits)
    val cr0_out = out UInt(3 bits)
    val overflow = out Bool
  }
  val mul_a = SInt((wid+1) bits)
  val mul_b = SInt((wid+1) bits)
  val mul_o = UInt((2*wid) bits)
  val mul_result = SInt((2*wid) bits)

  when (io.word_operands){
    when(io.is_unsigned){
      mul_a := Cat(U(0, 1 bits), io.a(wid-1 downto wid/2)).asSInt
      mul_b := Cat(U(0, 1 bits), io.b(wid-1 downto wid/2)).asSInt
    }.otherwise{
      mul_a := io.a(wid-1 downto wid/2).asSInt
      mul_b := io.b(wid-1 downto wid/2).asSInt
    }
  }.otherwise{
    mul_a := io.a.asSInt
    mul_b := io.b.asSInt
  }
  io.overflow := False
  //cast data out to UInt
  when (io.is_div){
    when (mul_b === 0){
      mul_result := 0
      io.overflow := True
    }.otherwise{
      when (io.shift_a){
        mul_result := ((mul_a << 64) / mul_b)
      }.otherwise{
        mul_result := (mul_a / mul_b)
      }
    }
    //check overflow
  }.otherwise{
    mul_result := (mul_a * mul_b)
    when(mul_result > (2^32 -1)){
      io.overflow := True
    }.otherwise{
      io.overflow := False
    }
  }

  mul_o := mul_result.asUInt
  when(io.output_word){
    io.o := Cat(U(0, 32 bits), mul_o(wid-1 downto wid/2)).asUInt
  }.otherwise{
    when(io.output_high){
      io.o := mul_o(wid-1 downto 0)
    }.otherwise{
      io.o := mul_o(2*wid-1 downto wid)
    }
  }
  
  when (mul_result > 0){
    io.cr0_out := 2
  }.elsewhen( mul_result < 0){
    io.cr0_out := 4
  }.otherwise{
    io.cr0_out := 1
  }
  
}
