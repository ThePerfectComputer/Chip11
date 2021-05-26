package cpu.shared
import spinal.core._

import cpu.interfaces.regfile.{SourceSelect}

class RegfileInfo(
  val numRegisters: Int,
  val regSize: Int,
  val selector: SourceSelect.E){

  val idxBits = log2Up(numRegisters)
}

class RegfileInfoMasked(
  numRegisters: Int,
  regSize: Int,
  selector: SourceSelect.E,
  val maskSize: Int) extends
    RegfileInfo(numRegisters, regSize, selector){
}

object RegfileInfo {
  val GPR = new RegfileInfo(32, 64, SourceSelect.GPR)
  val SPR = new RegfileInfo(1024, 64, SourceSelect.SPR)
  val CRA = new RegfileInfoMasked(1, 16, SourceSelect.CRA, 4)
  val CRB = new RegfileInfoMasked(1, 16, SourceSelect.CRB, 4)
  val VR = new RegfileInfo(32, 128, SourceSelect.VR)
  val VSR = new RegfileInfo(64, 128, SourceSelect.VSR)
  val FPR = new RegfileInfo(32, 128, SourceSelect.FPR)
  val COMB = new RegfileInfo(32, 64, SourceSelect.COMBINED)
  val BHRB = new RegfileInfo(32, 64, SourceSelect.BHRB)
  val FPSCR = new RegfileInfo(1, 16, SourceSelect.FPSCR)
  val XER = new RegfileInfoMasked(1, 64, SourceSelect.XER, 6)

  var map = Map[SourceSelect.E, RegfileInfo]()
  map += (SourceSelect.GPR -> GPR)
  map += (SourceSelect.SPR -> SPR)
  map += (SourceSelect.CRA -> CRA)
  map += (SourceSelect.CRB -> CRB)
  map += (SourceSelect.VR -> VR)
  map += (SourceSelect.VSR -> VSR)
  map += (SourceSelect.FPR -> FPR)
  map += (SourceSelect.COMBINED -> COMB)
  map += (SourceSelect.BHRB -> BHRB)
  map += (SourceSelect.FPSCR -> FPSCR)
  map += (SourceSelect.XER -> XER)
  def apply(sel: SourceSelect.E) = map.get(sel)
}
