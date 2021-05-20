package cpu.interfaces

import cpu.shared.memory_state.{TransactionStatus, TransactionType, TransactionSize}

import spinal.core._
import spinal.lib._

class LineRequest() extends Bundle {
  val ldst_req        = TransactionType()
  val size            = TransactionSize()
  val data            = UInt(128 bits)
  val byte_address    = UInt(64 bits)
}

class LineResponse() extends Bundle {
  val data              = UInt(128 bits)
  val status            = TransactionStatus()
  val byte_address      = UInt(64 bits)
}