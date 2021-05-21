package soc.devices.memory_adaptor

import soc.interfaces.MemBus128
import cpu.interfaces.{LineRequest, LineResponse}
// import cpu.debug.debug_memory_adaptor
import cpu.shared.memory_state.{TransactionStatus, TransactionType, TransactionSize}

import spinal.core._
import spinal.lib._
import scala.collection.concurrent.Debug

object DebugMemoryAdaptor {
  var debug = false
}

object MemoryAdaptorState extends SpinalEnum {
  val IDLE         = newElement()
  val TRANSACTION1 = newElement()
  val TRANSACTION2 = newElement()
}

class MemoryAdaptor() extends Component {

  val io = new Bundle {
    val request  = in(new LineRequest)
    val response = out(new LineResponse)
    val membus   = (new MemBus128).flip()

  }


  def connect_transaction1() : Unit = {
    io.membus.write_data := write_adaptor.io.transaction1_data
    io.membus.write_mask := write_adaptor.io.transaction1_mask
  }

  def connect_transaction2() : Unit = {
    io.membus.write_data := write_adaptor.io.transaction2_data
    io.membus.write_mask := write_adaptor.io.transaction2_mask
  }

  def latch_new_request() : Unit = {
    request_combined      := io.request
    request_latched       := io.request
  }

  def hold_request() : Unit = {
    request_combined := request_latched
    request_latched  := request_latched
  }


  val request_combined = new LineRequest
  val request_latched = Reg(new LineRequest)
  // dontTouch(request_latched)
  request_combined := io.request

  // default tieoffs for IO
  io.response.status       := TransactionStatus.IDLE
  io.response.byte_address := request_latched.byte_address

  io.membus.ldst_req            := TransactionType.NONE
  io.membus.quad_word_address   := 0

  // internal modules
  val load_adaptor  = new LoadAdaptor
  val write_adaptor = new WriteAdaptor

  
  // some internal signals
  val state             = RegInit(MemoryAdaptorState.IDLE)
  val aligned           = Bool()
  val start_byte        = request_combined.byte_address(3 downto 0)
  val line_address      = request_combined.byte_address(63 downto 4)
  val request_accepted  = Bool()
  
  // default values
  aligned := False
  request_accepted := False
  
  // connect up modules

  // load adaptor
  load_adaptor.io.aligned           := aligned
  load_adaptor.io.start_byte        := start_byte
  load_adaptor.io.size              := request_combined.size
  load_adaptor.io.transaction2_ack  := False
  load_adaptor.io.request_accepted  := request_accepted
  load_adaptor.io.membus_load_line  := io.membus.read_data.as(UInt)
  io.response.data                  := load_adaptor.io.line_response

  // write adaptor
  write_adaptor.io.state := state
  write_adaptor.io.start_byte := start_byte
  write_adaptor.io.size := request_combined.size
  write_adaptor.io.aligned := aligned
  write_adaptor.io.line_request := request_combined.data
  io.membus.write_data.foreach{el => el := 0}
  io.membus.write_mask.foreach{el => el := False}

  // determine when misaligned
  for(permutation <- LineRequestTruthTable.TableEntries){
    when((start_byte === permutation.start_byte) & (request_combined.size.as(UInt) === permutation.request_size)){
      aligned := Bool(permutation.bytes_in_transaction2 == 0)
    }
  }

  // TODO : do we actually need load and store to be separate?
  val load    = io.request.ldst_req === TransactionType.LOAD
  val store   = io.request.ldst_req === TransactionType.STORE
  val ack     = io.membus.status === TransactionStatus.DONE
  val request = load | store

  switch (state){

    is (MemoryAdaptorState.IDLE){
      when (request){
        request_accepted := True
        latch_new_request()
        io.membus.ldst_req := request_combined.ldst_req
        when (aligned){
          state := MemoryAdaptorState.TRANSACTION1
          connect_transaction1()
          io.membus.quad_word_address := line_address
        }
        when (!aligned){
          state := MemoryAdaptorState.TRANSACTION2
          connect_transaction2()
          io.membus.quad_word_address := line_address + 1
        }
      }

    }

    is (MemoryAdaptorState.TRANSACTION2){
      connect_transaction2()
      hold_request()
      io.membus.quad_word_address := line_address + 1
      io.response.status          := TransactionStatus.WAITING
      io.membus.ldst_req          := request_combined.ldst_req

      when (ack){
        load_adaptor.io.transaction2_ack := True
        connect_transaction1()
        state := MemoryAdaptorState.TRANSACTION1
        io.membus.quad_word_address := line_address
      }

    }

    is (MemoryAdaptorState.TRANSACTION1){
      connect_transaction1()
      hold_request()
      io.membus.quad_word_address := line_address
      io.response.status          := TransactionStatus.WAITING
      when (ack){
        io.response.status          := TransactionStatus.DONE
        when (!request) {state := MemoryAdaptorState.IDLE}
        when (request){
          request_accepted := True
          latch_new_request()
          io.membus.ldst_req := request_combined.ldst_req
          when (aligned){
            state := MemoryAdaptorState.TRANSACTION1
            connect_transaction1()
            io.membus.quad_word_address := line_address
          }
          when (!aligned){
            state := MemoryAdaptorState.TRANSACTION2
            connect_transaction2()
            io.membus.quad_word_address := line_address + 1
          }
        }
      }

    }

  }

  import spinal.core.sim._

  def debugEnabled() : Boolean = DebugMemoryAdaptor.debug
  
  if (debugEnabled()) {
    state.simPublic()
  }
  
  def debug() {
    println("MEMORY ADAPTOR:")
    println(s"MEMORY ADAPTOR STATE: ${state.toEnum}")
    if (write_adaptor.debugEnabled()) {write_adaptor.debug()}
  }

}