package util.test

import util._

import org.scalatest._
import chiseltest._
import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage
import chiseltest.experimental.TestOptionBuilder._

class PipeStageTest extends FlatSpec with ChiselScalatestTester with Matchers {
  behavior of "PipeStage"

  // it should "create verilog" in {
  //   println("Before unit test?")
  //   val stage = new ChiselStage

  //   println(stage.emitVerilog(new Delay))
  //   // println(stage.emitVerilog(new RegisteredPipeStage[UInt, UInt, PipeStage[UInt, UInt]](new Delay)))
  //   // println(stage.emitVerilog(new RegisteredPipeStage[AdderInput, UInt, PipeStage[AdderInput, UInt]](new Adder(8))))
  // }
  it should "propagate ready/valid" in {
    test(new Delay) 
      .withFlags(Array("--t-write-vcd")) { c =>
        c.pipeInput.valid.poke(1.B)
        c.pipeOutput.ready.poke(0.B)
        c.pipeOutput.valid.expect(1.B)
        c.pipeInput.ready.expect(0.B)
        c.clock.step(1)

        c.pipeInput.valid.poke(0.B)
        c.pipeOutput.ready.poke(1.B)
        c.pipeInput.ready.expect(1.B)
        c.pipeOutput.valid.expect(0.B)

        c.clock.step(1)

        c.pipeInput.bits.poke(10.U)
        c.pipeOutput.bits.expect(10.U)

        c.clock.step(1)


        c.clock.step(1)

      }
  }

}
