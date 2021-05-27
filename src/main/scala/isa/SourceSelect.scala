package isa
import spinal.core._
// Register file selector for source 1-2
object SourceSelect extends SpinalEnum {
  val NONE     = newElement()
  val GPR      = newElement()
  val FPR      = newElement()

  val VR       = newElement()
  val VSR      = newElement()

  val COMBINED = newElement()

  val SPR      = newElement()

  val FPSCR    = newElement()
  val CRA      = newElement()
  val CRB      = newElement()

  val BHRB     = newElement()
  val XER      = newElement()
}
