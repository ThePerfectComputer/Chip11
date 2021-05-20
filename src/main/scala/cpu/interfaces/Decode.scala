package cpu.interfaces

import cpu.uOps.{uOps}

import spinal.core._
import isa.{MnemonicEnums, FormEnums}



class DecoderData extends FetchOutput {
  // TODO : remove opcode - shouldn't be getting generated
  val opcode = MnemonicEnums()
  val uOps = new uOps
  val form = FormEnums()
  val found_match = Bool()
  val insn_unsupported = Bool()
}

class MultiDecoderData(val stages: Int) extends FetchOutput {
  val data = Seq.fill(stages)(new DecoderData)

  override def clone = new MultiDecoderData(stages)
}
