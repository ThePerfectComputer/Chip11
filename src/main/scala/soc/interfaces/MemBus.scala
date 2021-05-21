package soc.interfaces

import cpu.shared.memory_state.{TransactionStatus, TransactionType}

import spinal.core._
import spinal.lib._

class MemBus128() extends Bundle {
  // the signal directionality below 
  // is from the viewpoint of a slave
  val ldst_req          = in(TransactionType())
  val quad_word_address = in(UInt(60 bits))
  val write_mask        = in(Vec(Bool(), 16))
  val write_data        = in(Vec(UInt(8 bits), 16))
  
  val read_data         = out(Vec(UInt(8 bits), 16))
  val status            = out(TransactionStatus())
}