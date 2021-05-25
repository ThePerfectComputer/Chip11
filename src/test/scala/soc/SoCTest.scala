package soc

import spinal.core._
//import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._

import java.io._
import scala.io.Source
import scala.Console
import Console.{RED, RESET}

class CSVLogger(dut: SoC, filePath: String) {
  val writer = new PrintWriter(new File(filePath))
  writer.write(
    "cia,cr,r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17,r18,r19,r20,r21,r22,r23,r24,r25,r26,r27,r28,r29,r30,r31\n"
  )
  def getCr : BigInt = {
    val cra = dut.cpu.cr.mem.getBigInt(0)
    val crb = dut.cpu.cr.mem.getBigInt(1)
    var result = BigInt(0)
    for(i <- 0 until 4){
      val mask = 0xf << (i*4)
      val cra_field = (cra & mask) >> (i*4)
      val crb_field = (crb & mask) >> (i*4)
      result = result | (cra_field << ((i*2+1)*4)) | (crb_field << (i*2*4))
    }
    result
  }
  var iteration = 0
  dut.clockDomain.onSamplings {

    if (dut.cpu.write.pipeOutput.valid.toBoolean) {
      val cia = dut.cpu.write.pipeOutput.payload.toBigInt
      val cr = getCr
      writer.write(f"$cia%x,")
      writer.write(f"$cr%x,")
      for (i <- 0 until 32) {
        val reg = dut.cpu.gpr.mem.getBigInt(i)
        if (i == 31)
          writer.write(f"$reg%x\n")
        else
          writer.write(f"$reg%x,")
      }
      writer.flush()
      if(iteration >= 100){
        finish()
      }
      iteration += 1
    }
  }
  def finish() {
    writer.close()
    simSuccess()
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
  val compiled = SimConfig.withWave.compile(new SoC)
  val testDir = "c_sources/tests"
  val binary = "test_le.bin"
  val csvOutputDir = new File("test_csv_output")
  val goldCsvDir = "c_sources/tests"
  csvOutputDir.mkdir()

  def compareCsvs(goldCsvFile: String, testCsvFile: String){
    println(s"compare: $goldCsvFile with $testCsvFile")
    val goldCsvLines = Source.fromFile(goldCsvFile).getLines()
    val testCsvLines = Source.fromFile(testCsvFile).getLines()
    for((gold, test) <- goldCsvLines zip testCsvLines){
      if(gold != test){
        Console.println(s"${RESET}${RED}Found difference:")
        Console.println(s"    $gold")
        Console.println(s"    $test${RESET}")
        fail()
      }
    }

  }

  def runTest(testName: String) {
    val testCsv = s"$csvOutputDir/$testName.csv"
    val goldCsv = s"$goldCsvDir/$testName/test.csv"
    it should s"run $testName" in {
      compiled.doSimUntilVoid(testName) { dut =>
        dut.ram.loadFromFile(s"$testDir/$testName/$binary")
        val logger = new CSVLogger(dut, testCsv)
        dut.clockDomain.forkStimulus(10)
      }
      compareCsvs(goldCsv, testCsv)
    }
  }
  val testDirFile = new File(testDir)
  val tests = testDirFile.listFiles().filter(_.isDirectory)
  for(file <- tests){ 
    val testName = file.getName()
    runTest(testName)
  }


}
