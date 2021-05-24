package cpu.stages

import cpu.interfaces.{FetchOutput, BPFetchRequestInterface, LineRequest, LineResponse}
import cpu.shared.memory_state.{TransactionSize, TransactionType, TransactionStatus}

import util._
import spinal.core._
import spinal.lib._

// Fetch Request Stage has no pipe input,
// has pipe output including the address of the requested instruction,
// and has side-channel output via a line request to a memory adaptor
class FetchRequest extends Component {
  val pipeOutput = master(Flushable(UInt(64 bits)))
  val io = new Bundle {
    val line_request = master(new LineRequest())

    val bp_interface = slave(new BPFetchRequestInterface())
  }

  // signals and objects
  object FetchState extends SpinalEnum {
    val firstRequest, firstResponse, steadyState = newElement()
  }
  val state = RegInit(FetchState.firstRequest)

  val cia_reg           = RegInit(U(0, 64 bits))
  val nia               = cia_reg + 4

  // default tieoffs
  pipeOutput.payload           := cia_reg
  pipeOutput.valid          := False
  io.line_request.ldst_req       := TransactionType.NONE
  io.line_request.size           := TransactionSize.WORD
  io.line_request.byte_address   := 0
  io.line_request.data           := 0



  switch (state) {

    is (FetchState.firstRequest) {
      io.line_request.ldst_req        := TransactionType.LOAD
      state                           := FetchState.firstResponse
      io.line_request.byte_address    := cia_reg
    }

    is (FetchState.firstResponse) {
      pipeOutput.valid          := True
      state                     := FetchState.firstResponse

      when(pipeOutput.fire) {
        io.line_request.ldst_req        := TransactionType.LOAD
        io.line_request.byte_address    := nia
        cia_reg                         := cia_reg + 4
        state                           := FetchState.steadyState
      }
    }

    is (FetchState.steadyState) {
      pipeOutput.valid          := True
      when(io.bp_interface.branch.valid) {
        io.line_request.ldst_req        := TransactionType.LOAD
        cia_reg                         := io.bp_interface.branch.payload
        io.line_request.byte_address    := io.bp_interface.branch.payload
        state := FetchState.firstResponse
      } .elsewhen(pipeOutput.fire) {
        io.line_request.ldst_req        := TransactionType.LOAD
        io.line_request.byte_address    := nia
        cia_reg                         := cia_reg + 4
      }
    }

  }

  // import cpu.debug.debug_fetch_request
  // if (debug_fetch_request) {
  //   when (io.bp_interface.branch.valid) {
  //     printf(p"FETCH REQUEST: Received valid branch address 0x${Hexadecimal(io.bp_interface.branch.bits)}\n")
  //   }
  //   when (pipeOutput.fire()) {
  //     printf(p"FETCH REQUEST: Requesting instruction from 0x${Hexadecimal(cia_reg)}\n")
  //   }
  // }

}
