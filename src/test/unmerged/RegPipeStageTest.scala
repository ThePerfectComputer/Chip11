package util.test

import util._

import org.scalatest._
import chiseltest._
import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage
import chiseltest.experimental.TestOptionBuilder._

class RegPipeStageTest extends FlatSpec with ChiselScalatestTester with Matchers {
  behavior of "RegisteredPipeStage"

  // it should "create verilog" in {
  //   println("Before unit test?")
  //   val stage = new ChiselStage

  //   println(stage.emitVerilog(new RegisteredPipeStage[AdderInput, UInt, PipeStage[AdderInput, UInt]](new Adder(8))))
  // }

  it should "register data" in {
    test(new RegisteredPipeStage(new Delay))
      .withFlags(Array("--t-write-vcd")) { c =>
        c.pipeOutput.ready.poke(1.B)
        c.pipeInput.ready.expect(1.B)

        c.clock.step(1)

        c.pipeInput.valid.poke(1.B)
        c.pipeInput.bits.poke(10.U)
        c.pipeOutput.valid.expect(0.B)

        c.clock.step(1)
        c.pipeInput.valid.poke(0.B)
        c.pipeInput.bits.poke(0.U)
        c.pipeOutput.ready.poke(0.B)
        c.pipeOutput.valid.expect(1.B)
        c.pipeOutput.bits.expect(10.U)

        c.clock.step(1)
        c.pipeOutput.valid.expect(1.B)
        c.pipeOutput.bits.expect(10.U)

        c.pipeOutput.ready.poke(1.B)

        c.clock.step(1)
        c.pipeOutput.valid.expect(0.B)


      }
  }

}

class DelayLineTest extends FlatSpec with ChiselScalatestTester with Matchers {
  behavior of "MultiDelay"

  // it should "create verilog" in {
  //   println("Before unit test?")
  //   val stage = new ChiselStage

  //   println(stage.emitVerilog(new MultiDelay))
  // }

  it should "flush data" in {
    test(new MultiDelay)
      .withFlags(Array("--t-write-vcd")) { c =>
        val data = Seq(1, 5, 20, 3, 8)
        c.pipeOutput.ready.poke(1.B)

        // Write the elements of data into the pipeline, one at a time
        fork {
          c.clock.step(1)
          c.pipeInput.valid.poke(1.B)
          for(item <- data){
            c.pipeInput.bits.poke(item.U)

            c.clock.step(1)
          }
          // Trigger a flush from inside d3 when item 1 (5.U) is being
          // output. This should clear both item 2 and 3 (20.U, 3.U) from
          // the pipeline, leaving only item 4 (8.U)
        } .fork{
          c.clock.step(4)
          c.stage3Flush.poke(1.B)
          c.clock.step(1)
          c.stage3Flush.poke(0.B)
          c.clock.step(4)

          // Check that only items 0, 1, and 4 come out of the pipeline
        }.fork{
          val dataAfterFlush = Seq(1, 5, 8)
          for(item <- dataAfterFlush){
            var vld = c.pipeOutput.valid.peek()
            while(!vld.litToBoolean){
              c.clock.step(1)
              vld = c.pipeOutput.valid.peek()
            }
            c.pipeOutput.bits.expect(item.U)
            c.clock.step(1)
          }
        }.join()
      }
  }


  it should "delay data" in {
    test(new MultiDelay)
      .withFlags(Array("--t-write-vcd")) { c =>
        c.pipeOutput.ready.poke(1.B)
        c.pipeInput.ready.expect(1.B)

        c.clock.step(1)

        c.pipeInput.valid.poke(1.B)
        c.pipeInput.bits.poke(10.U)
        c.pipeOutput.valid.expect(0.B)

        c.clock.step(1)
        c.pipeInput.valid.poke(0.B)
        c.pipeInput.bits.poke(0.U)
        c.pipeOutput.ready.poke(0.B)

        c.clock.step(1)
        c.pipeOutput.ready.poke(1.B)
        c.pipeOutput.valid.expect(0.B)


        c.clock.step(1)
        c.pipeOutput.valid.expect(1.B)
        c.pipeOutput.bits.expect(10.U)
        c.clock.step(1)


      }
  }
}
