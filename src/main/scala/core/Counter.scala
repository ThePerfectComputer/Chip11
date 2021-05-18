package core
import spinal.core._
import spinal.lib._

class Counter extends Component {
  val width = 8
  val io = new Bundle {
    val count = out UInt(width bits)

  }
  val counter = Reg(UInt(width bits)) init(0)

  counter := counter + 1
  io.count := counter

}

object CounterVerilog {
  def main(args: Array[String]) {
    SpinalVerilog(new Counter)
  }
}
