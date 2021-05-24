package cpu.interfaces

import spinal.core._

class FunctionalUnit extends Bundle {
  val dec_data        = new DecoderData
  val ldst_request    = new LoadStoreRequest
  val write_interface = new WriteInterface
  val compare         = new CompareData
}

class FunctionalUnitExit extends Bundle {
  val ldst_request    = new LoadStoreRequest
  val write_interface = new WriteInterface
}
