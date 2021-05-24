package cpu.interfaces

import cpu.shared.memory_state.{TransactionStatus, TransactionType, TransactionSize}

import spinal.core._
import spinal.lib._

class LineRequest() extends Bundle with IMasterSlave{
  val ldst_req        = TransactionType()
  val size            = TransactionSize()
  val data            = UInt(128 bits)
  val byte_address    = UInt(64 bits)

  override def asMaster() : Unit = {
    out(ldst_req)
    out(size)
    out(data)
    out(byte_address)
  }
}

class LineResponse() extends Bundle with IMasterSlave{
  val data              = UInt(128 bits)
  val status            = TransactionStatus()
  val byte_address      = UInt(64 bits)

  override def asMaster() : Unit = {
    out(data)
    out(status)
    out(byte_address)
  }
}
