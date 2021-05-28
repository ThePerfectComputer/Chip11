package util

import spinal.core._
import spinal.lib._

class Flushable[T <: Data](private val gen: HardType[T])
    extends Stream[T](gen) {
  val flush = Bool()

  override def asMaster(): Unit = {
    out(valid)
    in(ready)
    out(payload)
    in(flush)
  }

  def <<(that: Flushable[T]): Flushable[T] = connectFrom(that)

  def >>(into: Flushable[T]): Flushable[T] = {
    into << this
    into
  }

  /** Connect that to this. The valid/payload path are cut by an register stage
    */
  def <-<(that: Flushable[T]): Flushable[T] = {
    this << that.stage()
    that
  }

  /** Connect this to that. The valid/payload path are cut by an register stage
    */
  def >->(into: Flushable[T]): Flushable[T] = {
    into <-< this
    into
  }

  /** Connect that to this. The ready path is cut by an register stage
    */
  def </<(that: Flushable[T]): Flushable[T] = {
    this << that.s2mPipe
    that
  }

  /** Connect this to that. The ready path is cut by an register stage
    */
  def >/>(that: Flushable[T]): Flushable[T] = {
    that </< this
    that
  }

  /** Connect that to this. The valid/payload/ready path are cut by an register stage
    */
  def <-/<(that: Flushable[T]): Flushable[T] = {
    this << that.s2mPipe.m2sPipe()
    that
  }

  /** Connect that to this. The valid/payload/ready path are cut by an register stage
    */
  def >/->(into: Flushable[T]): Flushable[T] = {
    into <-/< this;
    into
  }

  def connectFrom(that: Flushable[T]): Flushable[T] = {
    this.valid := that.valid
    this.payload := that.payload
    that.ready := this.ready
    that.flush := this.flush
    that
  }

  override def combStage() : Flushable[T] = {
    val ret = Flushable(payloadType).setCompositeName(this, "combStage", true)
    ret << this
    ret
  }

  /** Connect this to a valid/payload register stage and return its output stream
  */
  override def stage() : Flushable[T] = this.m2sPipe()

  override def m2sPipe(
      collapsBubble: Boolean = true,
      crossClockData: Boolean = false,
      flush: Bool = null
  ): Flushable[T] = {
    val ret = Flushable(payloadType).setCompositeName(this, "m2sPipe", true)

    val rValid = RegInit(False).setCompositeName(this, "m2sPipe_rValid", true)
    val rData = Reg(payloadType).setCompositeName(this, "m2sPipe_rData", true)
    if (crossClockData) rData.addTag(crossClockDomain)

    this.ready := (Bool(collapsBubble) && !ret.valid) || ret.ready
    this.flush := ret.flush

    when(this.ready) {
      rValid := this.valid
      rData := this.payload
    }

    rValid clearWhen (ret.flush)

    ret.valid := rValid
    ret.payload := rData
    ret
  }

  // TODO test flushing
  override def s2mPipe(): Flushable[T] = {
    val ret = Flushable(payloadType).setCompositeName(this, "s2mPipe", true)

    val rValid = RegInit(False).setCompositeName(this, "s2mPipe_rValid", true)
    val rBits = Reg(payloadType).setCompositeName(this, "s2mPipe_rData", true)
    val rFlush = RegInit(False).setCompositeName(this, "s2mPipe_rFlush", true)

    ret.valid := this.valid || rValid
    this.ready := !rValid
    ret.payload := Mux(rValid, rBits, this.payload)

    rFlush := ret.flush

    when(ret.ready) {
      rValid := False
    }

    when(this.ready && (!ret.ready)) {
      rValid := this.valid
      rBits := this.payload
    }
    when(ret.flush) {
      rValid := False
      ret.valid := False
    }
    // TODO figure out how to register flush
    this.flush := ret.flush
    ret
  }

}

object Flushable {
  def apply[T <: Data](gen: HardType[T]): Flushable[T] = new Flushable(gen)
}
