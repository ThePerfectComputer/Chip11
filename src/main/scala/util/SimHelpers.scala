package util

import scala.collection.mutable.StringBuilder
import spinal.core._
import spinal.lib._
import spinal.core.sim._

object SimHelpers{
  def vecToString(vec : Vec[Bool]) : String = {
    val result  = new StringBuilder("LSB <")
    vec.zipWithIndex.foreach{ case(el, index) => (result ++= s" ${el.toBoolean}, ")}
    result ++= "> MSB"
    result.toString()
  }
}