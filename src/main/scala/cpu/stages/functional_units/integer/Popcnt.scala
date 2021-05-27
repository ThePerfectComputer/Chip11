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

  def doCount(data: Seq[UInt]): UInt = {
    if (data.size == 1) {
      return data(0)
    }
    val groups = data.grouped(2).toVector
    val results = groups.map(x => popCntFun(x(1), x(0)))

    doCount(results)
  }
  io.count := doCount(pop1)

}

class PopcntStage1 extends Component {
  val io = new Bundle {
    val data = in Bits (64 bits)
    val count8 = out UInt(64 bits)
  }

  val popcntMods = Seq.fill(8)(new Popcnt(8))
  val inputGroups = io.data.subdivideIn(8 slices)
  val count8Outputs = Seq.fill(8)(UInt(log2Up(8+1) bits))
  val count8Groups = io.count8.subdivideIn(8 slices)
  for((mod, input) <- popcntMods zip inputGroups){
    mod.io.data_in := input
  }
  for((mod, output) <- popcntMods zip count8Outputs){
    output := mod.io.count
  }
  for((output, count8) <- count8Outputs zip count8Groups){
    count8 := output.resized
  }

}

class PopcntStage2 extends Component {
  val io = new Bundle {
    val data = in UInt (64 bits)
    val count = out UInt (log2Up(64 + 1) bits)
    val count32 = out UInt(64 bits)
  }
  val count8GroupsLarge = io.data.subdivideIn(8 slices)
  val count8Groups = count8GroupsLarge.map(g => g(3 downto 0))

  def popCntFun(left: UInt, right: UInt): UInt = {
    val width = left.getWidth
    val res = UInt(width + 1 bits)
    res := left +^ right
    res
  }
  def doCount(data: Seq[UInt]): UInt = {
    if (data.size == 1) {
      return data(0)
    }
    val groups = data.grouped(2).toVector
    val results = groups.map(x => popCntFun(x(1), x(0)))

    doCount(results)
  }
  val count32Inputs = count8Groups.grouped(4)
  val count32Outputs = count32Inputs.map(doCount).toList
  val count32Groups = io.count32.subdivideIn(2 slices)
  for((output, group) <- count32Outputs zip count32Groups){
    group := output.resize(32)
  }

  io.count := doCount(count32Outputs)
}

class PopcntB extends Component {
  val io = new Bundle {
    val data = in Bits (64 bits)
    val count = out UInt (log2Up(64 + 1) bits)
    val count8 = out UInt(64 bits)
    val count32 = out UInt(64 bits)
  }
  val popcntMods = Seq.fill(8)(new Popcnt(8))
  val inputGroups = io.data.subdivideIn(8 slices)
  val count8Outputs = Seq.fill(8)(UInt(log2Up(8+1) bits))
  val count8Groups = io.count8.subdivideIn(8 slices)
  for((mod, input) <- popcntMods zip inputGroups){
    mod.io.data_in := input
  }
  for((mod, output) <- popcntMods zip count8Outputs){
    output := mod.io.count
  }
  for((output, count8) <- count8Outputs zip count8Groups){
    count8 := output.resized
  }

  def popCntFun(left: UInt, right: UInt): UInt = {
    val width = left.getWidth
    val res = UInt(width + 1 bits)
    res := left +^ right
    res
  }
  def doCount(data: Seq[UInt]): UInt = {
    if (data.size == 1) {
      return data(0)
    }
    val groups = data.grouped(2).toVector
    val results = groups.map(x => popCntFun(x(1), x(0)))

    doCount(results)
  }
  val count32Inputs = count8Outputs.grouped(4)
  val count32Outputs = count32Inputs.map(doCount).toList
  val count32Groups = io.count32.subdivideIn(2 slices)
  for((output, group) <- count32Outputs zip count32Groups){
    group := output.resize(32)
  }

  io.count := doCount(count32Outputs)

}
