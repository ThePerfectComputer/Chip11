package cpu.stages.functional_units.integer

import cpu.interfaces.{DecoderData, ReadInterface}
import util.{PipeStage, Delay}
import isa.MnemonicEnums

import spinal.core._
import spinal.lib._

// contains implementation of POWER shifter class which is derived from a Chisel module

class Shifter(val wid: Int) extends Component {
  val io = new Bundle {
    val rs = in UInt (wid bits)
    val rb = in UInt (wid bits)
    val ra = in UInt (wid bits)
    val me = in UInt (6 bits)
    val mb = in UInt (6 bits)

    val keep_source = in Bool ()
    val word_op = in Bool ()
    val left = in Bool ()
    val is_arithmetic = in Bool ()
    val is_shift = in Bool ()
    val byte_op = in Bool ()

    val o = out UInt (wid bits)
    val carry_out = out Bool ()
    val cr0_out = out UInt (3 bits)
  }
  val shifter_source = UInt((wid) bits)
  val shifter_amount = UInt(6 bits)

  val word_source = UInt((wid / 2) bits)
  val word_rotated = UInt((wid / 2) bits)
  val word_rotmask = UInt((wid / 2) bits)
  val rotmask = UInt(wid bits)
  val shifter_rotated = UInt(wid bits) //mod by wid
  val shifter_o = UInt((wid) bits)

  val shifter_backing = UInt(wid bits)
  val shifter_mask = UInt(wid bits)
  val shifter_mb = UInt(6 bits)
  val shifter_me = UInt(6 bits)

  word_rotmask := 0
  word_source := 0
  word_rotated := 0
  rotmask := 0

  when(io.keep_source) {
    shifter_backing := io.ra
  }.elsewhen(io.is_arithmetic) {
    when(io.byte_op) {
      shifter_backing.setAllTo(io.rs(7))
    }
      .elsewhen(io.word_op) {
        shifter_backing.setAllTo(io.rs(31))
      }
      .otherwise {
        shifter_backing.setAllTo(io.rs(63))
      }
  }.otherwise {
    shifter_backing := 0
  }

  when(io.word_op) {
    shifter_amount := io.rb(4 downto 0).resized
  }.otherwise {
    shifter_amount := io.rb(5 downto 0).resized
  }
  //mb left, me right

  //if shift and left, me = 63-shift
  //if shift and right, mb = shift
  shifter_me := io.me
  shifter_mb := io.mb
  when(io.is_shift) {
    when(io.left) {
      shifter_me := 63 - shifter_amount
      when(io.word_op) {
        shifter_mb := io.mb + 32
      }
    }.elsewhen(io.word_op) {
      shifter_mb := shifter_amount + 32
      shifter_me := io.me + 32
    }.elsewhen(io.byte_op) {
      shifter_me := 63
      shifter_mb := 56
    }.otherwise {
      shifter_mb := shifter_amount
    }
  }.otherwise {
    when(io.word_op) {
      shifter_me := io.me + 32
      shifter_mb := io.mb + 32
    }
  }
  //if rotate...

  shifter_source := io.rs

  //A rotshift left B = ((A<<B) & (0xffff << B)) | ((A >> 64-B) & (0xffff >> 64-B)

  when(io.rb === 0) {
    shifter_rotated := shifter_source
    when(io.word_op) {
      shifter_rotated := Cat(
        shifter_source(31 downto 0),
        shifter_source(31 downto 0)
      ).asUInt
    }
  }
    .elsewhen(io.word_op) {
      word_source := shifter_source(31 downto 0)
      when(io.left) {
        word_rotmask := (~(U(0, 32 bits)) |<< shifter_amount)
        word_rotated := ((word_source |<< shifter_amount) & word_rotmask) | ((word_source |>> (32 - shifter_amount)) & ~word_rotmask)
        //word_rotated := Cat(shifter_source(63, 32+(shifter_amount%32)), shifter_source(31+(shifter_amount%32), 32))
      }.otherwise {
        word_rotmask := ~(U(0, 32 bits)) |>> shifter_amount
        word_rotated := ((word_source |>> shifter_amount) & word_rotmask) | ((word_source |<< (32 - shifter_amount)) & ~word_rotmask)
        //word_rotated := Cat(shifter_source(63, 64-(shifter_amount%32)), shifter_source(63-(shifter_amount%32), 32))
      }
      shifter_rotated := Cat(word_rotated, word_rotated).asUInt
    }
    .otherwise {
      when(io.left) {
        rotmask := ~(U(0, 64 bits)) |<< shifter_amount
        shifter_rotated := ((shifter_source |<< shifter_amount) & rotmask) | ((shifter_source |>> (64 - shifter_amount)) & ~rotmask)
        //shifter_rotated := Cat(shifter_source(63, shifter_amount), shifter_source(shifter_amount-1, 0))
      }.otherwise {
        rotmask := ~(U(0, 64 bits)) |>> shifter_amount
        shifter_rotated := ((shifter_source |>> shifter_amount) & rotmask) | ((shifter_source |<< (64 - shifter_amount)) & ~rotmask)
        //shifter_rotated := Cat(shifter_source(63, 64-shifter_amount), shifter_source(63-shifter_amount, 0))
      }
    }

  val allBitsSet = UInt(64 bits)
  allBitsSet.setAllTo(True)
  io.carry_out := False
  when(io.is_shift & io.is_arithmetic) {
    when(io.rb(6 downto 0) =/= 0) {
      when(io.word_op & io.rs(31)) {
        when(((allBitsSet >> (shifter_amount + 32)) & shifter_source).orR) {
          io.carry_out := True
        }
      }.elsewhen(~io.word_op & io.rs(63)) {
        when(((allBitsSet >> shifter_amount) & shifter_source).orR) {
          io.carry_out := True
        }
      }
    }
  }
  val shifter_mask_amt = UInt(6 bits)
  shifter_mask_amt := (63 - shifter_me)

  val mr = (allBitsSet |>> shifter_mb)
  val ml = (allBitsSet |<< shifter_mask_amt)
  when(shifter_mb > shifter_me) {
    shifter_mask := mr | ml
  }.otherwise {
    shifter_mask := mr & ml
  }
  when(io.is_shift) {
    when(io.word_op) {
      when(io.rb(5)) {
        shifter_mask := 0
      }
    }.otherwise {
      when(io.rb(6)) {
        shifter_mask := 0
      }
    }
  }
  shifter_o := (shifter_rotated & shifter_mask) | (shifter_backing & ~shifter_mask)
  io.o := shifter_o

  when(shifter_o.asSInt > 0) {
    io.cr0_out := 2
  }.elsewhen(shifter_o.asSInt < 0) {
    io.cr0_out := 4
  }.otherwise {
    io.cr0_out := 1
  }

}
