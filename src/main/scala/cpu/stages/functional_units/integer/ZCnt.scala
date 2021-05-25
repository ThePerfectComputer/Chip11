package cpu.stages.functional_units.integer

import cpu.interfaces.{DecoderData, ReadInterface}
import util.{PipeStage, Delay}
import isa.MnemonicEnums

import spinal.core._
import spinal.lib._

class LZCnt(width: Int) extends Component {
  val io = new Bundle {
    val data_in = in Bits (width bits)
    val count = out UInt (log2Up(width + 1) bits)
  }

  val pairs = io.data_in.subdivideIn(2 bits)

  def lzcntPair(x: Bits): Bits = {
    val res = Bits(2 bits)
    switch(x) {
      is(0) { res := 2 }
      is(1) { res := 1 }
      default { res := 0 }
    }
    res
  }

  def lzCntFun(left: Bits, right: Bits): Bits = {
    val width = left.getWidth
    val res = Bits(width + 1 bits)
    when(left(width - 1) === True) {
      when(right(width - 1) === True) {
        res := Cat(True, B(0, width bits))
      }.otherwise {
        res := Cat(B(1, 2 bits), right(width - 2 downto 0))
      }
    }.otherwise {
      res := Cat(False, left)
    }
    res
  }

  val lz1 = pairs.map(lzcntPair).toVector

  def doCount(data: IndexedSeq[Bits]): UInt = {
    if (data.size == 1) {
      return data(0).asUInt
    }
    val groups = data.grouped(2).toVector
    val results = groups.map(x => lzCntFun(x(1), x(0)))

    doCount(results)
  }
  io.count := doCount(lz1)

}

class ZCnt extends Component {
  val io = new Bundle {
    val data = in UInt (64 bits)
    val count = out UInt (log2Up(64 + 1) bits)

    val countLeadingZeros = in Bool
    val isWord = in Bool
  }
  val lzCntMod = new LZCnt(64)
  val data = Bits(64 bits)

  lzCntMod.io.data_in := data

  when(io.countLeadingZeros) {
    data := io.data.asBits
  }.otherwise {
    data := Reverse(io.data).asBits
  }
  io.count := lzCntMod.io.count
  when(io.isWord) {
    io.count := lzCntMod.io.count - 32
  }

}
