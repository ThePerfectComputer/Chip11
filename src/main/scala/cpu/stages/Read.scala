package cpu.stages

import cpu.debug.debug_read
import cpu.interfaces.regfile.{SourceSelect, ReadPort}
import cpu.interfaces.{ReadInterface, WriteInterface, DecoderData, LoadStoreRequest}
import util._
import spinal.core._
import spinal.lib.{master}
import isa.ReadSlotPacking

import scala.collection._

// This allows you to look up a register file read port and cycle
// number from the register file class and slot number
object PortCycleMap {
  // (Enum Selector, source) -> (Register read port, cycle)
  var map = Map[(SourceSelect.E, Int), (Int, Int)]()
  // GPR
  map += ((SourceSelect.GPR, ReadSlotPacking.GPRPort1) -> (0, 1))
  map += ((SourceSelect.GPR, ReadSlotPacking.GPRPort2) -> (1, 1))
  map += ((SourceSelect.GPR, ReadSlotPacking.GPRPort3) -> (0, 2))
  map += ((SourceSelect.GPR, ReadSlotPacking.GPRPort4) -> (1, 2))
  // FPR
  map += ((SourceSelect.FPR, ReadSlotPacking.FPRPort1) -> (0, 1))
  map += ((SourceSelect.FPR, ReadSlotPacking.FPRPort2) -> (1, 1))
  map += ((SourceSelect.FPR, ReadSlotPacking.FPRPort3) -> (0, 2))
  map += ((SourceSelect.FPR, ReadSlotPacking.FPRPort4) -> (1, 2))
  //VSR
  map += ((SourceSelect.VSR, ReadSlotPacking.VSRPort1) -> (0, 1))
  map += ((SourceSelect.VSR, ReadSlotPacking.VSRPort2) -> (1, 1))
  map += ((SourceSelect.VSR, ReadSlotPacking.VSRPort3) -> (0, 2))
  //VR
  map += ((SourceSelect.VR, ReadSlotPacking.VRPort1) -> (0, 1))
  map += ((SourceSelect.VR, ReadSlotPacking.VRPort2) -> (1, 1))
  map += ((SourceSelect.VR, ReadSlotPacking.VRPort3) -> (0, 2))
  // Combined
  map += ((SourceSelect.COMBINED, ReadSlotPacking.COMBINEDPort1) -> (0, 1))
  map += ((SourceSelect.COMBINED, ReadSlotPacking.COMBINEDPort2) -> (1, 1))
  map += ((SourceSelect.COMBINED, ReadSlotPacking.COMBINEDPort3) -> (0, 2))
  // BHRB
  map += ((SourceSelect.BHRB, ReadSlotPacking.BHRBPort1) -> (0, 1))
  // SPR
  map += ((SourceSelect.SPR, ReadSlotPacking.SPRPort1) -> (0, 1))
  map += ((SourceSelect.SPR, ReadSlotPacking.SPRPort2) -> (1, 1))
  // CR
  map += ((SourceSelect.CRA, ReadSlotPacking.CRPort1) -> (0, 1))
  map += ((SourceSelect.CRB, ReadSlotPacking.CRPort2) -> (1, 1))
  // FPSCR
  map += ((SourceSelect.FPSCR, ReadSlotPacking.FPSCRPort1) -> (0, 1))
  map += ((SourceSelect.FPSCR, ReadSlotPacking.FPSCRPort1) -> (1, 1))



  def apply(sel: SourceSelect.E, port: Int) = map.get((sel, port))
}

class ReadStageIO extends Bundle {
  val gpr_rp = Vec(master(new ReadPort(5, 64)), 2)
  val vr_rp = Vec(master(new ReadPort(5, 128)), 2)
  val vsr_rp = Vec(master(new ReadPort(6, 128)), 2)
  val fpr_rp = Vec(master(new ReadPort(5, 64)), 2)
  val comb_rp = Vec(master(new ReadPort(5, 64)), 2)
  val bhrb_rp = Vec(master(new ReadPort(5, 64)), 1)
  val spr_rp = Vec(master(new ReadPort(10, 64)), 2)
  val cr_rp = Vec(master(new ReadPort(1, 16)), 2)
  val fpscr_rp = Vec(master(new ReadPort(1, 16)), 2)
}

class ReadStage extends PipeStageIO(new ReadInterface, new ReadInterface, new ReadStageIO) {

  val internal_ready = Bool()
  internal_ready := True
  ready := internal_ready

  // Initialize all the register file read ports
  for(idx <- 0 until 2){
    io.gpr_rp(idx).idx := 0
    io.vr_rp(idx).idx := 0
    io.vsr_rp(idx).idx := 0
    io.fpr_rp(idx).idx := 0
    io.comb_rp(idx).idx := 0
    io.bhrb_rp(0).idx := 0
    io.spr_rp(idx).idx := 0
    io.cr_rp(idx).idx := 0
    io.fpscr_rp(idx).idx := 0
  }

  // We need to latch some of the incoming slot data
  val incoming_sel = Reg(Vec(cloneOf(i.slots(0).sel), 5))
  val incoming_idx = Reg(Vec(cloneOf(i.slots(0).idx), 5))
  for (slot_index <- 0 until 5) {
    // set initial values
    incoming_sel(slot_index) := incoming_sel(slot_index).getZero
    incoming_idx(slot_index) := 0
    // when valid, latch incoming data
    when (pipeInput.valid) {
      incoming_sel(slot_index) := i.slots(slot_index).sel
      incoming_idx(slot_index) := i.slots(slot_index).idx
    }
  }

