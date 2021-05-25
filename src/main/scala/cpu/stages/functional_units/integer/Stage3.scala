package cpu.stages.functional_units.integer

import cpu.interfaces.{FunctionalUnit, FunctionalUnitExit}
import cpu.interfaces.regfile.Slot
import util.{PipeStage}

import spinal.core._
//import spinal.lib.

import cpu.debug.debug_stage3

class Stage3 extends PipeStage(new FunctionalUnit, new FunctionalUnitExit) {
  o.write_interface := i.write_interface
  o.ldst_request := i.ldst_request
  o.cia := i.dec_data.cia

  def compare_slot_value(slot : Slot) = {
    val value = SInt(64 bits)
    value := slot.data(63 downto 0).asSInt
    val cmp = UInt(4 bits)
    when(value < 0){
      cmp := 8
    }.elsewhen(value > 0){
      cmp := 4
    }.otherwise{
      cmp := 2
    }
    cmp
  }
    

  when (i.compare.activate) {
    // look for the slot containing data to compare
    for ((in_slot, in_index) <- i.write_interface.slots.zipWithIndex) {
      // check if the index matches the in_slot value set during form population
      when (in_index === i.compare.in_slot) {
        // use the active slot's value to get the result of comparing to 0
        val comparison = compare_slot_value(in_slot)
        // look for the slot which will hold the compariso result
        for ((out_slot, out_index) <- o.write_interface.slots.zipWithIndex) {
          // check if the index matches the out_slot value set during form population
          when (out_index === i.compare.out_slot) {
            out_slot.data := Cat(comparison, B(0, 12 bits)).asUInt.resized
            if (debug_stage3) {
              // when (pipeOutput.fire()) {
              //   printf(p"STAGE 3: Compared slot ${in_index.U} data 0x${Hexadecimal(in_slot.data)} to 0, got ${Binary(comparison)}\n")
              // }
            }
          }
        }
      }
    }
  }
}

