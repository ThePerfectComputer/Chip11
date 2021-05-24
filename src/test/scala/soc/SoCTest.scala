package soc

import spinal.core._
//import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._

import java.io._

class CSVLogger(dut: SoC, filePath: String) {
  val writer = new PrintWriter(new File(filePath))
  writer.write("cia,cr,r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17,r18,r19,r20,r21,r22,r23,r24,r25,r26,r27,r28,r29,r30,r31\n")
  dut.clockDomain.onSamplings {

    if (dut.cpu.write.pipeOutput.valid.toBoolean) {
      val cia = dut.cpu.write.pipeOutput.payload.toBigInt
      val cr = 0
      writer.write(f"$cia%x,")
      writer.write(f"$cr%x,")
      for(i <- 0 until 32){
        val reg = dut.cpu.gpr.mem.getBigInt(i)
        if(i == 31)
          writer.write(f"$reg%x\n")
        else
          writer.write(f"$reg%x,")
      }
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
    SimConfig.withWave.doSim(new SoC) {
      dut =>
      dut.ram.loadFromFile("c_sources/tests/test_simple/test_le.bin")
      val logger = new CSVLogger(dut, "test.csv")
        dut.clockDomain.forkStimulus(10)
        dut.clockDomain.waitSampling(200)
    }
  }

}
