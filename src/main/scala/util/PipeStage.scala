package util

import spinal.core._
import spinal.lib._


trait PipeData[A <: Data, B <: Data, I <: Data] {
  val io : I

  val pipeInput : Flushable[A]
  val pipeOutput : Flushable[B]

  def makeInput(inp: A) = Flushable(inp.asInput()).flip()
  def makeOutput(outp: B) = Flushable(outp.asOutput())
  // def makeOutput(outp: B) = IO(Flushable(Output(outp)))


  def connect[C <: Data, J <: Data](other: PipeData[B, C, J]) = {
    other.pipeInput <> pipeOutput
    other
  }
  def connectOut(sig: Flushable[B]) = {
    sig <> pipeOutput
  }

  def connectIn[C <: Data, J <: Data](other: PipeData[A, C, J]) = {
    other.pipeInput <> pipeInput
    other
  }
  def connectFrom(data: Flushable[A]) = {
    pipeInput <> data
  }
}


abstract class PipeStageIO[A <: Data, B <: Data, I <: Data](inp:A, outp: B, io_t: I) extends PipeData[A, B, I]{

  val io = io_t

  val pipeInput = makeInput(inp)
  val pipeOutput = makeOutput(outp)

  val i = pipeInput.payload
  val o = pipeOutput.payload
  val ready = True
  val flush = False

  pipeInput.ready := pipeOutput.ready & ready
  pipeInput.flush := pipeOutput.flush | flush

  when(pipeOutput.flush) {
    pipeOutput.valid := False
  }.otherwise {
    pipeOutput.valid := pipeInput.valid & ready
  }

}

class EmptyBundle extends Bundle {
  val bit = out Bool
}
abstract class PipeStage[A <: Data, B <: Data](inp:A, outp: B) extends PipeStageIO(inp, outp, new EmptyBundle){
  io.asBits := 0
}


class Delay[T <: Data](gen:T) extends PipeStage(gen, gen) {
  i <> o

}

