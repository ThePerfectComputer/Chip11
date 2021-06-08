package util

import spinal.core._
import spinal.core.Formal._
import spinal.lib.{master, slave}
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._
import scala.util.Random

class PipeStageDUT[T <: BaseType](dataType: HardType[T]) extends Component {
  val io = new Bundle {
    val pi = slave(Flushable(dataType()))
    val po = master(Flushable(dataType()))

    val in1 = in(dataType())
    val in2 = in(dataType())
  }

  val d1 = new Delay(dataType())
  val d2 = new Delay(dataType())
  val d3 = new Delay(dataType())

  d1 << io.pi
  d1 >-> d2 >/-> d3 >> io.po


  // Formal Stuff
  val sent1 = RegInit(False)
  val sent2 = RegInit(False)

  val received1 = RegInit(False)
  val received2 = RegInit(False)

  val init = initstate()
  when(init){
    assume(sent1 === False)
    assume(sent2 === False)
    assume(received1 === False)
    assume(received2 === False)
    assume(clockDomain.isResetActive)
  }.otherwise{
    assume(!clockDomain.isResetActive)
  }

  when(!init){
    when(!sent1 && io.pi.valid && io.pi.ready){
      assume(io.pi.payload === io.in1)
      sent1 := True
    }

    when(sent1 && !sent2 && io.pi.valid && io.pi.ready){
      assume(io.pi.payload === io.in2)
      sent2 := True
    }
  }
  assume(stable(io.in1))
  assume(stable(io.in2))
  assume(io.po.flush === False)

  when(!init){
    when(sent1 && !received1){
      when(io.po.valid && io.po.ready){
        assert(io.po.payload === io.in1)
        received1 := True
      }
    }
    when(sent2 && received1 && !received2){
      when(io.po.valid && io.po.ready){
        assert(io.po.payload === io.in2)
        received2 := True
      }
    }
    cover(received2)
  }

}

class PipeStageProofVerilogFormal extends AnyFlatSpec with should.Matchers {
  behavior of "PipeStageDUT"

  it should "create verilog" in {
    val config = SpinalConfig(
      defaultConfigForClockDomains =
        ClockDomainConfig(resetKind = SYNC, resetActiveLevel = HIGH),
      targetDirectory = "formal"
    ).withoutEnumString()
    config.includeFormal.generateSystemVerilog(new PipeStageDUT(UInt(8 bits)))
  }
}
