package util
import spinal.core._

object SignExtend {

  def apply(s: SInt, width: Int) : SInt = {
    val sWidth = s.getWidth
    val tmp = SInt(width bits)
    val ret = SInt(width bits)
    val shiftamt :Int = width - sWidth
    tmp := s << shiftamt
    ret := tmp >> shiftamt
    ret
  }
}

object SignExtendU {

  def apply(s: SInt, width: Int) : UInt = {
    SignExtend(s, width).asUInt
  }

}
