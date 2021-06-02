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
import scala.collection.mutable._

object BusUartControl {
  def getGenerics = UartCtrlGenerics(
    dataWidthMax=8)
}
class BusUARTLoopback(uartCtrlConfig: UartCtrlGenerics, rxFifoDepth: Int) extends Component {
  implicit val config = Axi4Ctrl.getAxi4Config
  val io = new Bundle {
    val bus = slave(Axi4(config))
  }
  val uart = new BusUART(uartCtrlConfig, rxFifoDepth)

  uart.io.uart.rxd := uart.io.uart.txd
  uart.io.bus <> io.bus

}

class BusUARTTests extends AnyFlatSpec with should.Matchers{
  behavior of "BusUART"

  implicit val config = Axi4Ctrl.getAxi4Config
  it should "do a thing" in {
    CommonSimConfig().withWave.compile(new BusUART(BusUartControl.getGenerics, 8)).doSim { dut =>
      dut.clockDomain.forkStimulus(10)

      val driver = Axi4Driver(dut.io.bus, dut.clockDomain)

      driver.reset()
      dut.clockDomain.waitSampling(5)

      dut.io.uart.rxd #= true

      driver.write(0, 0x10)
      driver.write(16, 7)
      dut.clockDomain.waitSampling(100)
      driver.write(32, 0x55)
      driver.write(32, 0x0f)
      driver.write(32, 0xf0)
      dut.clockDomain.waitSampling(100)
      var resp = driver.read(32)
      while((resp & 1) != 0){
        dut.clockDomain.waitSampling(50)
        resp = driver.read(32)
      }

    }
  }
  it should "support loopback" in {
    CommonSimConfig().withWave.compile(new BusUARTLoopback(BusUartControl.getGenerics, 8)).doSim { dut =>
      SimTimeout(1000000)
      dut.clockDomain.forkStimulus(10)

      val testdata = "Hello!" // must be less than the fifo depth

      val driver = Axi4Driver(dut.io.bus, dut.clockDomain)

      def fifoWrite(data:Int){
        var resp = driver.read(32).toInt
        while((resp & 2) == 0){
          dut.clockDomain.waitSampling(50)
          resp = driver.read(32).toInt
        }
        driver.write(32, data & 255)
      }

      driver.reset()
      dut.clockDomain.waitSampling(5)

      driver.write(0, 0x10)
      driver.write(16, 7)
      dut.clockDomain.waitSampling(100)
      for(c <- testdata){
        fifoWrite(c)
      }
      dut.clockDomain.waitSampling(100)
      var resp = driver.read(32)
      while((resp & 1) != 0){
        dut.clockDomain.waitSampling(50)
        resp = driver.read(32)
      }

      resp = driver.read(24).toInt
      var received = ListBuffer[Char]()
      while((resp & (1<<31)) != 0){
        println(f"resp: 0x${resp & 0xff}%x ${(resp & 0xff).charValue}%c")
        received += (resp & 0xff).charValue
        dut.clockDomain.waitSampling(10)
        resp = driver.read(24)
      }

      for((expected, actual) <- testdata.zip(received)){
        assert(expected == actual)
      }
    }
  }
}

