package cpu.shared
import spinal.core._

object XERMask {
  val _width = 6
  val SO = 1
  val OV = 2
  val CA = 4
  val OV32 = 8
  val CA32 = 16
  val REST = 32
  val ALL = 63
}
object XERBits {
  val SO = 31
  val OV = 30
  val CA = 29
  val OV32 = 19
  val CA32 = 18
}

class XERMaskMapping extends MaskMapping(6, 64) {

  io.mask_out.clearAll()
  when((io.mask_in & XERMask.SO) =/= 0) {
    io.mask_out(XERBits.SO) := True
  }
  when((io.mask_in & XERMask.OV) =/= 0) {
    io.mask_out(XERBits.OV) := True
  }
  when((io.mask_in & XERMask.CA) =/= 0) {
    io.mask_out(XERBits.CA) := True
  }
  when((io.mask_in & XERMask.OV32) =/= 0) {
    io.mask_out(XERBits.OV32) := True
  }
  when((io.mask_in & XERMask.CA32) =/= 0) {
    io.mask_out(XERBits.CA32) := True
  }
  when((io.mask_in & XERMask.REST) =/= 0) {
    io.mask_out(63 downto 32).setAll()
    io.mask_out(28 downto 20).setAll()
    io.mask_out(17 downto 0).setAll()
  }

}
