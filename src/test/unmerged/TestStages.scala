package util

import spinal.core._
import spinal.lib._

class AdderInput(val wid: Int) extends Bundle {
  val a = UInt(wid.W)
  val b = UInt(wid.W)
}

class AdderOutput extends Bundle {
  val y = UInt(8.W)
}

class Adder(val wid: Int) extends PipeStage(new AdderInput(wid), UInt(wid.W)){
  o := i.a + i.b
}

class Delay extends PipeStage(UInt(8.W), UInt(8.W)){
  o := i
}

class MultiDelay extends PipeStage(UInt(8.W), UInt(8.W)){

  val stage3Flush = IO(Input(Bool()))

  val d1 = Module(new RegisteredPipeStage(new Delay))
  val d2 = Module(new RegisteredPipeStage(new Delay))
  val d3 = Module(new Delay)


  connectIn(d1)             // Connect this module's inputs to d1
    .connect(d2)            // Connect d1 -> d2
    .connect(d3)            // Connect d2 -> d3
    .connectOut(pipeOutput) // Connect d3's to this modules' outputs

  d1.pipeOutput.flush := stage3Flush
}
