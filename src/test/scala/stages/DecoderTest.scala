package cpu.stages

import cpu.interfaces.regfile.{SourceSelect}
import cpu.interfaces.{DecoderData, ReadInterface, FetchOutput}
import isa.{FormEnums, MnemonicEnums, ISAPairings}
import util.{PipeStage, PipeData}

import spinal.core._
import spinal.lib._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._


class DecoderDUT extends PipeStage(new FetchOutput, new ReadInterface){
  val dec = new uOpAndFormDecoder
  //val populate = new PopulateByForm
  dec << pipeInput
  //dec >-> populate >> pipeOutput
}


class DecoderTest extends AnyFlatSpec with should.Matchers {
  behavior of "uOpAndFormDecoder"

  // it should "create verilog" in {
  //   val stage = new ChiselStage

  //   stage.emitVerilog(new uOpAndFormDecoder, Array("-td", "./test_run_dir"))
  //   stage.emitVerilog(new uOpAndFormDecoderBySeq(ISAPairings.pairings.take(10)), Array("-td", "./test_run_dir"))
  // }

  // it should "decode some simple instructions" in {
  //   test(new DecoderDUT)
  //     .withFlags(Array("--t-write-vcd")) { c =>
  //       c.pipeOutput.ready.poke(1.B)
  //       c.pipeInput.valid.poke(1.B)
  //       // andi. r1, r5, 0x3f
  //       c.pipeInput.bits.insn.poke(0x70a1003F.U)
  //       c.clock.step(10)
  //       c.pipeOutput.bits.dec_data.form.expect(FormEnums.D6)
  //       c.pipeOutput.bits.dec_data.found_match.expect(1.B)
  //       c.pipeOutput.bits.dec_data.opcode.expect(MnemonicEnums.andidot)
  //       c.pipeOutput.bits.slots(0).idx.expect(5.U)
  //       c.pipeOutput.bits.slots(0).sel.expect(SourceSelect.GPR)
  //       c.pipeOutput.bits.write_interface.slots(0).idx.expect(1.U)
  //       c.pipeOutput.bits.write_interface.slots(0).sel.expect(SourceSelect.GPR)
  //       c.pipeOutput.bits.imm.valid.expect(1.B)
  //       c.pipeOutput.bits.imm.bits.expect(0x3f.U)
  //     }
  // }
  // it should "decode ExecuteArgs" in {
  //   test(new DecoderDUT)
  //     .withFlags(Array("--t-write-vcd")) { c =>
  //       c.pipeOutput.ready.poke(1.B)
  //       c.pipeInput.valid.poke(1.B)

  //       // addi r4, r8, 0x1234
  //       c.pipeInput.bits.insn.poke(0x38a81234.U)
  //       c.clock.step(1)
  //       // add r2, r3, r4
  //       c.pipeInput.bits.insn.poke(0x7c432214.U)
  //       c.clock.step(1)
  //       // neg r10, r11
  //       c.pipeInput.bits.insn.poke(0x7d4b00d0.U)
  //       c.clock.step(10)
  //     }
  // }
}
