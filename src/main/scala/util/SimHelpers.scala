package util

import scala.collection.mutable.StringBuilder
import spinal.core._
import spinal.lib._
import spinal.core.sim._

object SimHelpers{

  def vecToStringB(vec : Vec[Bool]) : String = {
    val result  = new StringBuilder("LeastSig <")
    vec.zipWithIndex.foreach{ case(el, index) => (result ++= s" ${el.toBoolean}, ")}
    result ++= "> MostSig"
    result.toString()
  }

  def vecToStringU(vec : Vec[UInt]) : String = {
    val result  = new StringBuilder("LeastSig <")
    vec.zipWithIndex.foreach{ case(el, index) => (result ++= s" ${el.toBigInt.toString(16)}, ")}
    result ++= "> MostSig"
    result.toString()
  }

}