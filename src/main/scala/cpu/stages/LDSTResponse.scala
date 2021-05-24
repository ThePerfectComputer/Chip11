package cpu.stages

import cpu.interfaces.{FunctionalUnitExit, WriteStageInterface, LineResponse}
import cpu.shared.memory_state.{TransactionStatus}
import cpu.shared.memory_state.{TransactionType}
import util.PipeStage

import spinal.core._
import spinal.lib._

class LDSTResponse extends PipeStage(new FunctionalUnitExit, new WriteStageInterface) {
  val io = slave(new LineResponse)
  val ldst_req = i.ldst_request

  when (i.ldst_request.req_type =/= TransactionType.LOAD){
    when(io.status === TransactionStatus.DONE) {
      ready := True
    } .elsewhen(io.status === TransactionStatus.IDLE) {
      ready := True
    } .otherwise {
      ready := False
    }
  }

  o.write_interface := i.write_interface
  o.cia := i.cia

  when(io.status === TransactionStatus.DONE) {
    for((slot, i) <- o.write_interface.slots.zipWithIndex) {
      when(ldst_req.load_dest_slot === i) {
        slot.data := io.data.resized
      }
    }
  }

}
