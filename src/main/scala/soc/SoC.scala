package soc

import cpu.{CPU}
import soc.devices.memory_adaptor.MemoryAdaptor
//import soc.devices.{DualPortSram128, UART, Arbiter, AdaptorPeripheral}
import soc.devices.{DualPortSram128}

import spinal.core._
import spinal.lib._

object debug {
  val debug   = false
  val uart    = debug
  val arbiter = debug
}

class SoC(val mem_file:String = null) extends Component {

  // instantiate the CPU and memory
  val cpu = new CPU
  val ram = new DualPortSram128(depth=512, mem_file=mem_file)

  // use an adaptor to connect the CPU's fetch unit to the RAM's read-only port
  val fetch_adaptor = new MemoryAdaptor
  cpu.io.fetch_request <> fetch_adaptor.io.request
  cpu.io.fetch_response <> fetch_adaptor.io.response
  ram.io.port_1 <> fetch_adaptor.io.membus

  // use another adaptor to connect the CPU's loadstore unit to the RAM's read/write port
  val ldst_adaptor  = new MemoryAdaptor
  cpu.io.ldst_request <> ldst_adaptor.io.request
  cpu.io.ldst_response <> ldst_adaptor.io.response
  ram.io.port_2 <> ldst_adaptor.io.membus

}
