package cpu.stages

import cpu.interfaces.regfile.{WritePort, WritePortMasked}
import cpu.interfaces.WriteStageInterface
import cpu.shared.{RegfileInfo}
import util._
import spinal.core._
import spinal.lib.{master}
import isa.{WriteSlotPacking, SourceSelect}

import scala.collection._

// This allows you to look up a register file read port and cycle
// number from the register file class and slot number
object WritePortCycleMap {
  // (Enum Selector, source) -> (Register read port, cycle)
  var map = Map[(SourceSelect.E, Int), (Int, Int)]()
  // GPR
  map += ((SourceSelect.GPR, WriteSlotPacking.GPRPort1) -> (0, 1))
  map += ((SourceSelect.GPR, WriteSlotPacking.GPRPort2) -> (1, 1))

  // FPR
  map += ((SourceSelect.FPR, WriteSlotPacking.FPRPort1) -> (0, 1))
  map += ((SourceSelect.FPR, WriteSlotPacking.FPRPort2) -> (1, 1))

  // VSR
  map += ((SourceSelect.VSR, WriteSlotPacking.VSRPort1) -> (0, 1))
  map += ((SourceSelect.VSR, WriteSlotPacking.VSRPort2) -> (0, 2))

  // VR
  map += ((SourceSelect.VR, WriteSlotPacking.VRPort1) -> (0, 1))
  map += ((SourceSelect.VR, WriteSlotPacking.VRPort2) -> (0, 2))

  // Combined
  map += ((SourceSelect.COMBINED, WriteSlotPacking.COMBINEDPort1) -> (0, 1))
  map += ((SourceSelect.COMBINED, WriteSlotPacking.COMBINEDPort2) -> (0, 2))

  // BHRB
  map += ((SourceSelect.BHRB, WriteSlotPacking.BHRBPort1) -> (0, 1))

  // SPR
  map += ((SourceSelect.SPR, WriteSlotPacking.SPRPort1) -> (0, 1))
  map += ((SourceSelect.SPR, WriteSlotPacking.SPRPort2) -> (0, 2))

  // CR
  map += ((SourceSelect.CRA, WriteSlotPacking.CRPort1) -> (0, 1))
  map += ((SourceSelect.CRB, WriteSlotPacking.CRPort2) -> (1, 1))

  // FPSCR
  map += ((SourceSelect.FPSCR, WriteSlotPacking.FPSCRPort1) -> (0, 1))
  map += ((SourceSelect.FPSCR, WriteSlotPacking.FPSCRPort2) -> (0, 2))

  map += ((SourceSelect.XER, WriteSlotPacking.XERPort1) -> (0, 1))

  def apply(sel: SourceSelect.E, port: Int) = map.get((sel, port))
}

import cpu.debug.debug_write

class WriteStage extends PipeStage(new WriteStageInterface, UInt(64 bits)) {
  val io = new Bundle {
    val gpr_wp = Vec(master(new WritePort(RegfileInfo.GPR)), 2)
    val vr_wp = Vec(master(new WritePort(RegfileInfo.VR)), 1)
    val vsr_wp = Vec(master(new WritePort(RegfileInfo.VSR)), 1)
    val fpr_wp = Vec(master(new WritePort(RegfileInfo.FPR)), 2)
    val comb_wp = Vec(master(new WritePort(RegfileInfo.COMB)), 1)
    val bhrb_wp = Vec(master(new WritePort(RegfileInfo.BHRB)), 1)
    val spr_wp = Vec(master(new WritePort(RegfileInfo.SPR)), 1)
    val cr_wp = Vec(master(new WritePortMasked(1, 16, 4)), 2)
    val fpscr_wp = Vec(master(new WritePort(RegfileInfo.FPSCR)), 1)
    val xer_wp = Vec(master(new WritePortMasked(RegfileInfo.XER)), 1)
  }

  io.vr_wp(0).idx := 0
  io.vsr_wp(0).idx := 0
  io.comb_wp(0).idx := 0
  io.bhrb_wp(0).idx := 0
  io.spr_wp(0).idx := 0
  io.fpscr_wp(0).idx := 0
  io.xer_wp(0).idx := 0
  io.vr_wp(0).data := 0
  io.vsr_wp(0).data := 0
  io.comb_wp(0).data := 0
  io.bhrb_wp(0).data := 0
  io.spr_wp(0).data := 0
  io.fpscr_wp(0).data := 0
  io.xer_wp(0).data := 0
  io.vr_wp(0).en := False
  io.vsr_wp(0).en := False
  io.comb_wp(0).en := False
  io.bhrb_wp(0).en := False
  io.spr_wp(0).en := False
  io.fpscr_wp(0).en := False
  io.xer_wp(0).en := False
  io.xer_wp(0).mask := 0
  for (idx <- 0 until 2) {
    io.gpr_wp(idx).idx := 0
    io.fpr_wp(idx).idx := 0
    io.cr_wp(idx).idx := 0
    io.gpr_wp(idx).data := 0
    io.fpr_wp(idx).data := 0
    io.cr_wp(idx).data := 0
    io.cr_wp(idx).mask := B(15, 4 bits)
    io.gpr_wp(idx).en := False
    io.fpr_wp(idx).en := False
    io.cr_wp(idx).en := False
  }
  io.cr_wp(0).idx.allowOverride
  io.cr_wp(1).idx.allowOverride

