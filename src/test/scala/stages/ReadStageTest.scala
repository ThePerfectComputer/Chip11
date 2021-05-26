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
  rs.io.xer_rp(0).data := 0
  for(idx <- 0 until 2){
    rs.io.comb_rp(idx).data := 0
    rs.io.spr_rp(idx).data := 0
    rs.io.cr_rp(idx).data := 0
    rs.io.fpscr_rp(idx).data := 0
  }

  val gpr_init = Seq.tabulate(32)(x => BigInt(x))
  val vsr_init = Seq.tabulate(64)(x => BigInt(x))

  val gpr = new Regfile(32, 64, 2, 1, initData=gpr_init)
  val fpr = new Regfile(32, 64, 2, 1, initData=gpr_init)
  val vsr = new Regfile(64, 128, 2, 1, initData=vsr_init)
  val vr = new Regfile(32, 128, 2, 1, initData=gpr_init)

  // val io = new Bundle {
  //   val gpr_wp = slave(cloneOf(gpr.io.wp(0)))
  // }
  
  rs << pipeInput
  rs >> pipeOutput

  for((rp, gpr_rp) <- rs.io.gpr_rp.zip(gpr.io.rp)){
    rp <> gpr_rp
  }
  for((rp, fpr_rp) <- rs.io.fpr_rp.zip(fpr.io.rp)){
    rp <> fpr_rp
  }
  for((rp, vsr_rp) <- rs.io.vsr_rp.zip(vsr.io.rp)){
    rp <> vsr_rp
  }
  for((rp, vr_rp) <- rs.io.vr_rp.zip(vr.io.rp)){
    rp <> vr_rp
  }
  for(wp <- Seq(gpr.io.wp(0), fpr.io.wp(0), vr.io.wp(0), vsr.io.wp(0))){
    wp.en := False
    wp.data := 0
    wp.idx := 0
  }
  //gpr.io.wp(0) <> io.gpr_wp


}


class ReadStageTest extends AnyFlatSpec with should.Matchers{
  behavior of "ReadStage"

  it should "read some GPRs" in {
    SimConfig.withWave.doSim(new ReadStageWrapper) { dut =>
      dut.pipeOutput.ready #= true
      dut.pipeOutput.flush #= false
      dut.pipeInput.valid #= false
      //dut.io.gpr_wp.en #= false
      for(idx <- 0 until 5){
          dut.pipeInput.payload.slots(idx).sel #= SourceSelect.NONE
      }
      dut.clockDomain.forkStimulus(10)

      for(i <- 0 until 4){
        dut.pipeInput.valid #= true
        for(idx <- 0 until 4){
          dut.pipeInput.payload.slots(idx).idx #= i*4 + idx
          dut.pipeInput.payload.slots(idx).sel #= SourceSelect.GPR
        }
        sleep(1)

        var valid = dut.pipeOutput.valid.toBoolean
        do {
          dut.clockDomain.waitSampling(1)
          sleep(1)
          valid = dut.pipeOutput.valid.toBoolean
        } while(!valid);
        sleep(1)
        for(idx <- 0 until 4){
          val result = dut.pipeOutput.payload.slots(idx).data.toBigInt
          assert(result == (i*4+idx))
        }
      }
    }
  }

  it should "read GPRs, FPRs, VRs, and VSRs" in {
    SimConfig.withWave.doSim(new ReadStageWrapper) { dut =>
      dut.pipeOutput.ready #= true
      dut.pipeOutput.flush #= false
      dut.pipeInput.valid #= false
      //dut.io.gpr_wp.en #= false
      for(idx <- 0 until 5){
          dut.pipeInput.payload.slots(idx).sel #= SourceSelect.NONE
      }
      dut.clockDomain.forkStimulus(10)

      for(i <- 0 until 8){
        dut.pipeInput.valid #= true
        for(idx <- 0 until 4){
          val A = if(idx < 3){
            Array(SourceSelect.GPR, SourceSelect.FPR, SourceSelect.VSR, SourceSelect.VR)
          } else Array(SourceSelect.GPR, SourceSelect.FPR)

          val choice = A(Random.nextInt(A.size))
          dut.pipeInput.payload.slots(idx).idx #= i*4 + idx
          dut.pipeInput.payload.slots(idx).sel #= choice
        }
        sleep(1)

        var valid = dut.pipeOutput.valid.toBoolean
        do {
          dut.clockDomain.waitSampling(1)
          sleep(1)
          valid = dut.pipeOutput.valid.toBoolean
        } while(!valid);
        sleep(1)
        for(idx <- 0 until 4){
          val result = dut.pipeOutput.payload.slots(idx).data.toBigInt
          assert(result == (i*4+idx))
        }
      }
    }
  }
}


class ReadStageTestVerilog extends AnyFlatSpec with should.Matchers {
  behavior of "ReadStage"

  it should "create verilog" in {
    val config =
      SpinalConfig(mode = Verilog, mergeAsyncProcess = true).withoutEnumString()
    config.generate(new ReadStageWrapper)
  }
}
