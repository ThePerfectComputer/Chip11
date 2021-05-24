package soc

import spinal.core._
import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._



class SoCTest extends AnyFlatSpec with should.Matchers {
  behavior of "SoC"

  it should "create verilog" in {
    SpinalVerilog(new SoC)
  }
}
