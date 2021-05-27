package cpu.stages

import cpu.interfaces.{ReadInterface, WriteInterface, WriteStageInterface}
import cpu.interfaces.regfile.{SourceSelect, Slot}
import cpu.shared.{RegfileInfo}
import util.{PipeStage}
import isa.{MnemonicEnums, ReadSlotPacking, WriteSlotPacking}

import spinal.core._
import spinal.lib._

import scala.collection.mutable.ListBuffer

class HazardDetector2(val stages: Seq[String])
    extends PipeStage(new ReadInterface, new ReadInterface) {
  val io = new Bundle {
    // contains all the writeback indexes for stages after
    // hazard detector
    val write_interface_vec = in(Vec(new WriteInterface, stages.length))
    val stage_valid_vec = in(Vec(Bool, stages.length))
  }

  // Bitvector tracking register usage for each register file
  val gpr_bits = Bits(RegfileInfo.GPR.numRegisters bits)

  pipeOutput.payload := pipeInput.payload

  val read_slots = pipeInput.payload.slots
  val write_interface_vec = io.write_interface_vec
  val stage_valid_vec = io.stage_valid_vec

  val write_interface_data = write_interface_vec zip stage_valid_vec zip stages
  val write_bit_vecs = Seq.fill(stages.size)(Vec(Bool, RegfileInfo.GPR.numRegisters))
  for ((write_data, bit_vec) <- write_interface_data zip write_bit_vecs) {
    bit_vec := bit_vec.getZero
    write_data match {
      case ((write_slots, commit_is_valid), name) =>
        when(commit_is_valid) {
          for ((slot, idx) <- write_slots.slots.zipWithIndex) {
            if (WriteSlotPacking.GPRSlots contains idx) {
              when(slot.sel === SourceSelect.GPR) {
                bit_vec(slot.idx(RegfileInfo.GPR.idxBits - 1 downto 0)) := True
              }
            }
          }
        }
    }
  }
  gpr_bits := write_bit_vecs.map(_.asBits).reduce((x, y) => x|y)

  val gpr_read_bits = Vec(Bool, RegfileInfo.GPR.numRegisters)
  gpr_read_bits := gpr_read_bits.getZero
  for ((read_slot, idx) <- read_slots.zipWithIndex) {
    if(ReadSlotPacking.GPRSlots contains idx){
      when(read_slot.sel === SourceSelect.GPR) {
        gpr_read_bits(read_slot.idx(RegfileInfo.GPR.idxBits - 1 downto 0)) := True
      }
    }
  }

  when((gpr_read_bits.asBits & gpr_bits.asBits).orR){
    ready := False
  }

}
