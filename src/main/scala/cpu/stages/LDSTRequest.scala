package cpu.stages

import cpu.interfaces.{FunctionalUnitExit, WriteInterface, LineRequest}
import cpu.shared.memory_state.{TransactionType}
import util.PipeStage
import cpu.debug.debug_ldst

import spinal.core._
import spinal.lib._

class LDSTRequest
    extends PipeStage(new FunctionalUnitExit, new FunctionalUnitExit) {
  val io = master(new LineRequest)

  io.ldst_req := TransactionType.NONE
  io.size := i.ldst_request.size
  io.data := i.ldst_request.store_data
  io.byte_address := i.ldst_request.ea

  when(pipeInput.valid) {
    io.ldst_req := i.ldst_request.req_type
    when(i.ldst_request.req_type =/= TransactionType.NONE) {
      ready := io.ack
    }
  }

  when(i.ldst_request.req_type === TransactionType.STORE) {
    when(pipeOutput.valid) {
      if (debug_ldst) {
        report(L"STORE: ${pipeInput.payload.cia} writing ${io.data} to ${io.byte_address} ${io.size}")
      }
    }
  }
  when(i.ldst_request.req_type === TransactionType.LOAD) {
    when(pipeOutput.valid) {
      if (debug_ldst) {
        report(L"LOAD: ${pipeInput.payload.cia} from ${io.byte_address} ${io.size}")
      }
    }
  }

  o := i
}
