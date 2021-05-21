package cpu.interfaces

import cpu.interfaces.regfile.Slot
import spinal.core._
import spinal.lib._

class CompareData extends Bundle {
  val activate = Bool()
  val in_slot = UInt(3 bits)
  val out_slot = UInt(3 bits)
}

class ReadInterface extends Bundle {
  val slots = Vec(
    new Slot(6, 128),
    new Slot(6, 128),
    new Slot(6, 128),
    new Slot(10, 64),
    new Slot(10, 64))
  val imm = Flow(UInt(64 bits))
  val compare = new CompareData
  val dec_data = new DecoderData
  // TODO : make this write stage input
  val write_interface = new WriteInterface
  val ldst_request = new LoadStoreRequest
}
