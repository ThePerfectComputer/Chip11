package cpu.interfaces

import cpu.shared.memory_state.{TransactionType, TransactionSize}

import spinal.core._
import spinal.lib._

class LoadStoreRequest extends Bundle {
  val ea = UInt(64 bits)
  val req_type = TransactionType()
  val size = TransactionSize()
  // TODO figure out if the index must support the last
  //      two slots, which have 10 index bits
  val store_src_slot = UInt(3 bits)
  val store_data = UInt(128 bits)
  val load_dest_slot = UInt(3 bits)

}
