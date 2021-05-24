package cpu.interfaces

import spinal.core._
import spinal.lib._

class BranchControl extends Bundle {
  val is_branch = Bool()   // Is this a branch instruction
  val branch_taken = Bool()    // Did we take the branch
  val target_addr = UInt(64 bits)  // The address to jump to
  val branch_addr = UInt(64 bits)  // The address of the taken branch
}

class BPFetchRequestInterface extends Bundle with IMasterSlave {
  // Branch predictor controlling the fetch unit
  val branch = Flow(UInt(64 bits))

  override def asMaster() : Unit = {
    out(branch)
  }
}

class BPFetchResponseInterface extends Bundle with IMasterSlave{
  // Instruction data from fetch unit, should decide whether to branch
  val fetch_info = Flow(new Bundle {
    val insn = UInt(32 bits)
    val cia = UInt(64 bits)
  })
  override def asMaster() : Unit = {
    out(fetch_info)
  }
}
