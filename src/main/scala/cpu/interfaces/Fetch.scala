package cpu.interfaces

import spinal.core._

class FetchOutput extends Bundle {
  val insn = UInt(32 bits)
  val cia = UInt(64 bits)
}
