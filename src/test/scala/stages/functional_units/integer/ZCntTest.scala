package cpu.stages.functional_units.integer

import spinal.core._
//import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._
import scala.util.Random

class LZCntTest extends AnyFlatSpec with should.Matchers {
  behavior of "Stage1"

  it should "add shit" in {
    SimConfig.withWave.doSim(new LZCnt(64)) { dut =>
      for(i <- 0 to 64){
        val test = (BigInt(1) << i) - 1
        dut.io.data_in #= test
        sleep(1)
        assert(dut.io.count.toInt == 64 - i)
        sleep(10)
      }
    }
  }
}
