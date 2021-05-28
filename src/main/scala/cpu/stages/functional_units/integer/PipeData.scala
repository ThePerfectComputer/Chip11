package cpu.stages.functional_units.integer
import spinal.core._
import cpu.interfaces.{FunctionalUnit}

class ExecuteData extends FunctionalUnit {
  val additionalData = Bits(203 bits)
}

class AdderPipeData extends Bundle {
  val upperA = UInt(32 bits)
  val upperB = UInt(32 bits)
  val carry_32 = Bool
  val overflow_32 = Bool
  val invert_a = Bool
}

class ShifterPipeData extends Bundle {
  val shifter_mask = UInt(64 bits)
  val shifter_backing = UInt(64 bits)
  val shifter_amount = UInt(7 bits)
  val shifter_source = UInt(64 bits)
  val left = Bool
  val word_op = Bool
  val is_shift = Bool
  val is_arithmetic = Bool

}


class ComparatorPipeData extends Bundle {
  val a = UInt(32 bits)
  val b = UInt(32 bits)
  val carry = Bool
  val lower_is_not_equal = Bool
  val logical = Bool
  val so_bit = Bool
}
