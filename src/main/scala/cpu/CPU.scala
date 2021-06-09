package cpu

import cpu.interfaces.{LineRequest, LineResponse, ReadInterface, WriteInterface}
import cpu.shared.{Regfile, RegfileMasked, XERMaskMapping}
import cpu.shared.{BranchPredictor, BRAMMultiRegfile, RegfileMasked}
import cpu.stages.{FetchUnit, uOpAndFormDecoder, PopulateByForm, ReadStage}
import cpu.stages.{HazardDetector, WriteStage, LDSTRequest, LDSTResponse}
import cpu.stages.functional_units.integer.{Stage1, Stage2, Stage3}
import util.{PipeStage, Delay}

import spinal.core._
import spinal.core.sim._
import spinal.lib._

class CPUConfig( 
  val adder: Boolean=true,
  val branch: Boolean=true,
  val logical: Boolean=true,
  val shifter: Boolean=true,
  val comparator: Boolean=true,
  val multiplier: Boolean=false,
  val zcnt: Boolean=true,
  val popcnt: Boolean=true
){}

object debug {
  val debug = false
  val cycle_count                 = debug
  val debug_memory_adaptor        = debug
  val debug_fetch_request         = debug
  val debug_fetch_response        = debug
  val debug_all_decode            = false // shaves off 2 minutes from FIRRTL compile
  val debug_last_decode           = debug // shaves off 1 minute from FIRRTL compile
  val debug_form                  = debug
  val debug_hazard                = debug // shaves off 1 minute from FIRRTL compile
  val debug_read                  = debug
  val debug_stage1                = debug
  val debug_stage1_ifu            = debug_stage1
  val debug_stage1_ifu_adder      = debug_stage1_ifu
  val debug_stage1_ifu_branch     = debug_stage1_ifu
  val debug_stage1_ifu_logical    = debug_stage1_ifu
  val debug_stage1_ifu_multiplier = debug_stage1_ifu
  val debug_stage1_ifu_shifter    = debug_stage1_ifu
  val debug_stage3                = debug
  val debug_write                 = false
}

class CPU(implicit val config: CPUConfig) extends Component {
  // create external interfaces to memory from fetch and loadstore
  val io = new Bundle {
    val fetch_request   = master(new LineRequest)
    val fetch_response  = slave(new LineResponse)

    val ldst_request    = master(new LineRequest)
    val ldst_response   = slave(new LineResponse)
  }

  // if enabled, keep track of current cycle with a counter (useful for waveform debugging)
  if (debug.cycle_count) {
    val cycle_counter = Counter(1000000)
  }

  // instantiate pipeline stages
  val fetch = new FetchUnit
  val decode        = new uOpAndFormDecoder                           // decode instructions into uOps and forms
  val form_pop      = new PopulateByForm     // use form to populate instruction data

  val read          = new ReadStage                                   // read registers into slots selected by form
  val s1            = new Stage1             // execute stage 1 of integer functional unit
  val s2            = new Stage2             // execute stage 2 of integer functional unit
  val s3            = new Stage3             // execute stage 3 of integer functional unit
  val ldst_request  = new LDSTRequest        // interact with memory to read/write register slots
  val ldst_response = new LDSTResponse       // interact with memory to read/write register slots
  val write         = new WriteStage                                  // write slot data into selected registers

  // unfortunately, Chisel's limitations make it difficult to do something
  // more elegant than the following. I tried for almost two days.
  val stagesCommittable = Seq(
    read.getClass.getName(),
    // Needed because read registers its data internally, but needs a
    // registered connection to the next stage to prevent a
    // combinatorial loop
    s"read hidden",
    s"${read.getClass.getName()}output",
    s1.getClass.getName(),
    s2.getClass.getName(),
    s3.getClass.getName(),
    ldst_request.getClass.getName(),
    ldst_response.getClass.getName(),
    write.getClass.getName()
  )
  // val stagesCommittable = Seq(
  //   "read",
  //   "execute1",
  //   "execute2",
  //   "execute3",
  //   "write")

  val hazard        = new HazardDetector(stagesCommittable)

  // connect hazard detector inputs
  hazard.io.write_interface_vec(0) := read.pipeInput.payload.write_interface
  hazard.io.write_interface_vec(1) := read.io.hidden_wi
  hazard.io.write_interface_vec(2) := read.pipeOutput.payload.write_interface
  hazard.io.write_interface_vec(3) := s1.pipeInput.payload.write_interface
  hazard.io.write_interface_vec(4) := s2.pipeInput.payload.write_interface
  hazard.io.write_interface_vec(5) := s3.pipeInput.payload.write_interface
  hazard.io.write_interface_vec(6) := ldst_request.pipeInput.payload.write_interface
  hazard.io.write_interface_vec(7) := ldst_response.pipeInput.payload.write_interface
  hazard.io.write_interface_vec(8) := write.pipeInput.payload.write_interface

