package cpu.stages

import cpu.interfaces.{FetchOutput, BPFetchResponseInterface, LineResponse}
import cpu.shared.memory_state.{TransactionSize, TransactionType, TransactionStatus}

import util._
import spinal.core._
import spinal.lib.{master, slave}

import scala.collection._

import cpu.debug.debug_fetch_response

class FetchResponse extends PipeStage(UInt(64 bits), new FetchOutput) {
  val io = new Bundle {
  val line_response = slave(new LineResponse)
  val bp_interface = master(new BPFetchResponseInterface())
}


  // default values
  pipeOutput.payload.insn  := 0
  pipeOutput.payload.cia   := 0

  ready.allowOverride
  ready := False

  val cia = pipeInput.payload

  io.bp_interface.fetch_info.payload.insn  := pipeOutput.payload.insn
  io.bp_interface.fetch_info.payload.cia   := pipeOutput.payload.cia
  io.bp_interface.fetch_info.valid      := pipeOutput.valid

  val line_response_reg           = Reg(new LineResponse()) init(new LineResponse().getZero)
  val fifo_written                = RegInit(False)
  val accessing                   = pipeOutput.ready
  val response_present            = (io.line_response.status === TransactionStatus.DONE)
  val address_match               = (io.line_response.byte_address === cia)
  val address_match_reg           = (line_response_reg.byte_address === cia)

  when (pipeOutput.flush) {
    fifo_written := False
  }.otherwise{
    when (response_present && !fifo_written) {

      when (!accessing) {
        line_response_reg     := io.line_response
        fifo_written          := True
      }

      when (accessing && address_match) {
        ready := True
        // Is this right??
        pipeOutput.payload.insn  := io.line_response.data(31 downto 0)
        pipeOutput.payload.cia   := cia

        when(pipeOutput.fire) {debug()}
      }

    }

    when (accessing  &&  fifo_written && address_match_reg) {
      ready := True
      pipeOutput.payload.insn  := line_response_reg.data(31 downto 0)
      pipeOutput.payload.cia   := cia
      fifo_written := False

      when(pipeOutput.fire) {debug()}
    }
  }

  def debug() {
    // if (cpu.debug.debug_fetch_response){
    //   printf(p"FETCH RESPONSE: Received instruction 0x${Hexadecimal(pipeOutput.payload.insn)}\n")
    // }

  }

}