  // Initialize the output registers
  for(idx <- 0 until 5) {
    // for read interface
    o.slots(idx).data := 0
    o.slots(idx).sel := incoming_sel(idx)
    o.slots(idx).idx := incoming_idx(idx)

  }

  // What cycle we're on
  val cycle = RegInit(U(1, 2 bits))
  cycle := 1

  // Whether we need a second one
  val secondCycleNeeded = Bool
  secondCycleNeeded := False
  // Holds the data read from the read port on the previous cycle
  val data_out = Vec(Seq(UInt(128 bits), UInt(128 bits), UInt(128 bits), UInt(64 bits), UInt(64 bits)))
  val data_out_reg = Reg(cloneOf(data_out))
  data_out_reg := data_out
  for( elem <- data_out) {
    elem := 0
  }
  // Take data from data_out versus data_out_reg
  val forward_data = Vec(Bool, 5)
  for(elem <- forward_data) {
    elem := False
  }

  // Registers for emulating the behavior of RegisteredPipeStage manually
  val main_valid_reg = Reg(Bool())
  main_valid_reg := False




  for((slot, i) <- i.slots.zipWithIndex) {

    def readFromRegfile[T <: Vec[ReadPort]](enumVal: SourceSelect.E, io_rp : T) = {
      val regInfo = PortCycleMap(enumVal, i)
      regInfo match {
        // If we get good data back
        case Some((rpidx, readcycle)) => {
          val resultAvailable = Reg(Bool)
          resultAvailable := False
          when(slot.sel === enumVal){
            // Look up the port index and cycle map
            // Assign to the read port index on the correct cycle
            when(cycle === readcycle) {
              io_rp(rpidx).idx := slot.idx
              resultAvailable := True
            }
            // Also indicate if we need a second cycle
            if(readcycle == 2)
              secondCycleNeeded := True
          }
          // If the register file has produced a result, place the data in data_out
          when(resultAvailable === True){
            data_out(i) := io_rp(rpidx).data
            forward_data(i) := True
          }
        }
        case None =>
      }
    }

    readFromRegfile(SourceSelect.GPR, io.gpr_rp)
    readFromRegfile(SourceSelect.FPR, io.fpr_rp)
    readFromRegfile(SourceSelect.VSR, io.vsr_rp)
    readFromRegfile(SourceSelect.VR, io.vr_rp)
    readFromRegfile(SourceSelect.COMBINED, io.comb_rp)
    readFromRegfile(SourceSelect.BHRB, io.bhrb_rp)
    readFromRegfile(SourceSelect.SPR, io.spr_rp)
    readFromRegfile(SourceSelect.CRA, io.cr_rp)
    readFromRegfile(SourceSelect.CRB, io.cr_rp)
    readFromRegfile(SourceSelect.FPSCR, io.fpscr_rp)
  }

  // Send the data to the pipeline output
  for((slot, i) <- o.slots.zipWithIndex){
    // If a given piece of data is in data_out
    when(slot.sel === SourceSelect.NONE){
      slot.data := 0
    } .elsewhen(forward_data(i) === True) {
      slot.data := data_out(i)
    } .otherwise {
      slot.data := data_out_reg(i)
    }
  }

  when(cycle === 1){
    // If we need to delay, update the cycle counter and clear the ready bit
    when(secondCycleNeeded =/= False){
      internal_ready := False
      cycle := 2
      // Otherwise, delay valid/spec by one cycle
    } .otherwise {
      main_valid_reg := pipeInput.valid
    }
  }.otherwise {
    cycle := 1
    main_valid_reg := pipeInput.valid
  }

  // State for extra data in read interface
  val imm_reg = Reg(cloneOf(i.imm))
  val dec_data_reg = Reg(new DecoderData)
  val write_interface_reg = Reg(new WriteInterface)
  val ldst_request_reg = Reg(new LoadStoreRequest)
  val compare_reg = Reg(cloneOf(i.compare))

  imm_reg := i.imm
  dec_data_reg := i.dec_data
  write_interface_reg := i.write_interface
  ldst_request_reg := i.ldst_request
  compare_reg := i.compare


  o.imm := imm_reg
  o.dec_data := dec_data_reg
  o.write_interface := write_interface_reg
  o.ldst_request := ldst_request_reg
  o.compare := compare_reg



  // Upon a speculative flush, reset the stage so it can begin reading on the next cycle
  // We have to do this manually, because the stage is already
  // registered (we're essentially implementing RegisteredPipeStage by
  // hand).
  when(pipeOutput.flush){
    main_valid_reg := False
    cycle := 1
  }

  pipeOutput.valid := main_valid_reg

  import cpu.debug.debug_read
  if (debug_read) {
    // when (pipeOutput.fire()) {
    //   val sels = Wire(Vec(Bool()), 5)
    //   for (idx <- 0 until 5) {
    //     sels(idx) := pipeOutput.bits.slots(idx).sel =/= SourceSelect.NONE
    //   }
    //   when (sels.asUInt.orR) {
    //     printf("READ: Loaded slots\n")
    //   } .otherwise {
    //     printf("READ: No slots loaded\n")
    //   }

    //   for ((slot, index) <- pipeOutput.bits.slots.zipWithIndex) {
    //     when (slot.sel =/= SourceSelect.NONE) {
    //       for (sel <- SourceSelect.all) {
    //         when (sel === slot.sel) {
    //           printf(s"\tSlot $index:\n\t\tSource:\t$sel\n")
    //           printf(p"\t\tIndex:\t${slot.idx}\n\t\tData:\t0x${Hexadecimal(slot.data)}\n")
    //         }
    //       }
    //     }
    //   }
    // }
  }
}
