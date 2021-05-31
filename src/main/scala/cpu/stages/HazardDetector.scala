package cpu.stages

import cpu.interfaces.{ReadInterface, WriteInterface, WriteStageInterface}
import cpu.interfaces.regfile.{Slot}
import util.{PipeStage}
import isa.{MnemonicEnums, SourceSelect}
import isa.{ReadSlotPacking, WriteSlotPacking}

import spinal.core._
import spinal.lib._

import scala.collection.mutable.ListBuffer

class HazardDetector(val stages: Seq[String])
    extends PipeStage(new ReadInterface, new ReadInterface) {
  val io = new Bundle {
    // contains all the writeback indexes for stages after
    // hazard detector
    val write_interface_vec = in(Vec(new WriteInterface, stages.length))
    val stage_valid_vec = in(Vec(Bool, stages.length))
  }

  pipeOutput.payload := pipeInput.payload

  val read_slots = pipeInput.payload.slots
  val write_interface_vec = io.write_interface_vec
  val stage_valid_vec = io.stage_valid_vec

  val numReadSlots = read_slots.size
  val numWriteInterfaces = write_interface_vec.size
  val numWriteSlots = write_interface_vec(0).slots.size

  val readyBits = Bits(numReadSlots * numWriteInterfaces * numWriteSlots bits)
  readyBits.setAll()
  ready.allowOverride
  ready := readyBits.andR

  def matchWriteVec(
      read_slot: Slot,
      readSlotIdx: Int,
      sel: SourceSelect.E,
      slotSet: Set[Int]
  ) = {
    val write_interface_data =
      write_interface_vec zip stage_valid_vec zip stages
    for ((write_data, writeDataIdx) <- write_interface_data.zipWithIndex) {
      write_data match {
        case ((write_slots, commit_is_valid), name) =>
          when(commit_is_valid) {
            matchWriteSlot(
              write_slots,
              read_slot,
              writeDataIdx,
              readSlotIdx,
              sel,
              slotSet
            )
          }
      }
    }
  }

  def matchWriteSlot(
      write_slots: WriteInterface,
      read_slot: Slot,
      writeDataIdx: Int,
      readSlotIdx: Int,
      sel: SourceSelect.E,
      slotSet: Set[Int]
  ) = {
    for ((write_slot, writeSlotIdx) <- write_slots.slots.zipWithIndex) {
      if (slotSet contains writeSlotIdx) {
        hazardDetect(
          write_slot,
          read_slot,
          writeSlotIdx,
          writeDataIdx,
          readSlotIdx,
          sel
        )
      }
    }
  }

  def hazardDetect(
      write_slot: Slot,
      read_slot: Slot,
      writeSlotIdx: Int,
      writeDataIdx: Int,
      readSlotIdx: Int,
      sel: SourceSelect.E
  ) = {

    if (
      Set(SourceSelect.CRA, SourceSelect.CRB, SourceSelect.XER) contains sel
    ) {
      val cond1 = (write_slot.sel === sel)
      val cond2 = (write_slot.idx.resized & read_slot.idx.resized) =/= 0
      when(cond1 && cond2) {
        when(pipeInput.valid) {
          readyBits(
            writeSlotIdx + writeDataIdx * numWriteSlots + readSlotIdx * numWriteSlots * numWriteInterfaces
          ) := False
          debug(read_slot, write_slot, name)
        }
      }
    } else {
      val cond1 = (write_slot.sel === sel)
      val cond2 = (write_slot.idx === read_slot.idx)
      when(cond1 && cond2) {
        when(pipeInput.valid) {
          readyBits(
            writeSlotIdx + writeDataIdx * numWriteSlots + readSlotIdx * numWriteSlots * numWriteInterfaces
          ) := False
          debug(read_slot, write_slot, name)
        }
      }
    }
    // If we're doing the CR thing, compare the indices with & and not equals
  }

  for ((read_slot, readSlotIdx) <- read_slots.zipWithIndex) {
    switch(read_slot.sel) {
      for (sel <- SourceSelect.elements) {
        if (sel != SourceSelect.NONE) {
          val slotSet = ReadSlotPacking(sel)
          val writeSlotSet = WriteSlotPacking(sel)
          if (slotSet contains readSlotIdx) {
            is(sel) {
              matchWriteVec(read_slot, readSlotIdx, sel, writeSlotSet)
            }
          }
        }
      }
    }
  }

  def debug(read_slot: Slot, write_slot: Slot, name: String) = {}

}
