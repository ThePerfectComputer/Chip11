package soc.devices.memory_adaptor

import cpu.interfaces.{LineRequest, LineResponse}
import spinal.lib.bus.amba4.axi.{
  Axi4Shared,
  Axi4SlaveFactory,
  Axi4Config,
  Axi4CrossbarFactory
}
// import cpu.debug.debug_memory_adaptor
import cpu.shared.memory_state.{TransactionStatus, TransactionType, TransactionSize}

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

class LineAXIAdaptor(id: Int)(implicit axiConfig: Axi4Config)
    extends Component {

  val io = new Bundle {
    val request  = slave(new LineRequest)
    val response = master(new LineResponse)

    val axi = master(Axi4Shared(axiConfig))
  }

  val requestReg = Reg(new LineRequest)

  // TODO convert this to a pipelined implementation. For now we'll stick with a state machine

  io.axi.arw.valid := False
  io.axi.r.ready := False
  io.axi.b.ready := False
  io.axi.w.valid := False
  //io.axi.w.valid.noCombLoopCheck

  io.axi.arw.size := 0
  io.axi.arw.payload.addr := 0
  io.axi.arw.write := False
  io.axi.arw.burst := 0
  io.axi.arw.len := 0
  io.axi.arw.id := id

  io.axi.w.last := False
  io.axi.w.payload.data := 0
  io.axi.w.strb := 0

  io.request.ack := False

  io.response.byte_address := 0
  io.response.data := 0
  io.response.status := TransactionStatus.IDLE

  val fsm = new StateMachine {
    val address1 = new State with EntryPoint
    val address2 = new State
    val read1 = new State
    val read2 = new State
    val write1 = new State
    val write2 = new State

    // Handles the awr channel
    address1.whenIsActive {
      // When valid
      when(io.request.ldst_req =/= TransactionType.NONE){
        requestReg := io.request
        io.request.ack := True
        goto(address2)
      }
    }

  }

}
