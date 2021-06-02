package cpu.stages

import cpu.interfaces.{
  FetchOutput,
  BPFetchRequestInterface,
  LineRequest,
  LineResponse
}
import soc.devices.memory_adaptor.{MemoryAdaptor, MemBusToAXIShared}

import util._
import spinal.core._
import spinal.lib._
import spinal.sim._
import spinal.lib.bus.amba4.axi.{
  Axi4Config,
  Axi4CrossbarFactory,
  Axi4SharedOnChipRam
}

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._

import scala.collection.mutable.ListBuffer
import scala.util.Random

class FetchDUT extends Component {
  val io = new Bundle {
    val bp_request = slave(new BPFetchRequestInterface())

    val pipeOutput = master(Flushable(new FetchOutput))
  }
  implicit val axiConfig = Axi4Config(
    addressWidth = 64,
    dataWidth = 128,
    idWidth = 4,
    useId = true,
    useRegion = false,
    useBurst = true,
    useLock = false,
    useCache = false,
    useSize = true,
    useQos = false,
    useLen = true,
    useLast = true,
    useResp = true,
    useProt = false,
    useStrb = true
  )

  val fetch = new FetchUnit
  io.pipeOutput <> fetch.pipeOutput
  io.bp_request <> fetch.io.bp_request

  val fetch_adaptor = new MemoryAdaptor
  fetch.io.line_request <> fetch_adaptor.io.request
  fetch.io.line_response <> fetch_adaptor.io.response

  val mbToAxi1 = new MemBusToAXIShared(0)
  mbToAxi1.io.membus <> fetch_adaptor.io.membus

  val ramSize = 128

  val ram = Axi4SharedOnChipRam(
    dataWidth = axiConfig.dataWidth,
    byteCount = ramSize,
    idWidth = 5
  )

  var initialData = new ListBuffer[BigInt]
  for (idx <- 0 until (ramSize / 16)) {
    var item = BigInt(0)
    for (i <- 0 until 4) {
      val word = BigInt(idx * 4 + i)
      item |= word << (32 * i)
    }
    initialData += item
  }
  ram.ram.init(initialData.map(B(_, 128 bits)))

  val axiCrossbar = Axi4CrossbarFactory()

  axiCrossbar.addSlaves(
    ram.io.axi -> (0x0, ramSize)
  )

  // I think this is defining what masters can access which slaves
  axiCrossbar.addConnections(
    mbToAxi1.io.axi -> List(ram.io.axi)
  )

  axiCrossbar.build()
}

class FetchTest extends AnyFlatSpec with should.Matchers {
  behavior of "FetchUnit"
  val r = new Random

  it should "fetch instructions" in {
    SimConfig.withWave.doSim(new FetchDUT) { dut =>
      dut.io.pipeOutput.ready #= true
      dut.io.pipeOutput.flush #= false
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.waitSampling(1)

      for (i <- 16 until 100 by 4) {
        while (!dut.io.pipeOutput.valid.toBoolean) {
          dut.clockDomain.waitSampling(1)
        }
        val cia = dut.io.pipeOutput.payload.cia.toBigInt
        val insn = dut.io.pipeOutput.payload.insn.toBigInt
        assert(cia === i)
        assert(insn === i/4)
        dut.clockDomain.waitSampling(1)
      }
    }
  }
  it should "branch" in {
    SimConfig.withWave.doSim(new FetchDUT) { dut =>
      dut.io.pipeOutput.ready #= true
      dut.io.pipeOutput.flush #= false
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.waitSampling(1)

      for (i <- 16 until 32 by 4) {
        while (!dut.io.pipeOutput.valid.toBoolean) {
          dut.clockDomain.waitSampling(1)
        }
        val cia = dut.io.pipeOutput.payload.cia.toBigInt
        val insn = dut.io.pipeOutput.payload.insn.toBigInt
        assert(cia === i)
        assert(insn === i/4)
        dut.clockDomain.waitSampling(1)
      }
      
      //dut.clockDomain.waitSampling(r.nextInt(10))
      dut.clockDomain.waitSampling(1)
      dut.io.bp_request.branch.valid #= true
      dut.io.bp_request.branch.payload #= 0x4
      dut.clockDomain.waitSampling(1)
      dut.io.bp_request.branch.valid #= false
      for (i <- 4 until 32 by 4) {
        while (!dut.io.pipeOutput.valid.toBoolean) {
          dut.clockDomain.waitSampling(1)
        }
        val cia = dut.io.pipeOutput.payload.cia.toBigInt
        val insn = dut.io.pipeOutput.payload.insn.toBigInt
        assert(cia === i)
        assert(insn === i/4)
        dut.clockDomain.waitSampling(1)
      }
    }
  }
}
