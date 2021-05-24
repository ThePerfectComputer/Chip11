package cpu.interfaces

import cpu.interfaces.regfile.Slot
import spinal.core._


class WriteInterface extends Bundle {
  val slots = Vec(
    new Slot(6, 128),
    new Slot(6, 128),
    new Slot(6, 128),
    new Slot(10, 64),
    new Slot(10, 64))

  override def clone = new WriteInterface
}

class WriteStageInterface extends Bundle {
  val write_interface = new WriteInterface
  val cia = UInt(64 bits)
  override def clone = new WriteStageInterface
}
