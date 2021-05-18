package util
import chisel3._

object SignExtend {

  def apply(s: SInt, width: Int) : SInt = {
    val sWidth = s.getWidth
    val tmp = Wire(SInt(width.W))
    val ret = Wire(SInt(width.W))
    val shiftamt :Int = width - sWidth
    tmp := s << shiftamt.U
    ret := tmp >> shiftamt.U
    ret
  }
}

object SignExtendU {

  def apply(s: SInt, width: Int) : UInt = {
    SignExtend(s, width).asUInt()
  }

}
