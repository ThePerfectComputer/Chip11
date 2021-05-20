package util

import spinal.core._
import spinal.lib._

object Mux1H {
  def apply[T <: Data](oneHot : BitVector,inputs : Seq[T]): T = apply(oneHot.asBools,inputs)
  def apply[T <: Data](oneHot : collection.IndexedSeq[Bool],inputs : Iterable[T]): T =  apply(oneHot,Vec(inputs))

  def apply[T <: Data](oneHot : BitVector,inputs : Vec[T]): T = apply(oneHot.asBools,inputs)
  def apply[T <: Data](oneHot : collection.IndexedSeq[Bool],inputs : Vec[T]): T = {
    var maxWidth :Int = 0
    for(item <- inputs){
      if(item.getBitsWidth > maxWidth){
        maxWidth = item.getBitsWidth
      }
    }
    val muxedVec = Vec(Bits(maxWidth bits), inputs.size)
    for((item, idx) <- inputs.zipWithIndex){
      val muxed = Bits(maxWidth bits).setPartialName(s"muxed_$idx")
      muxed := Mux(oneHot(idx), inputs(idx).asBits, B(0, maxWidth bits))
      muxedVec(idx) := muxed
    }
    val outBits = Bits(maxWidth bits)
    outBits := muxedVec.reduceLeft(_ | _)

    val out = cloneOf(inputs(0))
    out.assignFromBits(outBits)
    out
  }
}
