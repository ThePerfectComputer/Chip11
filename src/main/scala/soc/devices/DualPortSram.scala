package soc.devices

import soc.interfaces.MemBus128
import cpu.shared.memory_state.{TransactionStatus, TransactionType}

import spinal.core._
import spinal.lib._

class DualPortSram128(depth: Int=8, dataWidth: Int=128) extends Component {
  val io = new Bundle {
    val port_1 = new MemBus128()
    val port_2 = new MemBus128()
  }
  // val mem_file: String="c_sources/simple_test/program.mem"

  val mem = Mem(Vec(UInt(8 bits), 16), depth)
  val address_width = mem.addressWidth
  println(s"address width is ${address_width}")
  // loadMemoryFromFile(mem, mem_file)

  val port1_do_load  = (io.port_1.ldst_req === TransactionType.LOAD)
  val port1_do_store = (io.port_1.ldst_req === TransactionType.STORE)

  val port2_do_load  = (io.port_2.ldst_req === TransactionType.LOAD)
  val port2_do_store = (io.port_2.ldst_req === TransactionType.STORE)

  val port1_status_reg   = RegInit(TransactionStatus.IDLE)
  val port2_status_reg   = RegInit(TransactionStatus.IDLE)

  io.port_1.status := port1_status_reg
  io.port_2.status := port2_status_reg
  
  // for the fetch stage
  io.port_1.read_data := mem.readSync(io.port_1.quad_word_address.take(mem.addressWidth).asUInt, enable=port1_do_load)
  when(port1_do_load | port1_do_store) {
    port1_status_reg := TransactionStatus.DONE
  }.otherwise{
    port1_status_reg := TransactionStatus.IDLE
  }

  // for the loadstore stage
  when (port2_do_store) {mem.write(io.port_2.quad_word_address.take(mem.addressWidth).asUInt, io.port_2.write_data, mask=io.port_2.write_mask.asBits)}
  io.port_2.read_data := mem.readSync(io.port_2.quad_word_address.take(mem.addressWidth).asUInt, enable=port2_do_load)
  when(port2_do_load | port2_do_store) {
    port2_status_reg := TransactionStatus.DONE
  }.otherwise{
    port2_status_reg := TransactionStatus.IDLE
  }

}

object runme {
  def main(args : Array[String]) = println("I compiled")
}