  o := i.cia

  val cycle = RegInit(U(1, 2 bits))
  cycle := 1

  val secondCycleNeeded = Bool
  secondCycleNeeded := False

  when(pipeInput.valid) {
    for ((wp, i) <- i.write_interface.slots.zipWithIndex) {
      def writeToRegfile[T <: Vec[WritePort]](
          enumVal: SourceSelect.E,
          io_wp: T
      ) = {
        val regInfo = WritePortCycleMap(enumVal, i)
        regInfo match {
          case Some((wpidx, writecycle)) => {
            // When the selector enum matches enumVal on the given slot
            when(wp.sel === enumVal) {
              when(cycle === writecycle) {
                io_wp(wpidx).idx := wp.idx.resized
                io_wp(wpidx).data := wp.data.resized
                io_wp(wpidx).en := True

                if (debug_write) {
                  report(L"WRITE: ${pipeInput.payload.cia} writing ${wp.data} at ${wp.idx} in regfile ${wp.sel.asBits.asUInt}")
                }
              }
              if (writecycle == 2)
                secondCycleNeeded := True

            }
          }
          case None =>
        }
      }
      def writeToRegfileMasked[T <: Vec[WritePortMasked]](
          enumVal: SourceSelect.E,
          io_wp: T,
          use_mask: Boolean
      ) = {
        val regInfo = WritePortCycleMap(enumVal, i)
        regInfo match {
          case Some((wpidx, writecycle)) => {
            // When the selector enum matches enumVal on the given slot
            when(wp.sel === enumVal) {
              when(cycle === writecycle) {
                io_wp(wpidx).idx := wp.idx.resized
                io_wp(wpidx).data := wp.data.resized
                io_wp(wpidx).en := True
                if (use_mask) {
                  io_wp(wpidx).mask := wp
                    .idx(io_wp(wpidx).mask.getWidth - 1 downto 0)
                    .asBits
                } else {
                  io_wp(wpidx).mask := ~B(0, io_wp(wpidx).mask.getWidth bits)
                }

                if (debug_write) {
                  report(L"WRITE: ${pipeInput.payload.cia} writing ${io_wp(wpidx).data} with mask ${wp.idx} in regfile ${wp.sel.asBits.asUInt}")
                }
                // if (debug_write) {
                //   printf(
                //     p"WRITE: Writing value 0x${Hexadecimal(wp.data.asTypeOf(io_wp(wpidx).data))}"
                //   )
                //   printf(p" with mask ${Binary(io_wp(wpidx).mask.asUInt())}")
                //   printf(s" to $enumVal\n")
                // }
              }
              if (writecycle == 2)
                secondCycleNeeded := True
            }
          }
          case None =>
        }
      }
      writeToRegfile(SourceSelect.GPR, io.gpr_wp)
      writeToRegfile(SourceSelect.FPR, io.fpr_wp)
      writeToRegfile(SourceSelect.VSR, io.vsr_wp)
      writeToRegfile(SourceSelect.VR, io.vr_wp)
      writeToRegfile(SourceSelect.COMBINED, io.comb_wp)
      writeToRegfile(SourceSelect.BHRB, io.bhrb_wp)
      writeToRegfile(SourceSelect.SPR, io.spr_wp)
      writeToRegfileMasked(SourceSelect.CRA, io.cr_wp, use_mask = true)
      writeToRegfileMasked(SourceSelect.CRB, io.cr_wp, use_mask = true)
      writeToRegfile(SourceSelect.FPSCR, io.fpscr_wp)
      writeToRegfileMasked(SourceSelect.XER, io.xer_wp, use_mask = true)
    }
  }
  io.cr_wp(0).idx := 0
  io.cr_wp(1).idx := 1

  when(cycle === 1) {
    // If we need to delay, update the cycle counter and clear the ready bit
    when(secondCycleNeeded) {
      ready := False
      cycle := 2
      // Otherwise, delay valid/spec by one cycle
    }
  }.otherwise {
    cycle := 1
  }
}
