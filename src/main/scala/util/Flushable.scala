package util

import spinal.core._
import spinal.lib._

class Flushable[T <: Data](private val gen: T) extends Stream[T](gen) {
  val flush = Bool()

  override def asMaster(): Unit = {
    out(valid)
    in(ready)
    out(payload)
    in(flush)
  }
}

object Flushable {
  def apply[T <: Data](gen: T): Flushable[T] = new Flushable(gen)
}
