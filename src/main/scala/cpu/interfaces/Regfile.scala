package cpu.interfaces.regfile

import cpu.shared.{RegfileInfo, RegfileInfoMasked}
import isa.{SourceSelect}
import spinal.core._
import spinal.lib._

case class Slot(val idxWid: Int, val dataWid: Int) extends Bundle {
  val idx = UInt(idxWid bits)
  val sel = SourceSelect()
  val data = UInt(dataWid bits)

  def ===(that: Slot) = {
    (this.idx === that.idx) & 
    (this.sel === that.sel) & 
    (this.data === that.data)
  }
}

class ReadPort(val idxWidth: Int, val dataWidth: Int) extends Bundle with IMasterSlave {
  def this(info: RegfileInfo) {
    this(info.idxBits, info.regSize)
  }
  val idx = out UInt(idxWidth bits)
  val data = in UInt(dataWidth bits)

  override def asMaster() : Unit = {
    in(data)
    out(idx)
  }
  override def clone = new ReadPort(idxWidth, dataWidth)
}

class WritePort(val idxWidth: Int, val dataWidth: Int) extends Bundle with IMasterSlave{
  def this(info: RegfileInfo) {
    this(info.idxBits, info.regSize)
  }
  val idx = out UInt(idxWidth bits)
  val en =  out Bool()
  val data =  out UInt(dataWidth bits)

  override def asMaster() : Unit = {
    out(data)
    out(idx)
    out(en)
  }
  override def clone = new WritePort(idxWidth, dataWidth)
}

class WritePortMasked(override val idxWidth: Int, override val dataWidth: Int, val maskWidth: Int)
    extends WritePort(idxWidth, dataWidth) {
  def this(info: RegfileInfoMasked) {
    this(info.idxBits, info.regSize, info.maskSize)
  }
  val mask = out Bits(maskWidth bits)
  override def asMaster() : Unit = {
    out(data)
    out(idx)
    out(en)
    out(mask)
  }
  override def clone = new WritePortMasked(idxWidth, dataWidth, maskWidth)
}

