package util

import spinal.core._
import spinal.lib._

class RegisteredPipeStage[A <: Data, B <: Data, I <: Data](gen: => PipeStageIO[A, B, I])
     extends Component with PipeData[A, B, I]{

  val pipeMod = gen

  val io = cloneOf(pipeMod.io)
  io <> pipeMod.io

  val pipeInput = makeInput(pipeMod.pipeInput.payload)
  val pipeOutput = makeOutput(pipeMod.pipeOutput.payload)

  val data_reg = Reg(cloneOf(pipeMod.pipeOutput.payload))
  val valid_reg = Reg(Bool())

  pipeMod.pipeInput <> pipeInput

  when(pipeOutput.ready) {
    data_reg := pipeMod.pipeOutput.payload
    valid_reg := pipeMod.pipeOutput.valid
  }

  pipeOutput.payload := data_reg
  when (pipeOutput.flush) {
    pipeOutput.valid := False
    valid_reg := False
  } .otherwise {
    pipeOutput.valid := valid_reg
  }
  pipeMod.pipeOutput.ready := pipeOutput.ready
  pipeMod.pipeOutput.flush := pipeOutput.flush

}
