package cpu.shared

import spinal.core._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._
import scala.util.Random
import scala.collection.mutable._

trait RegfileBehaviors {
  this: AnyFlatSpec =>

  def readAndWrite(regfile: => Regfile) {
    it should "Support reads and writes" in {
      SimConfig.withWave.doSim(regfile) { dut =>
        val numRegs = regfile.numRegs
        val regBits = regfile.regWidth
        val readPorts = regfile.readPorts
        val writePorts = regfile.writePorts
        val r = new Random
        val data = Seq.tabulate(numRegs)(_ => r.nextInt(256))
        dut.clockDomain.forkStimulus(10)
        dut.io.wp(0).en #= false
        dut.clockDomain.waitSampling(1)

        for ((item, idx) <- data.zipWithIndex) {
          dut.io.wp(0).data #= item
          dut.io.wp(0).idx #= idx
          dut.io.wp(0).en #= true
          dut.clockDomain.waitSampling(1)
        }
        dut.io.wp(0).en #= false

        dut.clockDomain.waitSampling(1)
        for ((item, idx) <- data.zipWithIndex) {
          dut.io.rp(0).idx #= idx
          dut.clockDomain.waitSampling(1)
          sleep(1) // WHY IS THIS NECESSARY???
          val result = dut.io.rp(0).data.toBigInt
          assert(result == item)
        }
        dut.clockDomain.waitSampling(1)
      }
    }
  }

  def readMultiPort(regfile: => Regfile) {
    it should "Support multiport reads" in {
      SimConfig.withWave.doSim(regfile) { dut =>
        val numRegs = regfile.numRegs
        val regBits = regfile.regWidth
        val readPorts = regfile.readPorts
        val writePorts = regfile.writePorts
        val r = new Random
        val data = Seq.tabulate(numRegs)(_ => r.nextInt(256))
        dut.clockDomain.forkStimulus(10)
        dut.io.wp(0).en #= false
        dut.clockDomain.waitSampling(1)

        for ((item, idx) <- data.zipWithIndex) {
          dut.io.wp(0).data #= item
          dut.io.wp(0).idx #= idx
          dut.io.wp(0).en #= true
          dut.clockDomain.waitSampling(1)
        }
        dut.io.wp(0).en #= false

        dut.clockDomain.waitSampling(1)
        for ((item, idx) <- data.zipWithIndex) {
          for (rp <- dut.io.rp) {
            rp.idx #= idx
          }
          dut.clockDomain.waitSampling(1)
          sleep(1) // WHY IS THIS NECESSARY???
          for (rp <- dut.io.rp) {
            val result = rp.data.toBigInt
            assert(result == item)
          }
        }
        dut.clockDomain.waitSampling(1)
      }
    }
  }
  def writeMultiPort(regfile: => Regfile) {
    it should "Support multiport writes" in {
      SimConfig.withWave.doSim(regfile) { dut =>
        val numRegs = regfile.numRegs
        val regBits = regfile.regWidth
        val readPorts = regfile.readPorts
        val writePorts = regfile.writePorts
        val r = new Random
        val data = Seq.tabulate(numRegs)(_ => r.nextInt(256)).zipWithIndex
        // val data = Seq.tabulate(numRegs)(x => x).zipWithIndex

        val itemsPerPort = (numRegs + writePorts - 1) / writePorts
        val dataGroups = data.grouped(itemsPerPort).toVector

        dut.clockDomain.forkStimulus(10)

        var forkList = ListBuffer[SimThread]()
        for ((wp, group) <- dut.io.wp.zip(dataGroups)) {
          val thread = fork {
            wp.en #= false

            dut.clockDomain.waitSampling(4)
            for ((data, idx) <- group) {
              wp.idx #= idx
              wp.data #= data
              wp.en #= true
              dut.clockDomain.waitSampling(1)
            }
            wp.en #= false
            dut.clockDomain.waitSampling(5)
          }
          forkList += thread
        }
        forkList.map(x => x.join())

        dut.clockDomain.waitSampling(1)
        for ((item, idx) <- data) {
          dut.io.rp(0).idx #= idx
          dut.clockDomain.waitSampling(1)
          sleep(1) // WHY IS THIS NECESSARY???
          val result = dut.io.rp(0).data.toBigInt
          assert(result == item)
        }
        dut.clockDomain.waitSampling(1)
      }
    }
  }

}

class RegfileTest
    extends AnyFlatSpec
    with should.Matchers
    with RegfileBehaviors {
  behavior of "Regfile"

  it should behave like readAndWrite(new Regfile(32, 32, 1, 1))
  it should behave like readMultiPort(
    new Regfile(32, 32, 3, 1)
  )
  it should behave like writeMultiPort(
    new Regfile(32, 32, 1, 2)
  )

}

class BRAMMultiRegfileTest
    extends AnyFlatSpec
    with should.Matchers
    with RegfileBehaviors {
  behavior of "BRAMMultiRegfile"

  it should behave like readAndWrite(new BRAMMultiRegfile(32, 32, 1, 1))
  it should behave like readMultiPort(
    new BRAMMultiRegfile(32, 32, 3, 1)
  )
  it should behave like writeMultiPort(
    new BRAMMultiRegfile(32, 32, 1, 2)
  )

}

class RegfileTestVerilog extends AnyFlatSpec with should.Matchers {
  behavior of "uOpAndFormDecoder"

  it should "create verilog" in {
    val config =
      SpinalConfig(mode = Verilog, mergeAsyncProcess = true).withoutEnumString()
    config.generate(new Regfile(32, 32, 1, 1))
    config.generate(new BRAMMultiRegfile(32, 32, 3, 3))
  }
}
