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

import cpu.interfaces.{
  ReadInterface,
  WriteInterface,
  DecoderData,
  LoadStoreRequest
}
import cpu.shared.{Regfile, RegfileMasked, BRAMMultiRegfile}
import cpu.interfaces.regfile.{ReadPort}
import isa.{SourceSelect}

class WriteStageTest extends AnyFlatSpec with should.Matchers {
  behavior of "WriteStage"

  it should "write GPRs" in {
    SimConfig.withWave.doSim(new WriteStage) { dut =>
      dut.pipeOutput.ready #= true
      dut.pipeOutput.flush #= false
      dut.pipeInput.valid #= false
      for(idx <- 0 until 5){
          dut.pipeInput.payload.write_interface
            .slots(idx)
            .sel #= SourceSelect.NONE
      }
      //dut.io.gpr_wp.en #= false
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.waitSampling(10)

      for (i <- 0 until 16) {
        for (idx <- 0 until 2) {
          dut.pipeInput.valid #= true
          dut.pipeInput.payload.write_interface.slots(idx).idx #= i * 2 + idx
          dut.pipeInput.payload.write_interface
            .slots(idx)
            .sel #= SourceSelect.GPR
          dut.pipeInput.payload.write_interface
            .slots(idx)
            .data #= idx * 37 + i * 102
        }
        for (idx <- 0 until 2) {
          sleep(1)
          assert(dut.io.gpr_wp(idx).idx.toInt == i * 2 + idx)
          assert(dut.io.gpr_wp(idx).en.toBoolean == true)
          assert(dut.io.gpr_wp(idx).data.toBigInt == idx * 37 + i * 102)
        }
        dut.clockDomain.waitSampling(1)
      }

    }
  }
  it should "write SPRs" in {
    SimConfig.withWave.doSim(new WriteStage) { dut =>
      dut.pipeOutput.ready #= true
      dut.pipeOutput.flush #= false
      dut.pipeInput.valid #= false
      for(idx <- 0 until 5){
          dut.pipeInput.payload.write_interface
            .slots(idx)
            .sel #= SourceSelect.NONE
      }
      //dut.io.gpr_wp.en #= false
      dut.clockDomain.forkStimulus(10)
      for (i <- 0 until 16) {
        dut.pipeInput.valid #= true
        for (idx <- 0 until 2) {
          dut.pipeInput.valid #= true
          dut.pipeInput.payload.write_interface
            .slots(idx + 3)
            .idx #= i * 2 + idx
          dut.pipeInput.payload.write_interface
            .slots(idx + 3)
            .sel #= SourceSelect.SPR
          dut.pipeInput.payload.write_interface
            .slots(idx + 3)
            .data #= idx * 37 + i * 102
        }
        for (idx <- 0 until 2) {
          sleep(1)
          assert(dut.io.spr_wp(0).idx.toInt == i * 2 + idx)
          assert(dut.io.spr_wp(0).en.toBoolean == true)
          assert(dut.io.spr_wp(0).data.toBigInt == idx * 37 + i * 102)
          dut.clockDomain.waitSampling(1)
        }
      }
    }
  }
}

class WriteStageTestVerilog extends AnyFlatSpec with should.Matchers {
  behavior of "WriteStage"

  it should "create verilog" in {
    val config =
      SpinalConfig(mode = Verilog, mergeAsyncProcess = true).withoutEnumString()
    config.generate(new WriteStage)
  }
}
