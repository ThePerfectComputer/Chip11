package cpu.stages.functional_units.integer

import cpu.interfaces.{ReadInterface, BranchControl, DecoderData}
import cpu.uOps.functional_units.Integer.{BranchArgs}
import cpu.debug.debug_stage1_ifu_branch

import isa.{MnemonicEnums, Forms, ReadSlotPacking}

import spinal.core._
import spinal.lib._

class BIBits extends Bundle {
  val regfield = UInt(
    2 bits
  ) // Selects which bit in the CR (eq, gt, lt, so) to use
  val cr_bank = UInt(1 bits) // Selects between CRA and CRB
  val cr_num = UInt(2 bits) // Selects which cr in CRA or CRB to use
}

class Branch extends Component {
  val io = new Bundle {
    val ri = in(new ReadInterface)

    val pipedata = out(new BranchPipeData)
  }


  val dec_data = io.ri.dec_data
  val insn = dec_data.insn

  val branchArgs = new BranchArgs
  branchArgs.assignFromBits(dec_data.uOps.args)

  io.pipedata.conditional := branchArgs.conditional
  val bo = Reverse(Forms.B1.BO(insn))
  // val bo = Forms.B1.BO(insn)

  val bi = new BIBits
  bi.assignFromBits(Forms.B1.BI(insn).asBits)

  // Grab the cr from the correct read port
  // Similar to above. We're indexing into this so reverse it so the
  // index selects the correct bit
  val banked_cr = UInt(16 bits)
  when(bi.cr_bank === 0) {
    // banked_cr := Reverse(io.ri.slots(ReadSlotPacking.CRAPort1).data(15, 0))
    banked_cr := io.ri.slots(ReadSlotPacking.CRAPort1).data(15 downto 0)
  }.otherwise {
    // banked_cr := Reverse(io.ri.slots(ReadSlotPacking.CRBPort1).data(15, 0))
    banked_cr := io.ri.slots(ReadSlotPacking.CRBPort1).data(15 downto 0)
  }
  io.pipedata.cr_bits := banked_cr

  val addr_a = UInt(64 bits)
  val addr_b = UInt(64 bits)

  val addr_o = UInt(33 bits)

  addr_o := addr_a(31 downto 0) +^ addr_b(31 downto 0)
  io.pipedata.branch_addr_low := addr_o(31 downto 0)
  io.pipedata.branch_addr_hi_a := addr_a(63 downto 32)
  io.pipedata.branch_addr_hi_b := addr_b(63 downto 32)
  io.pipedata.branch_addr_carry := addr_o(32)


  val ctr = io.ri.slots(ReadSlotPacking.SPRPort1).data(63 downto 0)
  val ctr_low = UInt(33 bits)
  ctr_low := ctr(31 downto 0) -^ 1
  io.pipedata.ctr_low := ctr_low(31 downto 0)
  io.pipedata.ctr_hi := ctr(63 downto 32)
  io.pipedata.ctr_carry := ctr_low(32)


  val lr_low = UInt(33 bits)
  lr_low := dec_data.cia(31 downto 0) +^ 4
  io.pipedata.lr_low := lr_low(31 downto 0)
  io.pipedata.lr_hi := dec_data.cia(63 downto 32)
  io.pipedata.lr_carry := lr_low(32)


  when(io.pipedata.conditional =/= True) {
    // absolute address
    when(Forms.I1.AA(insn) === True) {
      addr_a := io.ri.imm.payload |<< 2
      addr_b := 0
    }.otherwise {
      addr_a := io.ri.imm.payload |<< 2
      addr_b := dec_data.cia
    }
  }.otherwise {
    // Yep, reverse it because power's bit ordering is weird

    // Generate the index into banked_cr
    // Invert the bits as it indexes backwards due to power's weird bit ordering

    // Check the CR condition


    // absolute address
    when(branchArgs.immediate_address) {
      when(Forms.B1.AA(insn) === True) {
        addr_a := io.ri.imm.payload |<< 2
        addr_b := 0
      }.otherwise {
        addr_a := io.ri.imm.payload |<< 2
        addr_b := dec_data.cia
      }
    }.otherwise {
      addr_a := io.ri.slots(ReadSlotPacking.SPRPort1).data
      addr_b := 0
    }
  }

}

