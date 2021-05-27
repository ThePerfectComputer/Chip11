package soc

import spinal.core._
import spinal.lib.{slave, master}
import spinal.sim._
import cpu.{CPU, CPUConfig}
import cpu.shared.memory_state.{
  TransactionStatus,
  TransactionType,
  TransactionSize
}
import cpu.interfaces.{LineRequest, LineResponse}

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._

import java.io._
import scala.io.Source
import scala.Console
import Console.{RED, RESET, YELLOW}

class CSVLogger(dut: SoC, filePath: String) {
  val writer = new PrintWriter(new File(filePath))
  val maxIter = 2000
  writer.write(
    "cia,cr,r0,r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17,r18,r19,r20,r21,r22,r23,r24,r25,r26,r27,r28,r29,r30,r31\n"
  )
  def getCr: BigInt = {
    val cra = dut.cpu.cr.mem.getBigInt(0)
    val crb = dut.cpu.cr.mem.getBigInt(1)
    var result = BigInt(0)
    for (i <- 0 until 4) {
      val mask = 0xf << (i * 4)
      val cra_field = (cra & mask) >> (i * 4)
      val crb_field = (crb & mask) >> (i * 4)
      result =
        result | (cra_field << ((i * 2 + 1) * 4)) | (crb_field << (i * 2 * 4))
    }
    result
  }
  var iteration = 0
  dut.clockDomain.onSamplings {

    if (dut.cpu.write.pipeOutput.valid.toBoolean) {
      val cia = dut.cpu.write.pipeOutput.payload.toBigInt
      val cr = getCr
      val r31 = dut.cpu.gpr.mem.getBigInt(31)
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
      if (iteration >= maxIter || r31 == 1) {
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

class CPUShiftRegDUT(implicit val config: CPUConfig) extends Component {
  val io = new Bundle {
    val en = in Bool
    val tdi = in Bool
    val tdo = out Bool
    val tck = in Bool
    val sample = in Bool
  }
  val cpu = new CPU
  class ResponseBundle extends Bundle {
    val fetch_response = new LineResponse
    val ldst_response = new LineResponse
  }

  val inputs = new ResponseBundle
  val outputs = Cat(
    cpu.io.fetch_request.data,
    cpu.io.fetch_request.byte_address,
    cpu.io.fetch_request.size,
    cpu.io.fetch_request.ldst_req,
    cpu.io.ldst_request.data,
    cpu.io.ldst_request.byte_address,
    cpu.io.ldst_request.size,
    cpu.io.ldst_request.ldst_req
  )

  val input_reg = Reg(Bits(inputs.getBitsWidth bits))
  val output_reg = Reg(Bits(outputs.getWidth bits))
  inputs.assignFromBits(input_reg)
  cpu.io.fetch_response := inputs.fetch_response
  cpu.io.ldst_response := inputs.ldst_response

  io.tdo := output_reg(0)

  when(io.tck) {
    input_reg := Cat(io.tdi, input_reg(input_reg.getWidth - 1 downto 1))
    output_reg := Cat(U(0, 1 bits), output_reg(output_reg.getWidth - 1 downto 1))
  }
  when(io.sample) {
    output_reg := outputs
  }
}

class SoCTestVerilog extends AnyFlatSpec with should.Matchers {
  behavior of "SoC"

  it should "create verilog" in {
    implicit val config = new CPUConfig(
      adder = true,
      branch = false,
      logical = false,
      shifter = false,
      comparator = false,
      multiplier = false,
      zcnt = false,
      popcnt = false
    )
    SpinalVerilog(new CPU)
    SpinalVerilog(new CPUShiftRegDUT)
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

  def examineDifference(
      goldLine: String,
      testLine: String,
      headerLine: String
  ) {
    val headerIter = headerLine.split(',')
    val goldIter = goldLine.split(',')
    val testIter = testLine.split(',')
    val data =
      (goldIter zip testIter zip headerIter).map(x => (x._1._1, x._1._2, x._2))
    for ((gold, test, header) <- data) {
      if (gold != test) {
        Console.println(
          s"${RESET}${YELLOW} Difference in $header - expected $gold, found $test${RESET}"
        )
      }
    }

  }

  def compareCsvs(goldCsvFile: String, testCsvFile: String) {
    println(s"compare: $goldCsvFile with $testCsvFile")
    val goldCsvLines = Source.fromFile(goldCsvFile).getLines().toList
    val testCsvLines = Source.fromFile(testCsvFile).getLines().toList
    val csvHeader = goldCsvLines(0)
    for ((gold, test) <- goldCsvLines zip testCsvLines) {
      if (gold != test) {
        Console.println(s"${RESET}${RED}Found difference:")
        Console.println(s"    $gold")
        Console.println(s"    $test${RESET}")
        examineDifference(gold, test, csvHeader)
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
  for (file <- tests) {
    val testName = file.getName()
    runTest(testName)
  }

}
