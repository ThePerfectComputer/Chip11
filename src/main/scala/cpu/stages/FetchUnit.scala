package cpu.stages

import cpu.interfaces.{
  FetchOutput,
  BPFetchRequestInterface,
  LineRequest,
  LineResponse
}
import cpu.shared.memory_state.{
  TransactionSize,
  TransactionType,
  TransactionStatus
}

import util._
import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

// Fetch Request Stage has no pipe input,
// has pipe output including the address of the requested instruction,
// and has side-channel output via a line request to a memory adaptor
class FetchUnit extends Component {
  val pipeOutput = master(Flushable(new FetchOutput))
  val io = new Bundle {
    val line_request = master(new LineRequest())
    val line_response = slave(new LineResponse)

    val bp_interface = slave(new BPFetchRequestInterface())
  }

  pipeOutput.valid := False
  pipeOutput.payload := pipeOutput.payload.getZero

  val dataFifo = StreamFifo(Bits(128 bits), 4)
  dataFifo.io.push.valid := False
  dataFifo.io.push.payload := 0
  dataFifo.io.pop.ready := False
  io.line_request.ldst_req := TransactionType.NONE
  io.line_request.size := TransactionSize.QUADWORD
  io.line_request.byte_address := 0
  io.line_request.data := 0

  val busArea = new Area {
    val cia = RegInit(U(0x10, 64 bits))

    val fsm = new StateMachine {
      val requestState: State = new State with EntryPoint {
        whenIsActive {
          when(dataFifo.io.push.ready & ~io.bp_interface.branch.valid) {
            io.line_request.ldst_req := TransactionType.LOAD
            io.line_request.byte_address := cia
            cia := (cia & ~U(0xf, 64 bits)) + 0x10
            goto(responseState)
          }
        }
      }
      val responseState: State = new State {
        whenIsActive {
          when(io.line_response.status === TransactionStatus.DONE) {
            dataFifo.io.push.valid := True
            dataFifo.io.push.payload := io.line_response.data.asBits
            goto(requestState)
          }
        }
      }

    }
    when(io.bp_interface.branch.valid) {
      cia := io.bp_interface.branch.payload & ~U(0xf, 64 bits)
    }
  }

  val deserializeArea = new Area {
    val cia = RegInit(U(0x10, 64 bits))
    val word_addr = cia(3 downto 2)
    val dataOut = dataFifo.io.pop.payload.subdivideIn(32 bits)

    val fsm = new StateMachine {
      val running: State = new State with EntryPoint {
        whenIsActive {
          when(dataFifo.io.pop.valid & pipeOutput.ready) {
            pipeOutput.payload.insn := dataOut(word_addr).asUInt
            pipeOutput.payload.cia := cia
            pipeOutput.valid := True
            cia := cia + 4
            when(word_addr === 0x3) {
              dataFifo.io.pop.ready := True
            }
          }
        }
      }

    }

    when(io.bp_interface.branch.valid) {
      cia := io.bp_interface.branch.payload & ~U(0xf, 64 bits)
    }
  }

}
