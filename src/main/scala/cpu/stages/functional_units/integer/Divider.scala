package cpu.stages.functional_units.integer

import isa.MnemonicEnums

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

class Divider(wid: Int) extends Component {
  val io = new Bundle {
    val a = in UInt(wid bits)
    val b = in UInt(wid bits)

    val is_word = in Bool
    val is_unsigned = in Bool
    val shift_a = in Bool
    val input_valid = in Bool
    val input_ready = out Bool

    val o = out UInt(wid bits)
    val output_valid = out Bool
  }

  val output = Reg(UInt(wid bits))
  val output_valid = RegInit(False)

  io.o := output
  io.output_valid := output_valid


  val dividend = Reg(SInt(wid*2+1 bits))
  val divisor = Reg(SInt(wid+1 bits))
  val quotient = Reg(SInt(wid bits))
  val temp = Reg(SInt(wid*2+1 bits))
  io.input_ready := True

  val a = SInt((wid+1) bits)
  val b = SInt((wid+1) bits)
  when(io.is_word){
    when(io.is_unsigned){
      a := io.a(wid/2-1 downto 0).intoSInt.resized
      b := io.b(wid/2-1 downto 0).intoSInt.resized
    }.otherwise {
      a := io.a(wid/2-1 downto 0).asSInt.resized
      b := io.b(wid/2-1 downto 0).asSInt.resized
    }
  }.otherwise {
    when(io.is_unsigned){
      a := io.a.intoSInt
      b := io.b.intoSInt
    }.otherwise {
      a := io.a.asSInt.resized
      b := io.b.asSInt.resized
    }
  }

  val resultNegative = RegInit(False)

  val fsm = new StateMachine {

    val bitCounter = Reg(UInt(log2Up(dividend.getWidth+1) bits)) init(dividend.getWidth)

    val idle : State = new State with EntryPoint{
      whenIsActive {
        io.input_ready := True
        when(io.input_valid){
          output_valid := False
          divisor := b
          quotient := 0
          temp := 0
          when(io.shift_a){
            dividend := a << 64
          }.otherwise {
            dividend := a.resized
          }
          goto(sign_adjust)
        }
      }
    }
    val sign_adjust : State = new State{
      whenIsActive {
        resultNegative := divisor.msb ^ dividend.msb
        when(divisor.msb){
          divisor := -divisor
        }
        when(dividend.msb){
          dividend := -dividend
        }
        goto(work)
      }
    }

    val work : State = new State {
      val a = SInt(wid*2+1 bits)
      a := Mux(dividend.msb, (temp |<< 1) | 1, (temp |<< 1))
      onEntry(bitCounter := dividend.getWidth)
      whenIsActive {
        bitCounter := bitCounter - 1
        dividend := dividend |<< 1
        temp := Mux(a >= divisor, a-divisor, a)
        quotient := Mux(a >= divisor, (quotient |<< 1) | 1 , quotient |<< 1)


        when(bitCounter === 0){
          goto(idle)
          output := Mux(resultNegative, -quotient, quotient).asUInt
          output_valid := True

        }
      }
    }
  }
}
