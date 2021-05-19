package util.test

import util._

import spinal.core._
import spinal.lib._
import spinal.sim._
import spinal.core.sim._

import org.scalatest._
import flatspec._
import matchers._

class DelayReg[T <: Data](gen: T) extends PipeStage(gen, gen) {
  pipeInput.ready.allowOverride
  pipeInput.flush.allowOverride
  pipeOutput.valid.allowOverride
  pipeOutput.payload.allowOverride

  pipeInput >-> pipeOutput
}

class RegPipeStageTest extends AnyFlatSpec with should.Matchers {
  behavior of "DelayReg"

  it should "register data" in {
    SimConfig.withWave.doSim(new DelayReg(UInt(8 bits))) { dut =>
      dut.clockDomain.forkStimulus(10)
      dut.pipeOutput.ready #= true
      dut.pipeInput.valid #= false
      dut.pipeOutput.flush #= false
      sleep(1)
      assert(dut.pipeInput.ready.toBoolean == true)

      dut.clockDomain.waitSampling(2)

      dut.pipeInput.valid #= true
      dut.pipeInput.payload #= 10
      assert(dut.pipeOutput.valid.toBoolean == false)

      dut.clockDomain.waitSampling(1)
      dut.pipeInput.valid #= false
      dut.pipeInput.payload #= 0
      dut.pipeOutput.ready #= false
      sleep(1)
      assert(dut.pipeOutput.valid.toBoolean == true)
      assert(dut.pipeOutput.payload.toInt == 10)

      dut.clockDomain.waitSampling(1)
      assert(dut.pipeOutput.valid.toBoolean == true)
      assert(dut.pipeOutput.payload.toInt == 10)

      dut.pipeOutput.ready #= true

      dut.clockDomain.waitSampling(2)
      assert(dut.pipeOutput.valid.toBoolean == false)

    }
  }

}

class DelayLineTest extends AnyFlatSpec with should.Matchers {
  behavior of "MultiDelay"

  // it should "create verilog" in {
  //   println("Before unit test?")
  //   val stage = new ChiselStage

  //   println(stage.emitVerilog(new MultiDelay))
  // }

  it should "flush data" in {
    SimConfig.withWave.doSim(new MultiDelay) { dut =>
      dut.clockDomain.forkStimulus(10)
      dut.pipeInput.valid #= false
      dut.stage3Flush #= false
      dut.pipeOutput.flush #= false
      dut.pipeOutput.ready #= true
      val data = Seq(1, 5, 20, 3, 8)
      dut.pipeOutput.ready #= true
      dut.clockDomain.waitSampling(5)

      // Write the elements of data into the pipeline, one at a time
      val inputThread = fork {
        dut.clockDomain.waitSampling(1)
        dut.pipeInput.valid #= true
        for (item <- data) {
          dut.pipeInput.payload #= item

          dut.clockDomain.waitSampling(1)
        }
        // Trigger a flush from inside d3 when item 1 (5) is being
        // output. This should clear both item 2 and 3 (20, 3.U) from
        // the pipeline, leaving only item 4 (8)
      }
      val flushThread = fork {
        dut.clockDomain.waitSampling(4)
        dut.stage3Flush #= true
        dut.clockDomain.waitSampling(1)
        dut.stage3Flush #= false
        dut.clockDomain.waitSampling(4)

        // Check that only items 0, 1, and 4 come out of the pipeline
      }
      val outputThread = fork {
        val dataAfterFlush = Seq(1, 5, 8)
        for (item <- dataAfterFlush) {
          var vld = dut.pipeOutput.valid.toBoolean
          while (!vld) {
            dut.clockDomain.waitSampling(1)
            vld = dut.pipeOutput.valid.toBoolean
          }
          assert(dut.pipeOutput.payload.toInt == item)
          dut.clockDomain.waitSampling(1)
        }
      }

      inputThread.join()
      flushThread.join()
      outputThread.join()
    }
  }

  it should "delay data" in {
    SimConfig.withWave.doSim(new MultiDelay) { dut =>
      dut.clockDomain.forkStimulus(10)
      dut.pipeInput.valid #= false
      dut.stage3Flush #= false
      dut.pipeOutput.flush #= false
      dut.pipeOutput.ready #= true
      assert(dut.pipeInput.ready.toBoolean == true)

      dut.clockDomain.waitSampling(1)

      dut.pipeInput.valid #= true
      dut.pipeInput.payload #= 10
      assert(dut.pipeOutput.valid.toBoolean == false)

      dut.clockDomain.waitSampling(1)
      dut.pipeInput.valid #= false
      dut.pipeInput.payload #= 0
      dut.pipeOutput.ready #= false

      dut.clockDomain.waitSampling(1)
      dut.pipeOutput.ready #= true
      assert(dut.pipeOutput.valid.toBoolean == false)

      dut.clockDomain.waitSampling(1)
      assert(dut.pipeOutput.valid.toBoolean == true)
      assert(dut.pipeOutput.payload.toInt == 10)
      dut.clockDomain.waitSampling(1)

    }
  }
}
