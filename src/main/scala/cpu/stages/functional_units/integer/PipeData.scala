package cpu.stages.functional_units.integer
import spinal.core._
import cpu.interfaces.{FunctionalUnit}

class ExecuteData extends FunctionalUnit {
  val additionalData = Bits(128 bits)
}

class AdderPipeData extends Bundle {
  val upperA = UInt(32 bits)
  val upperB = UInt(32 bits)
  val carry_32 = Bool
  val overflow_32 = Bool
  val invert_a = Bool

}
