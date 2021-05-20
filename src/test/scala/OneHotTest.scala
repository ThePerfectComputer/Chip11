package util

import spinal.core._
import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._


class OneHotDUT(items: Int, width: Int) extends Component {
  val io = new Bundle {
    val in_oh = in Bits(items bits)
    val out_decoded = out UInt(width bits)
  }

  val decodeItems = Seq.tabulate(items)(i => U((i+1)*(i+1), width bits))
  io.out_decoded := Mux1H(io.in_oh, decodeItems).resized
}

object TestEnum extends SpinalEnum {
  val e0, e1, e2, e3, e4, e5, e6, e7 = newElement()
  val e8, e9, e10, e11, e12, e13, e14, e15 = newElement()
}

class OneHotEnum(width: Int) extends Component {
  val io = new Bundle {
    val in_oh = in(TestEnum(binaryOneHot))
    val out_decoded = out UInt(width bits)
  }

  val decodeItems = Seq.tabulate(TestEnum.elements.size)(i => U((i+1)*(i+1), width bits))

  switch(io.in_oh){
    for((item, enm) <- decodeItems.zip(TestEnum.elements)) {
      is(enm) {
        io.out_decoded := item
      }
    }
  }

}



class OneHotTest extends AnyFlatSpec with should.Matchers {
  behavior of "OneHotDUT"


  it should "create verilog" in {
    val config = SpinalConfig(mode=Verilog, mergeAsyncProcess=true).withoutEnumString()
    config.generate(new OneHotDUT(16, 12))
    config.generate(new OneHotEnum(12))
  }
}
