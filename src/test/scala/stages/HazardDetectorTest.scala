package cpu.stages

import spinal.core._
import spinal.sim._
import spinal.lib.{master, slave}

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._
import scala.util.Random
import scala.collection.mutable._
import util._

import cpu.interfaces.{ReadInterface, WriteInterface}


class HazardDUT extends PipeStage(new ReadInterface, new ReadInterface){
  val numStages = 8
  val io = new Bundle {
    // contains all the writeback indexes for stages after
    // hazard detector
    val write_interface_vec = in(Vec(new WriteInterface, numStages))
    val stage_valid_vec = in(Vec(Bool, numStages))
  }
  val write_interface_reg = Reg(Vec(new WriteInterface, numStages))
  val stage_valid_reg = Reg(Vec(Bool, numStages))
  write_interface_reg := io.write_interface_vec
  stage_valid_reg := io.stage_valid_vec

  val names = Seq.tabulate(numStages)(x => s"$x")

  val haz = new HazardDetector(names)
  haz.io.write_interface_vec := write_interface_reg
  haz.io.stage_valid_vec := stage_valid_reg
  haz <-< pipeInput
  haz >-> pipeOutput
}

class HazardDetectorVerilog extends AnyFlatSpec with should.Matchers {
  behavior of "HazardDetector"


  it should "create verilog" in {
    SpinalVerilog(new HazardDUT)
  }
}
