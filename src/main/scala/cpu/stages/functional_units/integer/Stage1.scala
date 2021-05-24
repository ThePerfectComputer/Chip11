package cpu.stages.functional_units.integer

import cpu.config
import cpu.debug.debug_fetch_request
import cpu.interfaces.{ReadInterface, FunctionalUnit, BranchControl}

import cpu.interfaces.regfile.{SourceSelect}
import cpu.shared.memory_state.{TransactionStatus, TransactionType, TransactionSize}
import cpu.uOps.functional_units.Integer.{AdderSelectB, AdderCarryIn, AdderArgs, LogicSelectB, LogicArgs, 
  MultiplierSelectB, MultiplierArgs, ShifterSelectB, ShifterME, ShifterMB, ShifterArgs, 
  ComparatorSelectB, ComparatorArgs}
import cpu.uOps.functional_units.Integer.{IntegerFUSub}
import cpu.uOps.{FunctionalUnit}
import util.{PipeStage}
import isa.{ReadSlotPacking, WriteSlotPacking, SPREnums, MnemonicEnums, Forms}

import spinal.core._
import spinal.lib._


class Stage1 extends PipeStage(new ReadInterface, new FunctionalUnit) {
  val io = new Bundle {
    val bc = out(new BranchControl)
  }

  // initialize output data
  o.write_interface := i.write_interface
  o.dec_data := i.dec_data
  o.ldst_request := i.ldst_request
  o.compare := i.compare

  // if necessary, load up the ldst_request with popuated data from the read stage
  // TODO: determine whether this should be done here, in read, or somewhere else
  for((s, i) <- pipeInput.payload.slots.zipWithIndex) {
    when(pipeInput.payload.ldst_request.store_src_slot === i) {
      pipeOutput.payload.ldst_request.store_data := pipeInput.payload.slots(i).data.resized
    }
  }

  // default tieoffs for branch unit interface
  io.bc.is_branch := False
  io.bc.branch_taken := False
  io.bc.target_addr := 0
  io.bc.branch_addr := 0

  val sub_function = i.dec_data.uOps.sub_function

