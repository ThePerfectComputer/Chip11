package bus
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb._
import spinal.lib.bus.amba3.apb.sim.Apb3Driver

import spinal.lib.bus.amba4.axi.{Axi4, Axi4SlaveFactory, Axi4Config, Axi4CrossbarFactory}

import spinal.core.sim._

object Apb3Ctrl {
  def getApb3Config = Apb3Config(
    addressWidth = 16,
    dataWidth = 32
  )
}

class BusTimer(width: Int)(implicit config: Axi4Config) extends Component {
  val io = new Bundle {
    val bus = slave(Axi4(config))
    val timerAMatch = out Bool
    val timerBMatch = out Bool
  }


  val timerA = new Timer(width, 0x40)
  io.timerAMatch := timerA.io.timerMatch

  val timerB = new Timer(width, 0x50)
  io.timerBMatch := timerB.io.timerMatch

  val axiCrossbar = Axi4CrossbarFactory()

  axiCrossbar.addSlaves(
    timerA.io.bus -> (0x40, 0x10),
    timerB.io.bus -> (0x50, 0x10)
  )

  // I think this is defining what masters can access which slaves
  axiCrossbar.addConnections(
    io.bus -> List(timerA.io.bus, timerB.io.bus)
  )

  axiCrossbar.build()

}

