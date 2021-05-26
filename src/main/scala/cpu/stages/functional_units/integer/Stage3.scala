package cpu.stages.functional_units.integer

import cpu.interfaces.{FunctionalUnit, FunctionalUnitExit}
import cpu.interfaces.regfile.Slot
import util.{PipeStage}
import cpu.shared.{XERBits, XERMask}
import cpu.interfaces.regfile.{SourceSelect}
import isa.{WriteSlotPacking}

import spinal.core._
//import spinal.lib.

import cpu.debug.debug_stage3

class Stage3 extends PipeStage(new FunctionalUnit, new FunctionalUnitExit) {
  o.write_interface := i.write_interface
  o.ldst_request := i.ldst_request
  o.cia := i.dec_data.cia
  val so_bit = Bool
  so_bit := i.so_bit
  val xer_slot = i.write_interface.slots(WriteSlotPacking.XERPort1)
  when(xer_slot.sel === SourceSelect.XER && (xer_slot.idx & XERMask.SO) =/= 0){
    so_bit := xer_slot.data(XERBits.SO)
  }

  def compare_slot_value(slot : Slot) = {
    val value = SInt(64 bits)
    value := slot.data(63 downto 0).asSInt
    val cmp = UInt(3 bits)
    when(value < 0){
      cmp := 4
    }.elsewhen(value > 0){
      cmp := 2
    }.otherwise{
      cmp := 1
    }
    Cat(cmp, so_bit)
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