class BranchStage2 extends Component {
  val io = new Bundle {
    val dec_data = in(new DecoderData)
    val pipedata = in(new BranchPipeData)

    val lr_w = out(Flow(UInt(64 bits)))
    val ctr_w = out(Flow(UInt(64 bits)))
    val bc = out(new BranchControl)
  }

  io.lr_w.valid := False
  io.lr_w.payload := 0
  io.ctr_w.valid := False
  io.ctr_w.payload := 0
  val insn = io.dec_data.insn

  val bo = Reverse(Forms.B1.BO(insn))
  // val bo = Forms.B1.BO(insn)

  val bi = new BIBits
  bi.assignFromBits(Forms.B1.BI(insn).asBits)

  val cr_idx = UInt(4 bits)
  cr_idx := (~Cat(bi.cr_num, bi.regfield)).asUInt

  io.bc.is_branch := False
  io.bc.branch_taken := False

  val addr_hi_a = UInt(33 bits)
  val addr_hi_b = UInt(33 bits)
  addr_hi_a := Cat(io.pipedata.branch_addr_hi_a, io.pipedata.branch_addr_carry).asUInt
  addr_hi_b := Cat(io.pipedata.branch_addr_hi_b, io.pipedata.branch_addr_carry).asUInt
  val addr_hi_o = UInt(33 bits)
  addr_hi_o := addr_hi_a + addr_hi_b
  val branch_addr = Cat(addr_hi_o(32 downto 1), io.pipedata.branch_addr_low).asUInt

  io.bc.target_addr := branch_addr
  io.bc.branch_addr := io.dec_data.cia


  val ctr = UInt(64 bits)

  val ctr_hi_a = UInt(33 bits)
  val ctr_hi_b = UInt(33 bits)
  ctr_hi_a := Cat(io.pipedata.ctr_hi, ~io.pipedata.ctr_carry).asUInt
  ctr_hi_b := Cat(~U(0, 32 bits), ~io.pipedata.ctr_carry).asUInt
  val ctr_hi = ctr_hi_a + ctr_hi_b
  ctr := Cat(ctr_hi(32 downto 1), io.pipedata.ctr_low).asUInt

  val ctr_hi_is_zero = Bool
  when(io.pipedata.ctr_carry){
    ctr_hi_is_zero := io.pipedata.ctr_hi === 1
  }.otherwise {
    ctr_hi_is_zero := io.pipedata.ctr_hi === 0

  }
  val ctr_is_zero = Bool
  ctr_is_zero := (io.pipedata.ctr_low === 0) & ctr_hi_is_zero


  val lr = UInt(64 bits)
  val lr_hi = io.pipedata.lr_hi + io.pipedata.lr_carry.asUInt
  lr := Cat(lr_hi, io.pipedata.lr_low).asUInt



  when(io.pipedata.conditional =/= True) {
    io.bc.is_branch := True
    io.bc.branch_taken := True
    // absolute address
  }.otherwise {
    // Yep, reverse it because power's bit ordering is weird

    // Generate the index into banked_cr
    // Invert the bits as it indexes backwards due to power's weird bit ordering

    // Check the CR condition
    val cond_1 = bo(0)
    val cond_2 = io.pipedata.cr_bits(cr_idx) === bo(1)

    val cond_ok = cond_1 | cond_2

    if (debug_stage1_ifu_branch) {
      // printf("Branch Unit:\n")
      // printf(p"\t          B0: 0b${Binary(bo)}\n")
      // printf(p"\t          BI: ${Forms.B1.BI(insn)}\n")
      // when(bi.cr_bank === 0) {
      //   printf(s"\t     CR Slot: ${ReadSlotPacking.CRAPort1}\n")
      // } .otherwise {
      //   printf(s"\t     CR Slot: ${ReadSlotPacking.CRBPort1}\n")
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

    val ctr_ok = bo(2) | (ctr_is_zero ^ bo(3))

    when(bo(2) === False) {
      io.ctr_w.payload := ctr
      io.ctr_w.valid := True
    }

    io.bc.is_branch := True
    io.bc.branch_taken := cond_ok & ctr_ok
    // absolute address
  }
  val lk = Forms.I1.LK(insn)
  when(lk){
    io.lr_w.valid := True
    io.lr_w.payload := lr
  }
}
