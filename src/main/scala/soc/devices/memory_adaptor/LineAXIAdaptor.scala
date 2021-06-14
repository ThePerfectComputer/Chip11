package soc.devices.memory_adaptor

import cpu.interfaces.{LineRequest, LineResponse}
import spinal.lib.bus.amba4.axi.{
  Axi4Shared,
  Axi4SlaveFactory,
  Axi4Config,
  Axi4CrossbarFactory
}
// import cpu.debug.debug_memory_adaptor
import cpu.shared.memory_state.{
  TransactionStatus,
  TransactionType,
  TransactionSize
}

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

class LineAXIAdaptor(id: Int)(implicit axiConfig: Axi4Config)
    extends Component {

  val io = new Bundle {
    val request = slave(new LineRequest)
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

  val req_start_byte = requestReg.byte_address(3 downto 0)
  val req_size = requestReg.size.as(UInt)
  // Whether the transaction will have to use two bus accesses
  val bus_aligned = Bool
  bus_aligned := False
  // Whether the transaction address is aligned to the size
  val byte_aligned = Bool
  byte_aligned := False

  for (permutation <- LineRequestTruthTable.TableEntries) {
    when(
      (req_start_byte === permutation.start_byte) & (req_size === permutation.request_size)
    ) {
      bus_aligned := Bool(permutation.bytes_in_transaction2 == 0)
      byte_aligned := Bool(permutation.byte_addr_aligned)
    }
  }

  // The size field presented to the axi bus
  val axi_size = UInt(3 bits)
  axi_size := 4
  when(byte_aligned) {
    switch(requestReg.size) {
      is(TransactionSize.BYTE) { axi_size := 0 }
      is(TransactionSize.HALFWORD) { axi_size := 1 }
      is(TransactionSize.WORD) { axi_size := 2 }
      is(TransactionSize.DOUBLEWORD) { axi_size := 3 }
      is(TransactionSize.QUADWORD) { axi_size := 4 }
    }
  }

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
      when(io.request.ldst_req =/= TransactionType.NONE) {
        requestReg := io.request
        io.request.ack := True
        goto(address2)
      }
    }
    address2.whenIsActive {
      io.axi.arw.addr := requestReg.byte_address
      io.axi.arw.id := id
      io.axi.arw.write := requestReg.ldst_req === TransactionType.STORE
      io.axi.arw.size := axi_size
      io.axi.arw.burst := 1 // INCR
      io.axi.arw.len := U(~bus_aligned).resized
      io.axi.arw.valid := True
      when(io.axi.arw.ready){
        when(requestReg.ldst_req === TransactionType.LOAD){
          when(bus_aligned){
            goto(read2)
          }.otherwise {
            goto(read1)
          }
        }.otherwise {
          when(bus_aligned){
            goto(write2)
          }.otherwise {
            goto(write1)
          }
        }
      }
    }

  }

}
