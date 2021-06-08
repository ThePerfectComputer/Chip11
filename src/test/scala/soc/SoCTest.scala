package soc

import spinal.core._
import spinal.lib.{slave, master, Stream, Flow}
import spinal.lib.com.uart._
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
import scala.collection.mutable._
import Console.{RED, RESET, YELLOW}

class CSVLogger(dut: SoCGen, filePath: String) {
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
    val fetch_ack = Bool
    val ldst_ack = Bool
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
  cpu.io.ldst_request.ack := inputs.ldst_ack
  cpu.io.fetch_request.ack := inputs.fetch_ack

  io.tdo := output_reg(0)

  when(io.tck) {
    input_reg := Cat(io.tdi, input_reg(input_reg.getWidth - 1 downto 1))
    output_reg := Cat(
      U(0, 1 bits),
      output_reg(output_reg.getWidth - 1 downto 1)
    )
  }
  when(io.sample) {
    output_reg := outputs
  }
}

class SoCTestVerilog extends AnyFlatSpec with should.Matchers {
  behavior of "SoC"

  it should "create verilog" in {
    implicit val config = new CPUConfig(
      adder = false,
      branch = true,
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
        SimTimeout(100000)
        dut.loadFromFile(s"$testDir/$testName/$binary")
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

class SoCUARTWrapper extends Component {
  val io = new Bundle {
    val write = slave(Stream(Bits(8 bits)))
    val read = master(Stream(Bits(8 bits)))
  }

  val soc = new SoCWithUART(memorySize=(16 * 1024 * 1024))

  val uartCtrl = new UartCtrl(soc.uartGenerics)
  soc.io.tx <> uartCtrl.io.uart.rxd
  soc.io.rx <> uartCtrl.io.uart.txd

  io.read <> uartCtrl.io.read
  io.write <> uartCtrl.io.write

  uartCtrl.io.config.clockDivider := 0x1
  uartCtrl.io.config.frame.dataLength := 7
  uartCtrl.io.config.frame.stop.assignFromBits(0)
  uartCtrl.io.config.frame.parity.assignFromBits(0)
  uartCtrl.io.writeBreak := False
}

class SoCWithUARTTest extends AnyFlatSpec with should.Matchers {
  val dir = "c_sources/uart"
  val compiled = SimConfig.withWave.compile(new SoCUARTWrapper)

  it should "print a string" in {
    compiled.doSim("print") { dut =>
      val binary = s"$dir/hello.bin"
      dut.io.read.ready #= true
      dut.io.write.valid #= false
      dut.soc.loadFromFile(binary)
      dut.clockDomain.forkStimulus(10)
      var received = new StringBuilder()
      dut.clockDomain.onSamplings {
        if (dut.io.read.valid.toBoolean) {
          val char = dut.io.read.payload.toInt
          println(f"resp: 0x${char & 0xff}%x ${(char & 0xff)}%c")
          received += (char & 0xff).toChar
        }
      }

      dut.clockDomain.waitSampling(10000)
      assert(received.toString() == "HELLO!")
    }
  }
  it should "run c_test" in {
    compiled.doSim("c_test") { dut =>
      val binary = s"c_sources/c_test/test.bin"
      val csv = "test_csv_output/c_test.csv"
      val testdata = "test1234"
      dut.io.read.ready #= true
      dut.io.write.valid #= false
      dut.soc.loadFromFile(binary)
      val logger = new CSVLogger(dut.soc, csv)
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.onSamplings {
        if (dut.io.read.valid.toBoolean) {
          val char = dut.io.read.payload.toInt
          println(f"resp: 0x${char & 0xff}%x ${(char & 0xff)}%c")
        }
      }
      fork {
        dut.clockDomain.waitSampling(1000)
        for(c <- testdata){
          dut.io.write.valid #= true
          dut.io.write.payload #= c
          while(!dut.io.write.ready.toBoolean){
            dut.clockDomain.waitSampling(1)
          }
          dut.io.write.valid #= false
          dut.clockDomain.waitSampling(1)
        }
      }

      dut.clockDomain.waitSampling(10000)
    }
  }

  it should "echo data" in {
    compiled.doSim("echo") { dut =>
      val binary = s"$dir/echo.bin"
      val testdata = "TEST"
      dut.io.read.ready #= true
      dut.io.write.valid #= false
      dut.soc.loadFromFile(binary)
      dut.clockDomain.forkStimulus(10)
      var received = new StringBuilder()
      dut.clockDomain.onSamplings {
        if (dut.io.read.valid.toBoolean) {
          val char = dut.io.read.payload.toInt
          println(f"resp: 0x${char & 0xff}%x ${(char & 0xff)}%c")
          received += char.toChar
        }
      }
      fork {
        dut.clockDomain.waitSampling(100)
        for(c <- testdata){
          dut.io.write.valid #= true
          dut.io.write.payload #= c
          while(!dut.io.write.ready.toBoolean){
            dut.clockDomain.waitSampling(1)
          }
          dut.io.write.valid #= false
          dut.clockDomain.waitSampling(1)
        }
      }

      dut.clockDomain.waitSampling(10000)
      assert(received.toString() == testdata)
    }
  }
  it should "run micropython" in {
    compiled.doSim("upython") { dut =>
      val binary = s"c_sources/upython/firmware.bin"
      val csv = "test_csv_output/upython.csv"
      dut.io.read.ready #= true
      dut.io.write.valid #= false
      dut.soc.loadFromFile(binary)
      val logger = new CSVLogger(dut.soc, csv)
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.onSamplings {
        if (dut.io.read.valid.toBoolean) {
          val char = dut.io.read.payload.toInt
          println(f"resp: 0x${char & 0xff}%x ${(char & 0xff)}%c")
        }
      }

      dut.clockDomain.waitSampling(1000000)
    }
  }

}

class SoCWithUARTVerilog extends AnyFlatSpec with should.Matchers {
  it should "do verilog" in {
    SpinalVerilog(new SoCWithUART)

  }
}
