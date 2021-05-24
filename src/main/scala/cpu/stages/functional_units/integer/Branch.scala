package cpu.stages.functional_units.integer

import cpu.interfaces.{ReadInterface, BranchControl}
import cpu.uOps.functional_units.Integer.{BranchArgs}
import cpu.debug.debug_stage1_ifu_branch

import isa.{MnemonicEnums, Forms, ReadSlotPacking}

import spinal.core._
import spinal.lib._


class BIBits extends Bundle {
  val regfield = UInt(2 bits) // Selects which bit in the CR (eq, gt, lt, so) to use
  val cr_bank = UInt(1 bits)  // Selects between CRA and CRB
  val cr_num = UInt(2 bits)   // Selects which cr in CRA or CRB to use
}

class Branch extends Component {
  val io = new Bundle{
    val ri = in(new ReadInterface)

    val lr_w = out(Flow(UInt(64 bits)))
    val ctr_w = out(Flow(UInt(64 bits)))

    val bc = out(new BranchControl)
  }
  io.bc.is_branch := False
  io.bc.branch_taken := False
  io.bc.target_addr := 0
  io.bc.branch_addr := 0

  io.lr_w.valid := False
  io.lr_w.payload := 0
  io.ctr_w.valid := False
  io.ctr_w.payload := 0

  val dec_data = io.ri.dec_data
  val insn = dec_data.insn

  val branchArgs = new BranchArgs
  branchArgs.assignFromBits(dec_data.uOps.args)

  when(branchArgs.conditional =/= True){
    io.bc.is_branch := True
    io.bc.branch_taken := True
    io.bc.branch_addr := dec_data.cia
    // absolute address
    when(Forms.I1.AA(insn) === True){
      io.bc.target_addr := io.ri.imm.payload << 2
    }.otherwise{
      io.bc.target_addr := (io.ri.imm.payload << 2) + dec_data.cia
    }
  } .otherwise {
    // Yep, reverse it because power's bit ordering is weird
    val bo = Reverse(Forms.B1.BO(insn))
    // val bo = Forms.B1.BO(insn)

    val bi = new BIBits
    bi.assignFromBits(Forms.B1.BI(insn).asBits)

    // Grab the cr from the correct read port
    // Similar to above. We're indexing into this so reverse it so the
    // index selects the correct bit
    val banked_cr = UInt(16 bits)
    when(bi.cr_bank === 0){
      // banked_cr := Reverse(io.ri.slots(ReadSlotPacking.CRPort1).data(15, 0))
      banked_cr := io.ri.slots(ReadSlotPacking.CRPort1).data(15 downto 0)
    }.otherwise{
      // banked_cr := Reverse(io.ri.slots(ReadSlotPacking.CRPort2).data(15, 0))
      banked_cr := io.ri.slots(ReadSlotPacking.CRPort2).data(15 downto 0)
    }

    // Generate the index into banked_cr
    // Invert the bits as it indexes backwards due to power's weird bit ordering
    val cr_idx = (~Cat(bi.regfield, bi.cr_num)).asUInt

    // Check the CR condition
    val cond_1 = bo(0)
    val cond_2 = banked_cr(cr_idx) === bo(1)
    val cond_ok = cond_1 | cond_2

    if (debug_stage1_ifu_branch) {
      // printf("Branch Unit:\n")
      // printf(p"\t          B0: 0b${Binary(bo)}\n")
      // printf(p"\t          BI: ${Forms.B1.BI(insn)}\n")
      // when(bi.cr_bank === 0) {
      //   printf(s"\t     CR Slot: ${ReadSlotPacking.CRPort1}\n")
      // } .otherwise {
      //   printf(s"\t     CR Slot: ${ReadSlotPacking.CRPort2}\n")
      // }
      // printf(p"\tCR Bank Sel.: ${bi.cr_bank}\n")
      // printf(p"\t   Banked CR: 0b${Binary(banked_cr)}\n")
      // printf(p"\t     CR Num.: ${bi.cr_num}\n")
      // printf(p"\t CR Regfield: ${bi.regfield}\n")
      // printf(p"\t    CR Index: $cr_idx\n")
      // printf(p"\t     Cond #1: $cond_1\n")
      // printf(p"\t     Cond #2: $cond_2\n")
      // printf(p"\t     Cond OK: $cond_ok\n")
    }

    val ctr = io.ri.slots(ReadSlotPacking.SPRPort1).data(63 downto 0)
    val ctr_ok = bo(2) | ((ctr =/= 0) ^ bo(3))

    when(bo(2) === False){
      io.ctr_w.payload := ctr - 1
      io.ctr_w.valid := True
    }


    io.bc.is_branch := True
    io.bc.branch_taken := cond_ok & ctr_ok
    io.bc.branch_addr := dec_data.cia
    // absolute address
    when(branchArgs.immediate_address){
      when(Forms.B1.AA(insn) === True){
        io.bc.target_addr := io.ri.imm.payload << 2
      }.otherwise{
        io.bc.target_addr := (io.ri.imm.payload << 2) + dec_data.cia
      }
    }.otherwise {
      io.bc.target_addr := io.ri.slots(ReadSlotPacking.SPRPort1).data
    }


  }
  val lk = Forms.I1.LK(insn)

  when(io.bc.branch_taken & (lk === True)){
    io.lr_w.payload := dec_data.cia + 4
    io.lr_w.valid := True
  }

}
