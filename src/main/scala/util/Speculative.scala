package util

import chisel3._
import chisel3.util._
import chisel3.experimental.IO

class Speculative[+T <: Data](private val gen: T) extends IrrevocableIO[T](gen) {
  val spec = Output(Bool())
  override def cloneType: this.type = new Speculative(gen).asInstanceOf[this.type]
}

object Speculative {
  def apply[T <: Data](gen: T): Speculative[T] = new Speculative(gen)
}

class SpecControl extends Bundle {
  val flush = Bool()
  val clear = Bool()
}
