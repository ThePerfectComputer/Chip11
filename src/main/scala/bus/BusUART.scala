package bus
import spinal.core._
import spinal.lib._
import spinal.lib.com.uart._
import scala.math.BigInt

import spinal.lib.bus.amba4.axi.{Axi4, Axi4SlaveFactory, Axi4Config, Axi4CrossbarFactory}

import spinal.core.sim._

class BusUART(uartCtrlConfig: UartCtrlGenerics, rxFifoDepth: Int) extends Component {
  val io = new Bundle {
    val bus = slave(Axi4(Axi4Ctrl.getAxi4Config))
    val uart = master(Uart())
  }


  val uartCtrl = new UartCtrl(uartCtrlConfig)
  io.uart <> uartCtrl.io.uart
  uartCtrl.io.writeBreak := False

  val busCtrl = new Axi4SlaveFactory(io.bus)

  busCtrl.driveAndRead(uartCtrl.io.config.clockDivider, address=0) init(0)
  busCtrl.driveAndRead(uartCtrl.io.config.frame, address=4) 

  val txFifo = StreamFifo(Bits(uartCtrlConfig.dataWidthMax bits), rxFifoDepth)
  txFifo.io.pop >/> uartCtrl.io.write

  busCtrl.createAndDriveFlow(Bits(uartCtrlConfig.dataWidthMax bits), address = 8).toStream >> txFifo.io.push

  val stream : Stream[Bits] = uartCtrl.io.read.queue(rxFifoDepth)
  busCtrl.readStreamNonBlocking(stream, address=12, validBitOffset=31, payloadBitOffset=0)

  busCtrl.read(uartCtrl.io.write.valid, address=8)
  busCtrl.read(txFifo.io.push.ready, address=8, bitOffset=1)
  busCtrl.read(stream.valid, address=8, bitOffset=2)
  busCtrl.read(txFifo.io.occupancy, address=8, bitOffset=3)


  // axiCrossbar.addSlaves(
  //   timerA.io.bus -> (0x40, 0x10),
  //   timerB.io.bus -> (0x50, 0x10)
  // )

  // // I think this is defining what masters can access which slaves
  // axiCrossbar.addConnections(
  //   io.bus -> List(timerA.io.bus, timerB.io.bus)
  // )

  // axiCrossbar.build()

}
