package util

import spinal.core._
import spinal.lib._

object Mux1H {
  def apply[T <: Data](oneHot : BitVector,inputs : Seq[T]): T = apply(oneHot.asBools,inputs)
  def apply[T <: Data](oneHot : collection.IndexedSeq[Bool],inputs : Iterable[T]): T =  apply(oneHot,Vec(inputs))

  def apply[T <: Data](oneHot : BitVector,inputs : Vec[T]): T = apply(oneHot.asBools,inputs)
  def apply[T <: Data](oneHot : collection.IndexedSeq[Bool],inputs : Vec[T]): T = {
    val oneHotBits = oneHot.asBits
    var maxWidth :Int = 0
      for(item <- inputs){
        if(item.getBitsWidth > maxWidth){
          maxWidth = item.getBitsWidth
        }
      }
      val outBits = Bits(maxWidth bits)
      outBits := 0

    oneHotSwitch(oneHotBits){
      for((item, i) <- inputs.zipWithIndex){
        is(i){
          outBits := item.asBits
        }
      }
    }
      val out = cloneOf(inputs(0))
      out.assignFromBits(outBits)
      out

    // var maxWidth :Int = 0
    // for(item <- inputs){
    //   if(item.getBitsWidth > maxWidth){
    //     maxWidth = item.getBitsWidth
    //   }
    // }
    // val muxedVec = Vec(Bits(maxWidth bits), inputs.size)
    // for((item, idx) <- inputs.zipWithIndex){
    //   val muxed = Bits(maxWidth bits).setPartialName(s"muxed_$idx")
    //   muxed := Mux(oneHot(idx), inputs(idx).asBits, B(0, maxWidth bits))
    //   muxedVec(idx) := muxed
    // }
    // val outBits = Bits(maxWidth bits)
    // outBits := muxedVec.reduceLeft(_ | _)

    // val out = cloneOf(inputs(0))
    // out.assignFromBits(outBits)
    // out
  }
}
