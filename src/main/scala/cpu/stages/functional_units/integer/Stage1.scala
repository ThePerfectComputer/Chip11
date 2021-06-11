package cpu.stages.functional_units.integer

import cpu.{CPUConfig}
import cpu.debug.debug_fetch_request
import cpu.interfaces.{ReadInterface, FunctionalUnit, BranchControl}

import isa.{SourceSelect}
import cpu.shared.memory_state.{
  TransactionStatus,
  TransactionType,
  TransactionSize
}
import cpu.shared.{XERBits, XERMask}
import cpu.uOps.functional_units.Integer.{
  AdderSelectB,
  AdderCarryIn,
  AdderArgs,
  LogicSelectB,
  LogicArgs,
  MultiplierSelectB,
  MultiplierArgs,
  DividerArgs,
  ShifterSelectB,
  ShifterME,
  ShifterMB,
  ShifterArgs,
  ComparatorSelectB,
  ComparatorArgs,
  ZCntArgs,
  ZCntDirection,
  ZCntSize,
  PopcntArgs,
  PopcntSize
}
import cpu.uOps.functional_units.Integer.{IntegerFUSub}
import cpu.uOps.{FunctionalUnit}
import util.{PipeStage}
import isa.{ReadSlotPacking, WriteSlotPacking, SPREnums, MnemonicEnums, Forms}

import spinal.core._
import spinal.lib._

