package cpu.stages

import cpu.interfaces.{FunctionalUnitExit, WriteInterface, LineRequest}
import cpu.shared.memory_state.{TransactionType}
import util.PipeStage

import spinal.core._
import spinal.lib._

class LDSTRequest extends PipeStage(new FunctionalUnitExit, new FunctionalUnitExit) {
  val io = master(new LineRequest)

  io.ldst_req         := TransactionType.NONE
  io.size             := i.ldst_request.size
  io.data             := i.ldst_request.store_data
  io.byte_address     := i.ldst_request.ea

  when (pipeInput.valid) {io.ldst_req := i.ldst_request.req_type}

  o := i
}
