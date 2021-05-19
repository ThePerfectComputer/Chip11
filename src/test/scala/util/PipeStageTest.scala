package util

import spinal.core._
import spinal.lib._
import spinal.sim._
import spinal.core.sim._

import org.scalatest._
import flatspec._
import matchers._

class PipeStageTest extends AnyFlatSpec with should.Matchers {

  it should "create verilog" in {
    SpinalVerilog(new Delay(UInt(8 bits)))
  }

  it should "propagate ready/valid" in {
    SimConfig.withWave.doSim(new Delay(UInt(8 bits))){dut =>
      dut.pipeOutput.flush #= false
      dut.pipeInput.valid #= true
      dut.pipeOutput.ready #= false
      sleep(10)
      assert(dut.pipeOutput.valid.toBoolean == true)
      assert(dut.pipeInput.ready.toBoolean == false)

      sleep(10)
      dut.pipeInput.valid #= false
      dut.pipeOutput.ready #= true
      sleep(10)
      assert(dut.pipeOutput.valid.toBoolean == false)
      assert(dut.pipeInput.ready.toBoolean == true)
      sleep(10)
      dut.pipeInput.payload #= 10
      sleep(10)
      assert(dut.pipeInput.payload.toInt == 10)


    }
  }


}
