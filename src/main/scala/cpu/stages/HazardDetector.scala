package cpu.stages

import cpu.interfaces.{ReadInterface, WriteInterface, WriteStageInterface}
import cpu.interfaces.regfile.{SourceSelect, Slot}
import util.{PipeStage}
import isa.MnemonicEnums

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

  read_slots.foreach { read_slot =>
    when((read_slot.sel =/= SourceSelect.NONE)) {
      write_interface_vec
        .zip(stage_valid_vec)
        .zip(stages)
        .foreach { case ((write_slots, commit_is_valid), name) =>
          when(commit_is_valid) {
            write_slots.slots.foreach { write_slot =>
              when(
                write_slot.sel === SourceSelect.CRA || write_slot.sel === SourceSelect.CRB
              ) {
                val cond1 = (write_slot.sel === read_slot.sel)
                val cond2 = (write_slot.idx.resized & read_slot.idx.resized) =/= 0
                when(cond1 && cond2) {
                  when(pipeInput.valid) {
                    ready := False
                    debug(read_slot, write_slot, name)
                  }
                }
              }
                .otherwise {
                  val cond1 = (write_slot.sel === read_slot.sel)
                  val cond2 = (write_slot.idx === read_slot.idx)
                  when(cond1 && cond2) {
                    when(pipeInput.valid) {
                      ready := False
                      debug(read_slot, write_slot, name)
                    }
                  }
                }
            // If we're doing the CR thing, compare the indices with & and not equals
            }
          }
        }
    }
  }

  def debug(read_slot: Slot, write_slot: Slot, name: String) = {
    // import cpu.debug.debug_hazard
    // if (cpu.debug.debug_hazard) {
    //   printf(s"HAZARD: Detected Hazard @ ")

    //   for (sel <- SourceSelect.all) {
    //     when(sel === write_slot.sel) {
    //       printf(s"Write(${sel}, ")
    //     }
    //   }
    //   printf(p"${write_slot.idx}), ")

    //   for (sel <- SourceSelect.all) {
    //     when(sel === read_slot.sel) {
    //       printf(s"Read(${sel}, ")
    //     }
    //   }
    //   printf(p"${read_slot.idx}) ")

    //   printf(s"for stage ${name}")

    //   printf("\n")
    // }
  }

}
