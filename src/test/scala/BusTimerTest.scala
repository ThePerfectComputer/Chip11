package bus

import core.CommonSimConfig
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._
import spinal.lib.bus.amba4.axi.sim._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._

class BusTimerTests extends AnyFlatSpec with should.Matchers{
  behavior of "BusTimer"

  it should "do a thing" in {
    CommonSimConfig().withWave.compile(new BusTimer(16)).doSim { dut =>
      dut.clockDomain.forkStimulus(10)

      val driver = Axi4Driver(dut.io.bus, dut.clockDomain)

      driver.reset()
      dut.clockDomain.waitSampling(5)
      driver.write(0x44, 0x5)
      driver.writeBurst(0x40, List(65530, 8))
      dut.clockDomain.waitSampling(10)
      val ret2 = driver.readBurst(0x40, 2)
      print(ret2)
      dut.clockDomain.waitSampling(2)

      driver.writeBurst(0x50, List(0, 0xa))
      dut.clockDomain.waitSampling(30)

      dut.clockDomain.waitSampling(5)
      driver.write(0x44, 0xd)
      dut.clockDomain.waitSampling(30)
      driver.write(0x50, 0)
    }
  }
}

// object BusTimerTests {
//   def main(args: Array[String]): Unit = {
//     CommonSimConfig().withWave.compile(new BusTimer(16)).doSim{ dut =>
//       dut.clockDomain.forkStimulus(10)

//       val driver = Apb3Driver(dut.io.bus, dut.clockDomain)


//       dut.clockDomain.waitSampling(5)
//       driver.write(0x44, 0x5)
//       dut.clockDomain.waitSampling(1)
//       driver.read(0x40)
//       dut.clockDomain.waitSampling(30)

//       dut.clockDomain.waitSampling(5)
//       driver.write(0x44, 0xd)
//       dut.clockDomain.waitSampling(30)
//     }
//   }
// }
