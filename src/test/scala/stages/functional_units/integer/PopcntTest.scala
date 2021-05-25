package cpu.stages.functional_units.integer

import spinal.core._
//import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._
import scala.util.Random

class PopcntTest extends AnyFlatSpec with should.Matchers {
  behavior of "Popcount"

  it should "count set bits" in {
    SimConfig.withWave.doSim(new PopcntB) { dut =>
      for(i <- 0 to 64){
        val test = (BigInt(1) << i) - 1
        dut.io.data #= test
        sleep(1)
        assert(dut.io.count.toInt == i)
        sleep(10)
      }
    }
  }
}
