package cpu.interfaces

import cpu.interfaces.regfile.Slot
import spinal.core._
import spinal.lib._


class WriteInterface extends Bundle {
  val slots = Seq(
    new Slot(6, 128),
    new Slot(6, 128),
    new Slot(6, 128),
    new Slot(10, 64),
    new Slot(10, 64))
  override def clone = new ReadInterface
}

class WriteStageInterface extends Bundle {
  val write_interface = (new WriteInterface)
}