  // this stage activates when execute.args.functional_unit === Integer
  when(i.dec_data.uOps.functional_unit === FunctionalUnit.INTEGER){

    //if (cpu.debug.debug_stage1) {when (pipeOutput.fire()) {printf("IFU STAGE1:\n")}}

    val sub_unit = IntegerFUSub()
    sub_unit.assignFromBits(sub_function(sub_unit.getBitsWidth-1 downto 0))

    // also need a switch statement here to select particular subfunction
    // to route to
    switch(sub_unit){

      is(IntegerFUSub.Adder){
        if (config.adder) {
          val adderMod = new Adder(64)


          val adderArgs = new AdderArgs
          adderArgs.assignFromBits(i.dec_data.uOps.args)

          adderMod.io.a := i.slots(ReadSlotPacking.GPRPort1).data.resize(64)
          switch(adderArgs.slotB){
            is(AdderSelectB.Slot1) { adderMod.io.b := i.slots(ReadSlotPacking.GPRPort1).data.resize(64)}
            is(AdderSelectB.Slot2) { adderMod.io.b := i.slots(ReadSlotPacking.GPRPort2).data.resize(64)}
            is(AdderSelectB.Slot3) { adderMod.io.b := i.slots(ReadSlotPacking.GPRPort3).data.resize(64)}
            is(AdderSelectB.Imm) { adderMod.io.b := i.imm.payload}
            is(AdderSelectB.ImmShift) { adderMod.io.b := (i.imm.payload |<< 16)}
            is(AdderSelectB.ImmShift2) { adderMod.io.b := (i.imm.payload |<< 2)}
            is(AdderSelectB.ZERO) { adderMod.io.b := 0}
          }
          switch(adderArgs.cIn) {
            is(AdderCarryIn.ZERO) { adderMod.io.carry_in := False}
            is(AdderCarryIn.ONE) { adderMod.io.carry_in := False}
            // TODO Fix
            is(AdderCarryIn.CA) { adderMod.io.carry_in := False}
          }
          adderMod.io.invert_a := adderArgs.invertA

          o.write_interface.slots(WriteSlotPacking.GPRPort1).data := adderMod.io.o.resized
          o.ldst_request.ea := adderMod.io.o

          def debug_adder(){
            // when (pipeOutput.fire()) {
            //   printf(p"\tADDER IO:\n")
            //   printf(p"\t\tInput A: ${adderMod.io.a}\n")
            //   printf(p"\t\tInput B: ${adderMod.io.b}\n")
            //   printf(p"\t\t Output: ${adderMod.io.o}\n")
            // }
          }
          // if (cpu.debug.debug_stage1) {debug_adder()}
        }
      }

      is(IntegerFUSub.Branch){
        if (config.branch) {
          val branchMod = new Branch
          branchMod.io.ri := i

          // def debug_branch(){
          //   when (pipeOutput.fire()) {
          //     printf(p"\tBRANCH IO:\n")
          //     printf(p"\t\tBranch Address: ${Hexadecimal(branchMod.io.bc.branch_addr)}\n")
          //     printf(p"\t\t  Branch Taken: ${branchMod.io.bc.branch_taken}\n")
          //     printf(p"\t\t     Is Branch: ${branchMod.io.bc.is_branch}\n")
          //     printf(p"\t\tTarget Address: ${Hexadecimal(branchMod.io.bc.target_addr)}\n")
          //   }
          // }
          // if (cpu.debug.debug_stage1_ifu_branch) {debug_branch()}

          when(pipeOutput.valid){
            io.bc := branchMod.io.bc
            when (branchMod.io.bc.branch_taken) {
              pipeInput.flush := True
            }
          }
          // TODO :
          // By default we shouldn't write to any registers,
          // should be tied off up top.

          // By default don't write to the SPRs
          o.write_interface.slots(WriteSlotPacking.SPRPort1).sel := SourceSelect.NONE
          o.write_interface.slots(WriteSlotPacking.SPRPort2).sel := SourceSelect.NONE

          // If the branch unit outputs a valid ctr, then write that to ctr SPR
          when(branchMod.io.ctr_w.valid){
            o.write_interface.slots(WriteSlotPacking.SPRPort1).data := branchMod.io.ctr_w.payload
            o.write_interface.slots(WriteSlotPacking.SPRPort1).sel := SourceSelect.SPR
            o.write_interface.slots(WriteSlotPacking.SPRPort1).idx := SPREnums.CTR.asBits.asUInt
          }
          // Same with LR
          when(branchMod.io.lr_w.valid){
            o.write_interface.slots(WriteSlotPacking.SPRPort2).data := branchMod.io.lr_w.payload
            o.write_interface.slots(WriteSlotPacking.SPRPort2).sel := SourceSelect.SPR
            o.write_interface.slots(WriteSlotPacking.SPRPort2).idx := SPREnums.LR.asBits.asUInt
          }
        }
      }

      is(IntegerFUSub.LogicUnit){
        if(config.logical) {
          val logicMod = new LogicUnit(64)

          // def debug_logic(){
          //   when (pipeOutput.fire()) {
          //     printf("\tLOGIC IO:\n")
          //     printf(p"\t\tInput A: ${logicMod.io.a}\n")
          //     printf(p"\t\tInput B: ${logicMod.io.b}\n")
          //     printf(p"\t\t Output: ${logicMod.io.o}\n")
          //   }
          // }
          // if (cpu.debug.debug_stage1) {debug_logic()}

          val logicArgs = new LogicArgs
          logicArgs.assignFromBits(i.dec_data.uOps.args)

          logicMod.io.a := i.slots(ReadSlotPacking.GPRPort1).data.resize(64)
          switch(logicArgs.slotB){
            is(LogicSelectB.Slot1) { logicMod.io.b := i.slots(ReadSlotPacking.GPRPort1).data.resize(64)}
            is(LogicSelectB.Slot2) { logicMod.io.b := i.slots(ReadSlotPacking.GPRPort2).data.resize(64)}
            is(LogicSelectB.Slot3) { logicMod.io.b := i.slots(ReadSlotPacking.GPRPort3).data.resize(64)}
            is(LogicSelectB.Imm) { logicMod.io.b := i.imm.payload}
            is(LogicSelectB.ImmShift) { logicMod.io.b := (i.imm.payload |<< 16)}
          }

          logicMod.io.invert_a := logicArgs.invertA
          logicMod.io.invert_b := logicArgs.invertB
          logicMod.io.invert_o := logicArgs.invertO
          logicMod.io.xor := logicArgs.xor

          o.write_interface.slots(WriteSlotPacking.GPRPort1).data := logicMod.io.o.resized
        }
      }

      // is(IntegerFUSub.Multiplier){
      //   if (config.multiplier) {
      //     val multiplierMod = Module(new Multiplier(64))
      //     multiplierMod.io.a := 0.U
      //     multiplierMod.io.b := 0.U
      //     multiplierMod.io.is_div := false.B
      //     multiplierMod.io.is_unsigned := false.B
      //     multiplierMod.io.word_operands := false.B
      //     multiplierMod.io.output_high := false.B
      //     multiplierMod.io.output_word := false.B
      //     multiplierMod.io.shift_a := false.B

      //     def debug_multiplier(){
      //       when (pipeOutput.fire()) {
      //         printf(p"\tMULT. IO: ${multiplierMod.io}\n")
      //       }
      //     }
      //     if (cpu.debug.debug_stage1) {debug_multiplier()}

      //     val multiplierArgs = Wire(new MultiplierArgs)
      //     multiplierArgs := i.dec_data.uOps.args.asTypeOf(new MultiplierArgs)

      //     multiplierMod.io.a := i.slots(ReadSlotPacking.GPRPort1).data
      //     switch(multiplierArgs.slotB){
      //       is(MultiplierSelectB.Slot1) { multiplierMod.io.b := i.slots(ReadSlotPacking.GPRPort1).data}
      //       is(MultiplierSelectB.Slot2) { multiplierMod.io.b := i.slots(ReadSlotPacking.GPRPort2).data}
      //       is(MultiplierSelectB.Slot3) { multiplierMod.io.b := i.slots(ReadSlotPacking.GPRPort3).data}
      //       is(MultiplierSelectB.Imm) { multiplierMod.io.b := i.imm.bits}
      //     }

      //     multiplierMod.io.is_div := multiplierArgs.is_div
      //     multiplierMod.io.is_unsigned := multiplierArgs.is_unsigned
      //     multiplierMod.io.word_operands := multiplierArgs.word_operands
      //     multiplierMod.io.output_high := multiplierArgs.output_high
      //     multiplierMod.io.output_word := multiplierArgs.output_word
      //     multiplierMod.io.shift_a := multiplierArgs.shift_a

      //     o.write_interface.slots(WriteSlotPacking.GPRPort1).data := multiplierMod.io.o
      //   }
      // }

      // is(IntegerFUSub.Shifter){
      //   if (config.shifter) {
      //     val shifterMod = Module(new Shifter(64))
      //     shifterMod.io.rs := 0.U
      //     shifterMod.io.rb := 0.U
      //     shifterMod.io.ra := 0.U
      //     shifterMod.io.me := 0.U
      //     shifterMod.io.mb := 0.U
      //     shifterMod.io.keep_source := false.B
      //     shifterMod.io.word_op := false.B
      //     shifterMod.io.left := false.B
      //     shifterMod.io.is_arithmetic := false.B
      //     shifterMod.io.is_shift := false.B
      //     shifterMod.io.byte_op := false.B

      //     // def debug_shifter(){
      //     //   when (pipeOutput.fire()) {
      //     //     printf(p"\tSHIFTER IO: ${shifterMod.io}\n")
      //     //   }
      //     // }
      //     // if (cpu.debug.debug_stage1) {debug_shifter()}

      //     val shifterArgs = new ShifterArgs
      //     shifterArgs.assignFromBits(i.dec_data.uOps.args)
        
      //     shifterMod.io.rs := i.slots(ReadSlotPacking.GPRPort1).data.resized(64)
      //     switch(shifterArgs.slotB){
      //       is(ShifterSelectB.Slot1) { shifterMod.io.rb := i.slots(ReadSlotPacking.GPRPort1).data.resized}
      //       is(ShifterSelectB.Slot2) { shifterMod.io.rb := i.slots(ReadSlotPacking.GPRPort2).data.resized}
      //       is(ShifterSelectB.Slot3) { shifterMod.io.rb := i.slots(ReadSlotPacking.GPRPort3).data.resized}
      //       is(ShifterSelectB.Imm) { shifterMod.io.rb := i.imm.bits}
      //       is(ShifterSelectB.ZERO) { shifterMod.io.rb := 0}
      //     }
      //     switch(shifterArgs.me){
      //       is(ShifterME.LSB) { shifterMod.io.me := 63}
      //       // WTF is this? I think this is supposed to be Forms.MD(whatever).me(i.insn)
      //       is(ShifterME.ME) { shifterMod.io.me := i.slots(3).data}
      //       is(ShifterME.IMM_REV) { shifterMod.io.me := (63.U - i.imm.bits)}
      //       is(ShifterME.WORD) { shifterMod.io.me := 31.U}
      //     }
      //     switch(shifterArgs.mb){
      //       is(ShifterMB.MSB) { shifterMod.io.me := 0.U}
      //       // WTF is this? I think this is supposed to be Forms.MD(whatever).mb(i.insn)
      //       is(ShifterMB.MB) { shifterMod.io.me := i.slots(4).data}
      //     }
      //     // Huh?
      //     shifterMod.io.ra := i.slots(2).data
      //     shifterMod.io.left := shifterArgs.left
      //     shifterMod.io.keep_source := shifterArgs.keep_source
      //     shifterMod.io.is_shift := shifterArgs.is_shift
      //     shifterMod.io.is_arithmetic := shifterArgs.is_arithmetic
      //     shifterMod.io.byte_op := shifterArgs.byte_op

      //     o.write_interface.slots(WriteSlotPacking.GPRPort1).data := shifterMod.io.o
      //   }
      // }

      is(IntegerFUSub.Comparator){
        if (config.comparator) {
          val comparatorMod = new Comparator(64)
          // def debug_comparator(){
          //   when (pipeOutput.fire()) {
          //     printf(p"\tCOMP. IO: ${comparatorMod.io}\n")
          //   }
          // }
          // if (cpu.debug.debug_stage1) {debug_comparator()}
          val comparatorArgs = new ComparatorArgs
          comparatorArgs.assignFromBits(i.dec_data.uOps.args)

          comparatorMod.io.a := i.slots(0).data.resize(64)
          switch(comparatorArgs.slotB){
            is(ComparatorSelectB.Slot1) { comparatorMod.io.b := i.slots(0).data.resize(64)}
            is(ComparatorSelectB.Slot2) { comparatorMod.io.b := i.slots(1).data.resize(64)}
            is(ComparatorSelectB.Slot3) { comparatorMod.io.b:= i.slots(2).data.resize(64)}
            is(ComparatorSelectB.Imm)   { comparatorMod.io.b := i.imm.payload}
          }


          val l = Forms.D1.L(i.dec_data.insn)
          comparatorMod.io.is_64b := l
          comparatorMod.io.logical := comparatorArgs.logical
          val bf = Forms.D1.BF(i.dec_data.insn)
          val field_select = 3 - bf(2 downto 1)

          val cr_fields = Vec(UInt(4 bits), 4)

          val xer = i.slots(ReadSlotPacking.SPRPort1).data
          val xer_so = xer(31)
          cr_fields := cr_fields.getZero
          cr_fields(field_select) := Cat(comparatorMod.io.o, xer_so).asUInt

          when(bf(0) === False) {
            o.write_interface.slots(WriteSlotPacking.CRPort1).data := cr_fields.asBits.asUInt.resized
          }.otherwise {
            o.write_interface.slots(WriteSlotPacking.CRPort2).data := cr_fields.asBits.asUInt.resized
          }
        }
      }


      is(IntegerFUSub.Move){
        if (config.adder) {
          o.write_interface.slots(0).data := 0
          o.ldst_request.ea := 0

          when(i.dec_data.opcode === MnemonicEnums.mtspr) {
            o.write_interface.slots(WriteSlotPacking.SPRPort1).data := 
              i.slots(ReadSlotPacking.GPRPort1).data.resize(64)
          }
          when(i.dec_data.opcode === MnemonicEnums.mfspr) {
            o.write_interface.slots(WriteSlotPacking.GPRPort1).data := 
              i.slots(ReadSlotPacking.SPRPort1).data.resized
          }
          when(i.dec_data.opcode === MnemonicEnums.mtcrf || i.dec_data.opcode === MnemonicEnums.mtocrf) {
            val fxm = Forms.XFX3.FXM(i.dec_data.insn)
            val maska = Cat(fxm(7), fxm(5), fxm(3), fxm(1))
            val maskb = Cat(fxm(6), fxm(4), fxm(2), fxm(0))
            val datain = i.slots(ReadSlotPacking.GPRPort1).data(31 downto 0)
            when(maska =/= 0){
              val dataa = Cat(datain(31 downto 28), datain(23 downto 20), datain(15 downto 12), datain(7 downto 4))
              o.write_interface.slots(WriteSlotPacking.CRPort1).data := dataa.asUInt.resized
            }
            when(maskb =/= 0){
              val datab = Cat(datain(27 downto 24), datain(19 downto 16), datain(11 downto 8), datain(3 downto 0))
              o.write_interface.slots(WriteSlotPacking.CRPort2).data := datab.asUInt.resized
            }
          }
          when(i.dec_data.opcode === MnemonicEnums.mfcr) {
            val cra = i.slots(ReadSlotPacking.CRPort1).data
            val crb = i.slots(ReadSlotPacking.CRPort2).data
            val dataout = Vec(UInt(4 bits), 8)
            dataout(7) := cra(15 downto 12)
            dataout(5) := cra(11 downto 8)
            dataout(3) := cra(7 downto 4)
            dataout(1) := cra(3 downto 0)
            dataout(6) := crb(15 downto 12)
            dataout(4) := crb(11 downto 8)
            dataout(2) := crb(7 downto 4)
            dataout(0) := crb(3 downto 0)
            o.write_interface.slots(WriteSlotPacking.GPRPort1).data := dataout.asBits.asUInt.resized
          }
          when(i.dec_data.opcode === MnemonicEnums.mfocrf) {
            val cra = i.slots(ReadSlotPacking.CRPort1).data
            val crb = i.slots(ReadSlotPacking.CRPort2).data
            val dataout = Vec(UInt(4 bits), 8)
            val fxm = Forms.XFX6.FXM(i.dec_data.insn)
            dataout(7) := cra(15 downto 12)
            dataout(5) := cra(11 downto 8)
            dataout(3) := cra(7 downto 4)
            dataout(1) := cra(3 downto 0)
            dataout(6) := crb(15 downto 12)
            dataout(4) := crb(11 downto 8)
            dataout(2) := crb(7 downto 4)
            dataout(0) := crb(3 downto 0)
            val datamask = Vec(UInt(4 bits), 8)
            for(i <- 0 until 8){
              datamask(i) := 0
              when(fxm(i)){
                datamask(i) := 0xF
              }
            }
            val data_masked = dataout.asBits.asUInt & datamask.asBits.asUInt
            o.write_interface.slots(WriteSlotPacking.GPRPort1).data := data_masked.resized
          }


        }
      }
    }
  }
}