  hazard.io.stage_valid_vec(0) := read.pipeInput.valid
  hazard.io.stage_valid_vec(1) := read.io.hidden_wi_valid
  hazard.io.stage_valid_vec(2) := read.pipeOutput.valid
  hazard.io.stage_valid_vec(3) := s1.pipeInput.valid
  hazard.io.stage_valid_vec(4) := s2.pipeInput.valid
  hazard.io.stage_valid_vec(5) := s3.pipeInput.valid
  hazard.io.stage_valid_vec(6) := ldst_request.pipeInput.valid
  hazard.io.stage_valid_vec(7) := ldst_response.pipeInput.valid
  hazard.io.stage_valid_vec(8) := write.pipeInput.valid

  // connect up pipeline stages
  decode <-< fetch.pipeOutput
  decode >-> form_pop >/-> hazard >-> read >->
  s1 >/-> s2 >-> s3 >-> ldst_request >/-> ldst_response >/-> write

  val flushLatency = LatencyAnalysis(s2.pipeInput.flush, fetch.pipeOutput.flush)
  println(s"flush latency: $flushLatency")
  
  // instantiate other hardware that isn't pipeline stages
  val branch        = new BranchPredictor                             // branch predictor

  // connect fetch unit to branch predictor
  fetch.io.bp_request <> branch.io.fetch_req_interface
  fetch.io.bp_response <> branch.io.fetch_resp_interface
  // connect branch predictor to the execute stage 1 branch unit

  val branchDelayed  = Delay(s2.io.bc, flushLatency, init=s2.io.bc.getZero)
  branch.io.b_ctrl := branchDelayed

  // connect fetch and loadstore to memory interfaces
  // fetch_req.io.line_request       <> io.fetch_request
  io.fetch_request       <> fetch.io.line_request
  fetch.io.line_response <> io.fetch_response
  ldst_request.io        <> io.ldst_request
  ldst_response.io       <> io.ldst_response

  // instantiate the regfile(s)
  // val gpr = if(doDebug) new DebugBRAMMultiRegfile(32, 64, 2, 2) else
  //     new BRAMMultiRegfile(32, 64, 2, 2)
  // val spr = if(doDebug) new DebugBRAMMultiRegfile(1024, 64, 2, 1) else
  //     new BRAMMultiRegfile(1024, 64, 2, 1)
  // val cr  = if(doDebug) new DebugRegfileMasked(2, 16, 2, 2, 4) else
  //     new RegfileMasked(2, 16, 2, 2, 4)
  //val gpr =  new BRAMMultiRegfile(32, 64, 2, 2).setDefinitionName("gprRegfile")
  val gpr = new BRAMMultiRegfile(32, 64, 2, 2).setDefinitionName("gprRegfile")
  val spr =  new BRAMMultiRegfile(1024, 64, 2, 1).setDefinitionName("sprRegfile")
  val cr  =  new RegfileMasked(2, 16, 2, 2, 4).setDefinitionName("crRegfile")
  val xer  =  new RegfileMasked(1, 64, 1, 1, 6, () => new XERMaskMapping)
  xer.setDefinitionName("xerRegfile")

  
  // connect the GPR regfile read ports to the read stage,
  // and the GPR regfile write ports to the write stage
  for(i <- 0 until 2) {
    gpr.io.rp(i) <> read.io.gpr_rp(i)
    gpr.io.wp(i) <> write.io.gpr_wp(i)
    spr.io.rp(i) <> read.io.spr_rp(i)
    // same for the CR
  }
  cr.io.rp(0) <> read.io.cr_rp(0)
  cr.io.rp(1) <> read.io.cr_rp(1)
  cr.io.wp(0) <> write.io.cr_wp(0)
  cr.io.wp(1) <> write.io.cr_wp(1)

  xer.io.rp(0) <> read.io.xer_rp(0)
  xer.io.wp(0) <> write.io.xer_wp(0)

  spr.io.wp(0) <> write.io.spr_wp(0)

  // default tieoffs for read stage ports
  for(i <- 0 until 2) {
    read.io.vr_rp(i).data    := 0
    read.io.vsr_rp(i).data   := 0
    read.io.fpr_rp(i).data   := 0
    read.io.comb_rp(i).data  := 0
    read.io.fpscr_rp(i).data := 0
  }
  read.io.bhrb_rp(0).data    := 0

  // TODO tie off any unused signals (register ports, etc.)

  // ensure first pipeline stage has valid input

  // ensure last pipeline stage is always ready
  write.pipeOutput.ready := True
  write.pipeOutput.flush := False

  write.pipeOutput.simPublic()
}
