package cpu.stages

import cpu.interfaces.{FunctionalUnitExit, WriteStageInterface, LineResponse}
import cpu.shared.memory_state.{TransactionStatus, TransactionType, TransactionSize}
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

  val resp_data = UInt(128 bits)
  resp_data := io.data.resized

  when(ldst_req.arithmetic){
    when(ldst_req.size === TransactionSize.HALFWORD){
      resp_data := io.data(15 downto 0).asSInt.resize(128).asUInt
    }
    when(ldst_req.size === TransactionSize.WORD){
      resp_data := io.data(31 downto 0).asSInt.resize(128).asUInt
    }

  }

  when(io.status === TransactionStatus.DONE && ldst_req.req_type === TransactionType.LOAD) {
    for((slot, i) <- o.write_interface.slots.zipWithIndex) {
      when(ldst_req.load_dest_slot === i) {
        slot.data := resp_data.resized
      }
    }
  }

}
