package soc.interfaces

import cpu.shared.memory_state.{TransactionStatus, TransactionType, TransactionSize}

import spinal.core._
import spinal.lib._

class MemBus128() extends Bundle with IMasterSlave{
  // the signal directionality below 
  // is from the viewpoint of a slave
  val ldst_req          = in(TransactionType())
  val byte_address      = in(UInt(64 bits))
  val access_size       = in(TransactionSize)
  val write_mask        = in(Vec(Bool(), 16))
  val write_data        = in(Vec(UInt(8 bits), 16))
  
  val read_data         = out(Vec(UInt(8 bits), 16))
  val status            = out(TransactionStatus())

  override def asMaster() : Unit = {
    out(ldst_req)
    out(byte_address)
    out(access_size)
    out(write_mask)
    out(write_data)

    in(read_data)
    in(status)
  }
}