class Stage1(implicit config: CPUConfig)
    extends PipeStage(new ReadInterface, new ExecuteData) {
  val so_bit = i.slots(ReadSlotPacking.XERPort1).data(XERBits.SO)
  o.so_bit := so_bit

  // initialize output data
  o.write_interface := i.write_interface
  o.dec_data := i.dec_data
  o.ldst_request := i.ldst_request
  o.compare := i.compare
  o.additionalData := 0

  // if necessary, load up the ldst_request with popuated data from the read stage
  // TODO: determine whether this should be done here, in read, or somewhere else
  for ((s, i) <- pipeInput.payload.slots.zipWithIndex) {
    when(pipeInput.payload.ldst_request.store_src_slot === i) {
      pipeOutput.payload.ldst_request.store_data := pipeInput.payload
        .slots(i)
        .data
        .resized
    }
  }


  val request_submitted = RegInit(False)
  val sub_function = i.dec_data.uOps.sub_function

  val sub_unit = IntegerFUSub().keep()
  sub_unit.assignFromBits(sub_function(sub_unit.getBitsWidth - 1 downto 0))
  // this stage activates when execute.args.functional_unit === Integer
  when(i.dec_data.uOps.functional_unit === FunctionalUnit.INTEGER) {

    //if (cpu.debug.debug_stage1) {when (pipeOutput.fire()) {printf("IFU STAGE1:\n")}}

    // also need a switch statement here to select particular subfunction
    // to route to
    switch(sub_unit) {

      is(IntegerFUSub.Adder) {
        if (config.adder) {
          val adderMod = new Adder(32)

          val adderData = new AdderPipeData

          val adderArgs = new AdderArgs
          adderArgs.assignFromBits(i.dec_data.uOps.args)

          val addA = UInt(64 bits)
          val addB = UInt(64 bits)

          adderMod.io.a := addA(31 downto 0)
          adderMod.io.b := addB(31 downto 0)

          addA := i.slots(ReadSlotPacking.GPRPort1).data.resize(64)
          switch(adderArgs.slotB) {
            is(AdderSelectB.Slot1) {
              addB := i.slots(ReadSlotPacking.GPRPort1).data.resize(64)
            }
            is(AdderSelectB.Slot2) {
              addB := i.slots(ReadSlotPacking.GPRPort2).data.resize(64)
            }
            is(AdderSelectB.Slot3) {
              addB := i.slots(ReadSlotPacking.GPRPort3).data.resize(64)
            }
            is(AdderSelectB.Imm) { addB := i.imm.payload }
            is(AdderSelectB.ImmShift) {
              addB := (i.imm.payload |<< 16)
            }
            is(AdderSelectB.ImmShift2) {
              addB := (i.imm.payload |<< 2)
            }
            is(AdderSelectB.ZERO) { addB := 0 }
            is(AdderSelectB.NEGATIVE_ONE) { addB := ~U(0, 64 bits) }
          }
          switch(adderArgs.cIn) {
            is(AdderCarryIn.ZERO) { adderMod.io.carry_in := False }
            is(AdderCarryIn.ONE) { adderMod.io.carry_in := True }
            // TODO Fix
            is(AdderCarryIn.CA) {
              adderMod.io.carry_in := i
                .slots(ReadSlotPacking.XERPort1)
                .data(XERBits.CA)
            }
          }
          adderMod.io.invert_a := adderArgs.invertA

          adderData.upperA := addA(63 downto 32)
          adderData.upperB := addB(63 downto 32)
          adderData.carry_32 := adderMod.io.carry_out
          adderData.overflow_32 := adderMod.io.overflow_out
          adderData.invert_a := adderArgs.invertA

          o.additionalData(adderData.getBitsWidth - 1 downto 0)
            .assignFromBits(adderData.asBits)

          o.write_interface
            .slots(WriteSlotPacking.GPRPort1)
            .data := adderMod.io.o.resized
          // o.write_interface
          //   .slots(WriteSlotPacking.XERPort1)
          //   .data(XERBits.CA) := adderMod.io.carry_out
          o.write_interface
            .slots(WriteSlotPacking.XERPort1)
            .data(XERBits.CA32) := adderMod.io.carry_out
          // o.write_interface
          //   .slots(WriteSlotPacking.XERPort1)
          //   .data(XERBits.OV) := adderMod.io.overflow_out
          o.write_interface
            .slots(WriteSlotPacking.XERPort1)
            .data(XERBits.OV32) := adderMod.io.overflow_out
          o.ldst_request.ea := adderMod.io.o.resized

          // SO handling
          // o.write_interface
          //   .slots(WriteSlotPacking.XERPort1)
          //   .data(XERBits.SO) := adderMod.io.overflow_out
          // when(!adderMod.io.overflow_out){
          //   o.write_interface.slots(WriteSlotPacking.XERPort1).idx :=
          //     i.write_interface.slots(WriteSlotPacking.XERPort1).idx & ((~XERMask.SO) & XERMask.ALL)
          // }

          def debug_adder() {
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

      is(IntegerFUSub.Branch) {
        if (config.branch) {
          val branchMod = new Branch


          val branchData = new BranchPipeData
          branchData := branchMod.io.pipedata
          branchMod.io.ri := i


          o.additionalData(branchData.getBitsWidth - 1 downto 0)
            .assignFromBits(branchData.asBits)

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

        }
      }

      is(IntegerFUSub.LogicUnit) {
        if (config.logical) {
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
          switch(logicArgs.slotB) {
            is(LogicSelectB.Slot1) {
              logicMod.io.b := i.slots(ReadSlotPacking.GPRPort1).data.resize(64)
            }
            is(LogicSelectB.Slot2) {
              logicMod.io.b := i.slots(ReadSlotPacking.GPRPort2).data.resize(64)
            }
            is(LogicSelectB.Slot3) {
              logicMod.io.b := i.slots(ReadSlotPacking.GPRPort3).data.resize(64)
            }
            is(LogicSelectB.Imm) { logicMod.io.b := i.imm.payload }
            is(LogicSelectB.ImmShift) {
              logicMod.io.b := (i.imm.payload |<< 16)
            }
          }

          logicMod.io.invert_a := logicArgs.invertA
          logicMod.io.invert_b := logicArgs.invertB
          logicMod.io.invert_o := logicArgs.invertO
          logicMod.io.xor := logicArgs.xor

          o.write_interface
            .slots(WriteSlotPacking.GPRPort1)
            .data := logicMod.io.o.resized
        }
      }
      is(IntegerFUSub.Divider){
        if(config.multiplier){
          val div = new Divider(64)
          val dividerArgs = new DividerArgs

          dividerArgs.assignFromBits(i.dec_data.uOps.args)


          div.io.a := i.slots(ReadSlotPacking.GPRPort1).data.resized
          // All divisions take from slot 2
          div.io.b := i.slots(ReadSlotPacking.GPRPort2).data.resized

          div.io.is_unsigned := dividerArgs.is_unsigned
          div.io.is_word := dividerArgs.is_word
          div.io.shift_a := dividerArgs.shift_a
          div.io.input_valid := False

          o.write_interface.slots(WriteSlotPacking.GPRPort1).data := div.io.o.resized


          div.io.output_ack := False
          when(pipeInput.valid){
            when(request_submitted){
              ready := div.io.output_valid
              div.io.output_ack := pipeOutput.ready
            }.otherwise{
              ready := False
              div.io.input_valid := pipeInput.valid & (sub_unit === IntegerFUSub.Divider)
            }

            when(div.io.input_valid & div.io.input_ready){
              request_submitted := True
            }
            when(div.io.output_valid & div.io.output_ack){
              request_submitted := False
            }
          }
        }
      }

      is(IntegerFUSub.Multiplier){
        if (config.multiplier) {
          val multiplierMod = new Multiplier(64)

          // def debug_multiplier(){
          //   when (pipeOutput.fire()) {
          //     printf(p"\tMULT. IO: ${multiplierMod.io}\n")
          //   }
          // }
          // if (cpu.debug.debug_stage1) {debug_multiplier()}

          val multiplierArgs = new MultiplierArgs
          multiplierArgs.assignFromBits(i.dec_data.uOps.args)

          multiplierMod.io.a := i.slots(ReadSlotPacking.GPRPort1).data.resized
          switch(multiplierArgs.slotB){
            is(MultiplierSelectB.Slot1) { multiplierMod.io.b := i.slots(ReadSlotPacking.GPRPort1).data.resized}
            is(MultiplierSelectB.Slot2) { multiplierMod.io.b := i.slots(ReadSlotPacking.GPRPort2).data.resized}
            is(MultiplierSelectB.Slot3) { multiplierMod.io.b := i.slots(ReadSlotPacking.GPRPort3).data.resized}
            is(MultiplierSelectB.Imm) { multiplierMod.io.b := i.imm.payload.resized}
          }

          multiplierMod.io.is_div := multiplierArgs.is_div
          multiplierMod.io.is_unsigned := multiplierArgs.is_unsigned
          multiplierMod.io.word_operands := multiplierArgs.word_operands
          multiplierMod.io.output_high := multiplierArgs.output_high
          multiplierMod.io.output_word := multiplierArgs.output_word
          multiplierMod.io.shift_a := multiplierArgs.shift_a

          o.write_interface.slots(WriteSlotPacking.GPRPort1).data := multiplierMod.io.o.resized
        }
      }

      is(IntegerFUSub.Shifter) {
        if (config.shifter) {
          val shifterMod = new Shifter(64)
          val shifterData = new ShifterPipeData
          shifterData := shifterMod.io.pipedata

          o.additionalData(shifterData.getBitsWidth - 1 downto 0)
            .assignFromBits(shifterData.asBits)

          // def debug_shifter(){
          //   when (pipeOutput.fire()) {
          //     printf(p"\tSHIFTER IO: ${shifterMod.io}\n")
          //   }
          // }
          // if (cpu.debug.debug_stage1) {debug_shifter()}

          val shifterArgs = new ShifterArgs
          shifterArgs.assignFromBits(i.dec_data.uOps.args)

          shifterMod.io.rs := i.slots(ReadSlotPacking.GPRPort1).data.resized
          switch(shifterArgs.slotB) {
            is(ShifterSelectB.Slot1) {
              shifterMod.io.rb := i.slots(ReadSlotPacking.GPRPort1).data.resized
            }
            is(ShifterSelectB.Slot2) {
              shifterMod.io.rb := i.slots(ReadSlotPacking.GPRPort2).data.resized
            }
            is(ShifterSelectB.Slot3) {
              shifterMod.io.rb := i.slots(ReadSlotPacking.GPRPort3).data.resized
            }
            is(ShifterSelectB.Imm) { shifterMod.io.rb := i.imm.payload }
            is(ShifterSelectB.ZERO) { shifterMod.io.rb := 0 }
          }
          switch(shifterArgs.me) {
            is(ShifterME.LSB) { shifterMod.io.me := 63 }
            is(ShifterME.LSB_32) { shifterMod.io.me := 31 }
            is(ShifterME.ME) {
              val me = Forms.MD2.me(i.dec_data.insn)
              shifterMod.io.me := Cat(me(0), me(5 downto 1)).asUInt
            }
            is(ShifterME.ME_32) {
              shifterMod.io.me := Forms.M2.ME(i.dec_data.insn).resized
            }
            is(ShifterME.IMM_REV) {
              shifterMod.io.me := (63 - i.imm.payload(5 downto 0))
            }
            is(ShifterME.WORD) { shifterMod.io.me := 31 }
          }
          val argsMb = ShifterMB()
          argsMb.assignFromBits(shifterArgs.mb.asBits)
          switch(argsMb) {
            is(ShifterMB.MSB) { shifterMod.io.mb := 0 }
            is(ShifterMB.MB) {
              val mb = Forms.MDS1.mb(i.dec_data.insn)
              shifterMod.io.mb := Cat(mb(0), mb(5 downto 1)).asUInt
            }
            is(ShifterMB.MB_32) {
              shifterMod.io.mb := Forms.M2.MB(i.dec_data.insn).resized
            }
          }
          shifterMod.io.ra := i
            .slots(ReadSlotPacking.GPRPort3)
            .data(63 downto 0)
          shifterMod.io.left := shifterArgs.left
          shifterMod.io.keep_source := shifterArgs.keep_source
          shifterMod.io.is_shift := shifterArgs.is_shift
          shifterMod.io.is_arithmetic := shifterArgs.is_arithmetic
          shifterMod.io.byte_op := shifterArgs.byte_op
          shifterMod.io.word_op := shifterArgs.word_op
        }
      }

      is(IntegerFUSub.Comparator) {
        if (config.comparator) {
          val comparatorMod = new Comparator(64)

          val comparatorData = new ComparatorPipeData
          comparatorData := comparatorMod.io.pipedata
          o.additionalData(comparatorData.getBitsWidth - 1 downto 0)
            .assignFromBits(comparatorData.asBits)
          // def debug_comparator(){
          //   when (pipeOutput.fire()) {
          //     printf(p"\tCOMP. IO: ${comparatorMod.io}\n")
          //   }
          // }
          // if (cpu.debug.debug_stage1) {debug_comparator()}
          val comparatorArgs = new ComparatorArgs
          comparatorArgs.assignFromBits(i.dec_data.uOps.args)

          comparatorMod.io.a := i.slots(0).data.resize(64)
          switch(comparatorArgs.slotB) {
            is(ComparatorSelectB.Slot1) {
              comparatorMod.io.b := i.slots(0).data.resize(64)
            }
            is(ComparatorSelectB.Slot2) {
              comparatorMod.io.b := i.slots(1).data.resize(64)
            }
            is(ComparatorSelectB.Slot3) {
              comparatorMod.io.b := i.slots(2).data.resize(64)
            }
            is(ComparatorSelectB.Imm) { comparatorMod.io.b := i.imm.payload }
          }

          val l = Forms.D1.L(i.dec_data.insn)
          comparatorMod.io.is_64b := l
          comparatorMod.io.logical := comparatorArgs.logical
          comparatorMod.io.so_bit := so_bit
        }
      }

      is(IntegerFUSub.ZCnt) {
        if (config.zcnt) {
          val zCntMod = new ZCnt
          // def debug_comparator(){
          //   when (pipeOutput.fire()) {
          //     printf(p"\tCOMP. IO: ${comparatorMod.io}\n")
          //   }
          // }
          // if (cpu.debug.debug_stage1) {debug_comparator()}
          val zCntArgs = new ZCntArgs
          zCntArgs.assignFromBits(i.dec_data.uOps.args)

          zCntMod.io.data := i.slots(ReadSlotPacking.GPRPort1).data(63 downto 0)
          switch(zCntArgs.direction) {
            is(ZCntDirection.LEADING) { zCntMod.io.countLeadingZeros := True }
            is(ZCntDirection.TRAILING) { zCntMod.io.countLeadingZeros := False }
          }
          switch(zCntArgs.size) {
            is(ZCntSize.DWORD) { zCntMod.io.isWord := False }
            is(ZCntSize.WORD) { zCntMod.io.isWord := True }
          }
          o.write_interface
            .slots(WriteSlotPacking.GPRPort1)
            .data := zCntMod.io.count.resized

        }
      }

      is(IntegerFUSub.Popcnt) {
        if (config.popcnt) {
          val popcntMod = new PopcntStage1
          // def debug_comparator(){
          //   when (pipeOutput.fire()) {
          //     printf(p"\tCOMP. IO: ${comparatorMod.io}\n")
          //   }
          // }
          // if (cpu.debug.debug_stage1) {debug_comparator()}
          val popcntArgs = new PopcntArgs
          popcntArgs.assignFromBits(i.dec_data.uOps.args)

          popcntMod.io.data := i
            .slots(ReadSlotPacking.GPRPort1)
            .data(63 downto 0)
            .asBits
            .resized

          o.write_interface
            .slots(WriteSlotPacking.GPRPort1)
            .data := popcntMod.io.count8.resized
        }
      }

      is(IntegerFUSub.Move) {
        if (true) {
          // o.write_interface.slots(0).data := 0
          o.ldst_request.ea := 0

          switch(i.dec_data.opcode) {
            is(MnemonicEnums.mtspr) {
              o.write_interface.slots(WriteSlotPacking.SPRPort1).data :=
                i.slots(ReadSlotPacking.GPRPort1).data.resize(64)
            }
            is(MnemonicEnums.mfspr) {
              o.write_interface.slots(WriteSlotPacking.GPRPort1).data :=
                i.slots(ReadSlotPacking.SPRPort1).data.resized
            }
            is(MnemonicEnums.mfxer) {
              o.write_interface.slots(WriteSlotPacking.GPRPort1).data :=
                i.slots(ReadSlotPacking.XERPort1).data.resized
            }
            is(MnemonicEnums.mtxer) {
              o.write_interface.slots(WriteSlotPacking.XERPort1).data :=
                i.slots(ReadSlotPacking.GPRPort1).data.resize(64)
            }
            is(MnemonicEnums.mtcrf, MnemonicEnums.mtocrf) {
              val fxm = Forms.XFX3.FXM(i.dec_data.insn)
              val maska = Cat(fxm(7), fxm(5), fxm(3), fxm(1))
              val maskb = Cat(fxm(6), fxm(4), fxm(2), fxm(0))
              val datain = i.slots(ReadSlotPacking.GPRPort1).data(31 downto 0)
              when(maska =/= 0) {
                val dataa = Cat(
                  datain(31 downto 28),
                  datain(23 downto 20),
                  datain(15 downto 12),
                  datain(7 downto 4)
                )
                o.write_interface
                  .slots(WriteSlotPacking.CRAPort1)
                  .data := dataa.asUInt.resized
              }
              when(maskb =/= 0) {
                val datab = Cat(
                  datain(27 downto 24),
                  datain(19 downto 16),
                  datain(11 downto 8),
                  datain(3 downto 0)
                )
                o.write_interface
                  .slots(WriteSlotPacking.CRBPort1)
                  .data := datab.asUInt.resized
              }
            }
            is(MnemonicEnums.mfcr) {
              val cra = i.slots(ReadSlotPacking.CRAPort1).data
              val crb = i.slots(ReadSlotPacking.CRBPort1).data
              val dataout = Vec(UInt(4 bits), 8)
              dataout(7) := cra(15 downto 12)
              dataout(5) := cra(11 downto 8)
              dataout(3) := cra(7 downto 4)
              dataout(1) := cra(3 downto 0)
              dataout(6) := crb(15 downto 12)
              dataout(4) := crb(11 downto 8)
              dataout(2) := crb(7 downto 4)
              dataout(0) := crb(3 downto 0)
              o.write_interface
                .slots(WriteSlotPacking.GPRPort1)
                .data := dataout.asBits.asUInt.resized
            }
            is(MnemonicEnums.mfocrf) {
              val cra = i.slots(ReadSlotPacking.CRAPort1).data
              val crb = i.slots(ReadSlotPacking.CRBPort1).data
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
              for (i <- 0 until 8) {
                datamask(i) := 0
                when(fxm(i)) {
                  datamask(i) := 0xf
                }
              }
              val data_masked = dataout.asBits.asUInt & datamask.asBits.asUInt
              o.write_interface
                .slots(WriteSlotPacking.GPRPort1)
                .data := data_masked.resized
            }
          }

        }
      }
    }
  }
}
