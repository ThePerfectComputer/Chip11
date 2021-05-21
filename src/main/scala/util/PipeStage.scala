package util

import spinal.core._
import spinal.lib._


trait PipeData[A <: Data, B <: Data] {

  val pipeInput : Flushable[A]
  val pipeOutput : Flushable[B]

  def makeInput(inp: HardType[A]) = slave(Flushable(inp()))
  def makeOutput(outp: HardType[B]) = master(Flushable(outp()))
  // def makeOutput(outp: B) = IO(Flushable(Output(outp)))

  def >>[C <: Data](other: PipeData[B, C]) = {
    pipeOutput >> other.pipeInput 
    other
  }
  def >>[C <: Data](other: Flushable[B]) = {
    pipeOutput >> other
    other
  }
  def <<[C <: Data](other: Flushable[A]) = {
    other >> pipeInput
    other
  }
  def <<[C <: Data](other: PipeData[C, A]) = {
    pipeInput << other.pipeOutput 
    other
  }
  def <-<[C <: Data](other: PipeData[C, A]) = {
    pipeInput <-< other.pipeOutput 
    other
  }
  def >->[C <: Data](other: PipeData[B, C]) = {
    pipeOutput >-> other.pipeInput 
    other
  }
  def >->[C <: Data](other: Flushable[B]) = {
    pipeOutput >-> other
    other
  }
  def <-/<[C <: Data](other: PipeData[C, A]) = {
    pipeInput <-/< other.pipeOutput 
    other
  }
  def >/->[C <: Data](other: PipeData[B, C]) = {
    pipeOutput >/-> other.pipeInput 
    other
  }
  def >/->[C <: Data](other: Flushable[B]) = {
    pipeOutput >/-> other
    other
  }

}


abstract class PipeStage[A <: Data, B <: Data](inp: HardType[A], outp: HardType[B]) extends Component with PipeData[A, B]{


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


class Delay[T <: Data](gen:T) extends PipeStage(gen, gen) {
  o := i

}

