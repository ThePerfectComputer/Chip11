package soc

import spinal.core._
import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._

import java.io._

class CSVLogger(dut: SoC, filePath: String) {
  val writer = new PrintWriter(new File(filePath))
  dut.clockDomain.onSamplings {

    if (dut.cpu.write.pipeOutput.valid.toBoolean) {
      val reg = dut.cpu.gpr.mem.getBigInt(1)
      writer.write(s"r1 value: $reg\n")
      writer.flush()
    }
  }

}

class SoCTestVerilog extends AnyFlatSpec with should.Matchers {
  behavior of "SoC"

  it should "create verilog" in {
    SpinalVerilog(new SoC)
  }

}

class SoCTestRun extends AnyFlatSpec with should.Matchers {
  behavior of "SoC"

  it should "run" in {
    SimConfig.withWave.doSim(new SoC("c_sources/tests/test_simple/test_le.bin")) {
      dut =>
      val logger = new CSVLogger(dut, "test.csv")
        dut.clockDomain.forkStimulus(10)
        dut.clockDomain.waitSampling(200)
    }
  }

}
