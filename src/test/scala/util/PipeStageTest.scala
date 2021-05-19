package util

import spinal.core._
import spinal.lib._
import spinal.sim._
import spinal.core.sim._

import org.scalatest._
import flatspec._
import matchers._

class PipeStageTest extends AnyFlatSpec with should.Matchers {

  // it should "create verilog" in {
  //   println("Before unit test?")
  //   val stage = new ChiselStage

  //   println(stage.emitVerilog(new Delay))
  //   // println(stage.emitVerilog(new RegisteredPipeStage[UInt, UInt, PipeStage[UInt, UInt]](new Delay)))
  //   // println(stage.emitVerilog(new RegisteredPipeStage[AdderInput, UInt, PipeStage[AdderInput, UInt]](new Adder(8))))
  // }
  // it should "propagate ready/valid" in {
  // }

  it should "propagate ready/valid" in {
    SimConfig.withWave.doSim(new Delay(UInt(8 bits))){dut =>
      dut.clockDomain.forkStimulus(period = 10)
    }
  }

    // ToyMemSim.main(Array(""))
  // }

  // it should "propagate ready/valid" in {
  //   test(new Delay) 
  //     .withFlags(Array("--t-write-vcd")) { c =>
  //       c.pipeInput.valid.poke(1.B)
  //       c.pipeOutput.ready.poke(0.B)
  //       c.pipeOutput.valid.expect(1.B)
  //       c.pipeInput.ready.expect(0.B)
  //       c.clock.step(1)

  //       c.pipeInput.valid.poke(0.B)
  //       c.pipeOutput.ready.poke(1.B)
  //       c.pipeInput.ready.expect(1.B)
  //       c.pipeOutput.valid.expect(0.B)

  //       c.clock.step(1)

  //       c.pipeInput.bits.poke(10.U)
  //       c.pipeOutput.bits.expect(10.U)

  //       c.clock.step(1)


  //       c.clock.step(1)

  //     }
  // }

}
