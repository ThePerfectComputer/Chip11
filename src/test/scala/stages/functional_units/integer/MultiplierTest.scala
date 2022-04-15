package cpu.stages.functional_units.integer


import spinal.core._
import spinal.core.Formal._
//import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._
import scala.util.Random

class MultDUT(wid: Int) extends Component {
  val io = new Bundle {
    val a = in UInt(wid bits)
    val b = in UInt(wid bits)

    val equiv = out Bool
  }

  val gold = new Mult(wid)
  val dut = new MultB(wid)

  gold.io.a <> io.a
  gold.io.b <> io.b
  dut.io.a <> io.a
  dut.io.b <> io.b

  io.equiv := dut.io.o === gold.io.o

  assert(io.equiv)

}

class MultiplierProofVerilogFormal extends AnyFlatSpec with should.Matchers {
  behavior of "MultDUT"

  it should "create verilog" in {
    val config = SpinalConfig(defaultConfigForClockDomains=ClockDomainConfig(resetKind = SYNC, resetActiveLevel = HIGH),
      targetDirectory="formal"
    ).withoutEnumString()
    config.includeFormal.generateSystemVerilog(new MultDUT(16))
  }
}
