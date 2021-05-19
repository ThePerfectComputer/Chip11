package cpu.uOps

import spinal.core._
import spinal.lib._
import scala.collection._

class uOps extends Bundle {
  val functional_unit = FunctionalUnit()
  val sub_function = Bits(5 bits)
  val args = Bits(15 bits)
}

object FunctionalUnit extends SpinalEnum {
  val NONE    = newElement()
  val INTEGER = newElement()
}

object uOps{
  def apply(functional_unit : FunctionalUnit.E, 
    sub_function : Data,
    args : Data) = {
    val u = new uOps
    assert(u.sub_function.getBitsWidth >= sub_function.getBitsWidth)
    assert(u.args.getBitsWidth >= args.getBitsWidth)
    val data = new uOps
    data.functional_unit := functional_unit
    data.sub_function := sub_function.asBits
    data.args := args.asBits
    data
  }
}
