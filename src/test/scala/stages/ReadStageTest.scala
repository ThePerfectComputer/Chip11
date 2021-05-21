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

import cpu.interfaces.{ReadInterface, WriteInterface, DecoderData, LoadStoreRequest}
import cpu.shared.{Regfile, RegfileMasked, BRAMMultiRegfile}
import cpu.interfaces.regfile.{SourceSelect, ReadPort}

class ReadStageWrapper extends PipeStage(new ReadInterface, new ReadInterface){

  val rs = new ReadStage
  rs.io.bhrb_rp(0).data := 0
  for(idx <- 0 until 2){
    rs.io.vr_rp(idx).data := 0
    rs.io.vsr_rp(idx).data := 0
    rs.io.comb_rp(idx).data := 0
    rs.io.spr_rp(idx).data := 0
    rs.io.cr_rp(idx).data := 0
    rs.io.fpscr_rp(idx).data := 0
    rs.io.fpr_rp(idx).data := 0
  }

  val gpr = new Regfile(32, 64, 2, 1)

  val io = new Bundle {
    val gpr_wp = slave(cloneOf(gpr.io.wp(0)))
  }
  
  rs << pipeInput
  rs >> pipeOutput

  for((rp, gpr_rp) <- rs.io.gpr_rp.zip(gpr.io.rp)){
    rp <> gpr_rp
  }
  gpr.io.wp(0) <> io.gpr_wp


}


class ReadStageTest extends AnyFlatSpec with should.Matchers{
  behavior of "Read"


}


class ReadStageTestVerilog extends AnyFlatSpec with should.Matchers {
  behavior of "ReadStage"

  it should "create verilog" in {
    val config =
      SpinalConfig(mode = Verilog, mergeAsyncProcess = true).withoutEnumString()
    config.generate(new ReadStageWrapper)
  }
}
