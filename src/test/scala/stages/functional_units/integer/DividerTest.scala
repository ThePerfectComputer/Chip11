package cpu.stages.functional_units.integer

import spinal.core._
//import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._
import scala.util.Random

class DividerTest extends AnyFlatSpec with should.Matchers {
  behavior of "Divider"

  it should "divide shit" in {
    SimConfig.withWave.doSim(new Divider(64)) { dut =>
      dut.io.input_valid #= false
      dut.clockDomain.forkStimulus(10)

      dut.clockDomain.waitSampling(5)

      val r = new Random

      for (i <- 0 until 10) {

        val a = BigInt(31, r)
        val b = BigInt(31, r)
        val c = a * b

        println(f"${a}%x * ${b}%x = ${c}%x")


        dut.io.a #= c
        dut.io.b #= b
        dut.io.is_word #= false
        dut.io.is_unsigned #= true
        dut.io.shift_a #= false
        dut.io.input_valid #= true
        dut.clockDomain.waitSampling(1)
        dut.io.input_valid #= false
        sleep(1)
        while (!dut.io.output_valid.toBoolean) {
          dut.clockDomain.waitSampling(1)
        }
        assert(dut.io.o.toBigInt == a)
      }

    }
  }
}
