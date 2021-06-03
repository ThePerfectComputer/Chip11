package cpu.shared

import cpu.interfaces.regfile.{ReadPort, WritePort, WritePortMasked}

import spinal.core._
import spinal.core.Formal._
import spinal.lib.{master, slave}
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._
import scala.util.Random


class BRAMMultiProofVerilogFormal extends AnyFlatSpec with should.Matchers {
  behavior of "BRAMMultiRegfile"

  it should "create verilog" in {
    val readPorts = 2

  val writePorts = 2
  val numRegs = 4
  val regWidth = 4
    val config = SpinalConfig(
      defaultConfigForClockDomains =
        ClockDomainConfig(resetKind = SYNC, resetActiveLevel = HIGH),
      targetDirectory = "formal"
    ).withoutEnumString()
    config.includeFormal.generateSystemVerilog(new Regfile(numRegs, regWidth, readPorts, writePorts))
    config.includeFormal.generateSystemVerilog(new BRAMMultiRegfile(numRegs, regWidth, readPorts, writePorts))
  }
}
