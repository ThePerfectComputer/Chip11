package cpu.stages.functional_units.integer

import cpu.interfaces.{DecoderData, ReadInterface}
import util.{PipeStage, Delay}
import isa.MnemonicEnums

import spinal.core._
import spinal.lib._

class Popcnt(width: Int) extends Component {
  val io = new Bundle {
    val data_in = in Bits (width bits)
    val count = out UInt (log2Up(width + 1) bits)
  }


  def popCntBitPair(x: Bits): UInt = {
    val res = UInt(2 bits)
    switch(x) {
      is(3) { res := 2 }
      is(2) { res := 1 }
      is(1) { res := 1 }
      default { res := 0 }
    }
    res
  }

  def popCntFun(left: UInt, right: UInt): UInt = {
    val width = left.getWidth
    val res = UInt(width + 1 bits)
    res := left +^ right
    res
  }

  val pairs = io.data_in.subdivideIn(2 bits)

  val pop1 = pairs.map(popCntBitPair).toVector

  def doCount(data: IndexedSeq[UInt]): UInt = {
    if (data.size == 1) {
      return data(0)
    }
    val groups = data.grouped(2).toVector
    val results = groups.map(x => popCntFun(x(1), x(0)))

    doCount(results)
  }
  io.count := doCount(pop1)

}
