package cpu.stages

import cpu.interfaces.{FetchOutput, BPFetchRequestInterface, LineRequest, LineResponse}
import soc.devices.memory_adaptor.{MemoryAdaptor, MemBusToAXIShared}

import util._
import spinal.core._
import spinal.lib._
import spinal.sim._
import spinal.lib.bus.amba4.axi.{Axi4Config, Axi4CrossbarFactory, Axi4SharedOnChipRam}


import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._


class FetchDUT extends Component {
  val io = new Bundle {
    val bp_interface = slave(new BPFetchRequestInterface())

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
  io.bp_interface <> fetch.io.bp_interface


  val fetch_adaptor = new MemoryAdaptor
  fetch.io.line_request <> fetch_adaptor.io.request
  fetch.io.line_response <> fetch_adaptor.io.response

  val mbToAxi1 = new MemBusToAXIShared(0)
  mbToAxi1.io.membus <> fetch_adaptor.io.membus

  val ramSize = 32

  val ram = Axi4SharedOnChipRam (
    dataWidth = axiConfig.dataWidth,
    byteCount = ramSize,
    idWidth = 5
  )

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

  it should "fetch instructions" in {
    SimConfig.withWave.doSim(new FetchDUT) { dut =>
      dut.clockDomain.forkStimulus(10)

      dut.clockDomain.waitSampling(100)
    }
  }
}

