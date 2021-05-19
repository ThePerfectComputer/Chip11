package cpu.interfaces.regfile

import spinal.core._
import spinal.lib._

class Slot(val idxWid: Int, val dataWid: Int) extends Bundle {
  val idx = UInt(idxWid bits)
  val sel = SourceSelect()
  val data = UInt(dataWid bits)

  def ===(that: Slot) = {
    (this.idx === that.idx) & 
    (this.sel === that.sel) & 
    (this.data === that.data)
  }
}

class ReadPort(val idxWidth: Int, val dataWidth: Int) extends Bundle with IMasterSlave{
  val idx = UInt(idxWidth bits)
  val data = UInt(dataWidth bits)

  override def asMaster() : Unit = {
    in(data)
    out(idx)
  }
}

class WritePort(val idxWidth: Int, val dataWidth: Int) extends Bundle with IMasterSlave{
  val idx = UInt(idxWidth bits)
  val en = Bool()
  val data = UInt(dataWidth bits)

  override def asMaster() : Unit = {
    out(data)
    out(idx)
    out(en)
  }
}

class WritePortMasked(override val idxWidth: Int, override val dataWidth: Int, val maskWidth: Int)
    extends WritePort(idxWidth, dataWidth) {
  val mask = Vec(Bool(), maskWidth)
  override def asMaster() : Unit = {
    out(data)
    out(idx)
    out(en)
    out(mask)
  }
}

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
}
