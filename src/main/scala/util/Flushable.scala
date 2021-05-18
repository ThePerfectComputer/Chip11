package util

import spinal.core._
import spinal.lib._

class Flushable[+T <: Data](private val gen: T) extends Stream[T](gen) {
  val flush = in Bool()
  override def cloneType: this.type = new Flushable(gen).asInstanceOf[this.type]
}

object Flushable {
  def apply[T <: Data](gen: T): Flushable[T] = new Flushable(gen)
}
