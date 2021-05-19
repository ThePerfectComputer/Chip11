package cpu.uOps.functional_units.Integer

import spinal.core._
import spinal.lib._
import scala.collection._

object IntegerFUSub extends SpinalEnum {
  val None = newElement()
  val Adder = newElement()
  val LogicUnit = newElement()
  val Multiplier = newElement()
  val Shifter = newElement()
  val Branch = newElement()
  val Comparator = newElement()
  val Move = newElement()
}

object AdderSelectB extends SpinalEnum {
  val Slot1 = newElement()
  val Slot2 = newElement()
  val Slot3 = newElement()
  val Imm = newElement()
  val ImmShift = newElement()
  val ImmShift2 = newElement()
  val ZERO = newElement()
}

object AdderCarryIn extends SpinalEnum {
  val ZERO = newElement()
  val ONE = newElement()
  val CA = newElement()
}

class AdderArgs extends Bundle {
  val slotB = AdderSelectB()
  val cIn = AdderCarryIn()
  val invertA = Bool()
}

object AdderArgs {
  def apply(slotB: AdderSelectB.E, cIn: AdderCarryIn.E, invertA: Boolean) = {
    val data = new AdderArgs
    data.slotB := slotB
    data.cIn := cIn
    data.invertA := Bool(invertA)
    data
  }
}

object LogicSelectB extends SpinalEnum {
  val Slot1 = newElement()
  val Slot2 = newElement()
  val Slot3 = newElement()
  val Imm = newElement()
  val ImmShift = newElement()
}

class LogicArgs extends Bundle {
  val slotB = LogicSelectB()
  val invertA = Bool()
  val invertB = Bool()
  val invertO = Bool()
  val xor = Bool()
}

object LogicArgs {
  def apply(slotB: LogicSelectB.E, invertA: Boolean, invertB: Boolean, invertO: Boolean, xor: Boolean) = {
    val data = new LogicArgs
    data.slotB := slotB
    data.invertA := Bool(invertA)
    data.invertB := Bool(invertB)
    data.invertO := Bool(invertO)
    data.xor := Bool(xor)
    data
  }
}

object MultiplierSelectB extends SpinalEnum {
  val Slot1 = newElement()
  val Slot2 = newElement()
  val Slot3 = newElement()
  val Imm = newElement()
}

class MultiplierArgs extends Bundle {
  val slotB = MultiplierSelectB()
  val is_div = Bool()
  val word_operands = Bool()
  val is_unsigned = Bool()
  val output_high = Bool()
  val output_word = Bool()
  val shift_a = Bool()
}

object MultiplierArgs {
  def apply(slotB: MultiplierSelectB.E, is_div: Boolean, word_operands: Boolean, is_unsigned: Boolean, output_high: Boolean, output_word: Boolean, shift_a: Boolean) = {
    val data = new MultiplierArgs
    data.slotB := slotB
    data.is_div := Bool(is_div)
    data.word_operands := Bool(word_operands)
    data.is_unsigned := Bool(is_unsigned)
    data.output_high := Bool(output_high)
    data.output_word := Bool(output_word)
    data.shift_a := Bool(shift_a)
    data
  }
}

object ShifterSelectB extends SpinalEnum {
  val Slot1 = newElement()
  val Slot2 = newElement()
  val Slot3 = newElement()
  val Imm = newElement()
  val ZERO = newElement()
}

object ShifterME extends SpinalEnum {
  val LSB = newElement()                                                                    
  val ME = newElement()
  val ME_32 = newElement()
  val IMM_REV = newElement()
  val WORD = newElement()
}

object ShifterMB extends SpinalEnum {
  val MSB = newElement()                                                                    
  val MB = newElement()
  val MB_32 = newElement()
}

class ShifterArgs extends Bundle {
  val slotB = ShifterSelectB()
  val me = ShifterME()
  val mb = ShifterMB()

  val keep_source = Bool()
  val word_op = Bool()
  val left = Bool()
  val is_arithmetic = Bool()
  val is_shift = Bool()

  val byte_op = Bool()

}

object ShifterArgs {
  def apply(slotB: ShifterSelectB.E, me: ShifterME.E, mb: ShifterMB.E, keep_source: Boolean, word_op: Boolean, left: Boolean, is_arithmetic: Boolean, is_shift: Boolean, byte_op: Boolean) = {
    val data = new ShifterArgs
    data.slotB := slotB
    data.me := me
    data.mb := mb
    data.keep_source := Bool(keep_source)
    data.word_op := Bool(word_op)
    data.left := Bool(left)
    data.is_arithmetic := Bool(is_arithmetic)
    data.is_shift := Bool(is_shift)
    data.byte_op := Bool(byte_op)
    data
  }


}
class BranchArgs extends Bundle {
  val conditional = Bool()
  val immediate_address = Bool()
}

object BranchArgs {
  def apply(conditional: Boolean, immediate_address: Boolean) = {
    val data = new BranchArgs
    data.conditional := Bool(conditional)
    data.immediate_address := Bool(immediate_address)
    data
  }
}

object ComparatorSelectB extends SpinalEnum {
  val Slot1 = newElement()
  val Slot2 = newElement()
  val Slot3 = newElement()
  val Imm = newElement()
}

class ComparatorArgs extends Bundle {
  val slotB = ComparatorSelectB()
  val logical = Bool()
}

object ComparatorArgs {
  def apply(slotB: ComparatorSelectB.E, logical: Boolean) = {
    val data = new ComparatorArgs
    data.slotB := slotB
    data.logical := Bool(logical)
    data
  }
}
