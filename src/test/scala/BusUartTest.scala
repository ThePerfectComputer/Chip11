package bus
import core.CommonSimConfig
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._
import spinal.lib.bus.amba4.axi.sim._
import spinal.lib.com.uart._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._

object BusUartControl {
  def getGenerics = UartCtrlGenerics(
    dataWidthMax=8)
}

class BusUARTTests extends AnyFlatSpec with should.Matchers{
  behavior of "BusUART"

  it should "do a thing" in {
    CommonSimConfig().withWave.compile(new BusUART(BusUartControl.getGenerics, 8)).doSim { dut =>
      dut.clockDomain.forkStimulus(10)

      val driver = Axi4Driver(dut.io.bus, dut.clockDomain)

      driver.reset()
      dut.clockDomain.waitSampling(5)

      dut.io.uart.rxd #= true

      driver.write(0, 0x10)
      driver.write(4, 7)
      dut.clockDomain.waitSampling(100)
      driver.write(8, 0x55)
      driver.write(8, 0x0f)
      driver.write(8, 0xf0)
      dut.clockDomain.waitSampling(100)
      var resp = driver.read(8)
      while((resp & 1) != 0){
        dut.clockDomain.waitSampling(50)
        resp = driver.read(8)
        println(resp)
      }

    }
  }
}

