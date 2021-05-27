package cpu.stages.functional_units.integer

import cpu.interfaces.{FunctionalUnit, FunctionalUnitExit}
import util.{PipeStage}
import cpu.uOps.{FunctionalUnit}
import cpu.uOps.functional_units.Integer.{IntegerFUSub, PopcntArgs, PopcntSize}
import isa.{ReadSlotPacking, WriteSlotPacking, SPREnums, MnemonicEnums, Forms}
import cpu.shared.{XERBits, XERMask}
import cpu.{CPUConfig}

import spinal.core._

class Stage2(implicit config: CPUConfig)
    extends PipeStage(new ExecuteData, new ExecuteData) {
  o := i
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
          adderData.assignFromBits(i.additionalData)

          adderMod.io.a := adderData.upperA
          adderMod.io.b := adderData.upperB
          adderMod.io.invert_a := adderData.invert_a
          adderMod.io.carry_in := adderData.carry_32

          o.write_interface
            .slots(WriteSlotPacking.GPRPort1)
            .data(63 downto 32) := adderMod.io.o.resized
          o.ldst_request.ea(63 downto 32) := adderMod.io.o
          o.write_interface
            .slots(WriteSlotPacking.XERPort1)
            .data(XERBits.CA) := adderMod.io.carry_out
          o.write_interface
            .slots(WriteSlotPacking.XERPort1)
            .data(XERBits.OV) := adderMod.io.overflow_out
          //SO handling
          o.write_interface
            .slots(WriteSlotPacking.XERPort1)
            .data(XERBits.SO) := adderMod.io.overflow_out
          when(!adderMod.io.overflow_out) {
            o.write_interface.slots(WriteSlotPacking.XERPort1).idx :=
              i.write_interface
                .slots(WriteSlotPacking.XERPort1)
                .idx & ((~XERMask.SO) & XERMask.ALL)
          }
        }
        is(IntegerFUSub.Shifter) {
          if (config.shifter) {
            val shifterMod2 = new ShifterStage2
            val shifterData = new ShifterPipeData
            shifterData.assignFromBits(i.additionalData)
            shifterMod2.io.pipedata := shifterData
            when(shifterData.is_arithmetic) {
              o.write_interface
                .slots(WriteSlotPacking.XERPort1)
                .data(XERBits.CA) := shifterMod2.io.carry_out
              o.write_interface
                .slots(WriteSlotPacking.XERPort1)
                .data(XERBits.CA32) := shifterMod2.io.carry_out
            }

            o.write_interface
              .slots(WriteSlotPacking.GPRPort1)
              .data := shifterMod2.io.o.resized
          }
        }
      }

      is(IntegerFUSub.Popcnt) {
        if (config.popcnt) {
          val popcntMod = new PopcntStage2
          // def debug_comparator(){
          //   when (pipeOutput.fire()) {
          //     printf(p"\tCOMP. IO: ${comparatorMod.io}\n")
          //   }
          // }
          // if (cpu.debug.debug_stage1) {debug_comparator()}
          val popcntArgs = new PopcntArgs
          popcntArgs.assignFromBits(i.dec_data.uOps.args)

          popcntMod.io.data := i.write_interface
            .slots(WriteSlotPacking.GPRPort1)
            .data(63 downto 0)

          switch(popcntArgs.size) {
            is(PopcntSize.DWORD) {
              o.write_interface
                .slots(WriteSlotPacking.GPRPort1)
                .data := popcntMod.io.count.resized
            }
            is(PopcntSize.WORD) {
              o.write_interface
                .slots(WriteSlotPacking.GPRPort1)
                .data := popcntMod.io.count32.resized
            }
          }
        }
      }
    }
  }
}
