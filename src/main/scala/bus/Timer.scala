package bus
import spinal.core._
import spinal.lib._
import spinal.lib.bus.misc.BusSlaveFactory
import spinal.lib.bus.amba4.axi.{Axi4, Axi4SlaveFactory, Axi4Config}
import spinal.lib.bus._

class Timer(width: Int, baseAddress: Int) extends Component {
  val io = new Bundle {
    val bus = slave(Axi4(Axi4Ctrl.getAxi4Config))
    val timerMatch = out Bool
  }

  val busCtrl = new Axi4SlaveFactory(io.bus)

  val timerCompare = Reg(UInt(width bits)) init(0)
  val counter = Reg(UInt(width bits)) init(0)

  counter := counter + 1
  io.timerMatch := False

  when(counter === (timerCompare - 1)) {
    counter := 0
    io.timerMatch := True
  }

  // This writes to the register when the bus says to
  busCtrl.readAndWrite(counter, baseAddress + 0)
  // This creates a register that's assigned by the bus
  busCtrl.driveAndRead(timerCompare, baseAddress+4)

}

