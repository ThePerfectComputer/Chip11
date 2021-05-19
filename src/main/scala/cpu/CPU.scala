package cpu

// import cpu.interfaces.{LineRequest, LineResponse, ReadInterface, WriteInterface}
// import cpu.shared.{Regfile, RegfileMasked}
// import cpu.shared.{BranchPredictor, BRAMMultiRegfile, RegfileMasked, DebugBRAMMultiRegfile, DebugRegfileMasked}
// import cpu.stages.{FetchRequest, FetchResponse, uOpAndFormDecoder, PopulateByForm, ReadStage}
// import cpu.stages.{HazardDetector, LDSTRequest, LDSTResponse, WriteStage}
// import cpu.stages.functional_units.integer.{Stage1, Stage2, Stage3}
// import util.{PipeStage, RegisteredPipeStage, Delay}

import spinal.core._
import spinal.lib._

object config {
  val adder      = true
  val branch     = true
  val logical    = true
  val shifter    = true
  val comparator = true
  val multiplier = false
}

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
  val debug_write                 = true
}
