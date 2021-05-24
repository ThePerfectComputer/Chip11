package soc

import spinal.core._
import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._



class SoCTestVerilog extends AnyFlatSpec with should.Matchers {
  behavior of "SoC"

  it should "create verilog" in {
    SpinalVerilog(new SoC)
  }

}

class SoCTestRun extends AnyFlatSpec with should.Matchers {
  behavior of "SoC"

  it should "run" in {
    SimConfig.withWave.doSim(new SoC) { dut =>
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.waitSampling(200)
    }
  }

}
