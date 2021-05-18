package util

import chisel3._
import chisel3.util._
import chisel3.experimental.BaseModule
import chisel3.experimental.IO

class RegisteredPipeStage[A <: Data, B <: Data, I <: Data](gen: => PipeStageIO[A, B, I])
     extends MultiIOModule with PipeData[A, B, I]{

  val pipeMod = Module(gen)

  val io = IO(chiselTypeOf(pipeMod.io))
  io <> pipeMod.io

  val pipeInput = makeInput(pipeMod.pipeInput.bits.cloneType)
  val pipeOutput = makeOutput(pipeMod.pipeOutput.bits.cloneType)

  val data_reg = Reg(pipeMod.pipeOutput.bits.cloneType)
  val valid_reg = Reg(Bool())

  pipeMod.pipeInput <> pipeInput

  when(pipeOutput.ready) {
    data_reg := pipeMod.pipeOutput.bits
    valid_reg := pipeMod.pipeOutput.valid
  }

  pipeOutput.bits := data_reg
  when (pipeOutput.flush) {
    pipeOutput.valid := false.B
    valid_reg := false.B
  } .otherwise {
    pipeOutput.valid := valid_reg
  }
  pipeMod.pipeOutput.ready := pipeOutput.ready
  pipeMod.pipeOutput.flush := pipeOutput.flush

}
