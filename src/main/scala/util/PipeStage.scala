package util

import spinal.core._
import spinal.lib._


trait PipeData[A <: Data, B <: Data, I <: Data] {
  val io : I

  val pipeInput : Flushable[A]
  val pipeOutput : Flushable[B]

  def makeInput(inp: HardType[A]) = slave(Flushable(inp()))
  def makeOutput(outp: HardType[B]) = master(Flushable(outp()))
  // def makeOutput(outp: B) = IO(Flushable(Output(outp)))

  def >>[C <: Data, J <: Data](other: PipeData[B, C, J]) = {
    pipeOutput >> other.pipeInput 
    other
  }
  def >>[C <: Data, J <: Data](other: Flushable[B]) = {
    pipeOutput >> other
    other
  }
  def <<[C <: Data, J <: Data](other: Flushable[A]) = {
    other >> pipeInput
    other
  }
  def <<[C <: Data, J <: Data](other: PipeData[C, A, J]) = {
    pipeInput << other.pipeOutput 
    other
  }
  def <-<[C <: Data, J <: Data](other: PipeData[C, A, J]) = {
    pipeInput <-< other.pipeOutput 
    other
  }
  def >->[C <: Data, J <: Data](other: PipeData[B, C, J]) = {
    pipeOutput >-> other.pipeInput 
    other
  }
  def <-/<[C <: Data, J <: Data](other: PipeData[C, A, J]) = {
    pipeInput <-/< other.pipeOutput 
    other
  }
  def >/->[C <: Data, J <: Data](other: PipeData[B, C, J]) = {
    pipeOutput >/-> other.pipeInput 
    other
  }

}


abstract class PipeStageIO[A <: Data, B <: Data, I <: Data](inp: HardType[A], outp: HardType[B], io_t: HardType[I]) extends Component with PipeData[A, B, I]{

  val io = io_t().asOutput()

  val pipeInput = slave(Flushable(inp()))
  val pipeOutput = master(Flushable(outp()))

  val i = pipeInput.payload
  val o = pipeOutput.payload
  val ready = True
  val flush = False

  println(pipeInput)
  println(pipeOutput)

  pipeInput.ready := pipeOutput.ready & ready
  pipeInput.flush := pipeOutput.flush | flush

  when(pipeOutput.flush) {
    pipeOutput.valid := False
  }.otherwise {
    pipeOutput.valid := pipeInput.valid & ready
  }
  pipeInput.ready.allowOverride
  pipeInput.flush.allowOverride
  pipeOutput.valid.allowOverride
  pipeOutput.payload.allowOverride


}

class EmptyBundle extends Bundle  with IMasterSlave {
  val bit = out UInt(5 bits)
  override def asMaster(): Unit = {
    out(bit)
  }
}

abstract class PipeStage[A <: Data, B <: Data](inp:A, outp: B) extends PipeStageIO(inp, outp, new EmptyBundle){
  io := io.getZero
}


class Delay[T <: Data](gen:T) extends PipeStage(gen, gen) {
  o := i

}

