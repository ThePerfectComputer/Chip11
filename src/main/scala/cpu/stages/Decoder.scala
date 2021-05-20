package cpu.stages

import cpu.interfaces.regfile.{SourceSelect}
import cpu.interfaces.{FetchOutput, DecoderData, ReadInterface}
import cpu.uOps.{FunctionalUnit, uOpsMapping}

import util.{PipeStage, PipeData}

import isa.{MnemonicEnums, FormEnums, SPREnums, Forms}
import isa.{ISAPairings, InstructionInfo}

import spinal.core._
import spinal.lib.{MuxOH}

/* Merely translates from FetchOutput to DecoderData
 * Reccommend connecting this combinatorially to the second decoder stage
 */

class DecodeInit extends PipeStage(new FetchOutput, new DecoderData) {
  o.insn := i.insn
  o.cia := i.cia
  o.opcode := MnemonicEnums.td
  o.form := FormEnums.NONE
  o.found_match := False
  o.insn_unsupported := False
  o.uOps.functional_unit := FunctionalUnit.INTEGER
  o.uOps.sub_function := 0
  o.uOps.args := 0
}

// /* Actually matches against the instructions */

class uOpAndFormDecoderBySeq(val instructions: Seq[InstructionInfo]) extends PipeStage(new DecoderData, new DecoderData){
  o <> i

  val oneHotMatcher = Bits(instructions.size bits)
  oneHotMatcher := 0
  
  // Generates a vector of booleans. Each boolean indicates whether
  // the instruction data matches the bit pattern in instructions(i)
  for((info, idx) <- instructions.zipWithIndex){
    val opPattern = MaskedLiteral(info.opcode)
    when(i.insn === opPattern){
      oneHotMatcher(idx) := True
    }
  }
  // If we haven't found a match yet,
  when(i.found_match === False){
    // set found_match if any bit in the vector is high
    o.found_match := oneHotMatcher.orR
    // // Lookup the mnemonic from instructions using the one hot index
    o.opcode.assignFromBits(MuxOH(oneHotMatcher, instructions.map(x => x.mnemonic.asBits)))
    o.form.assignFromBits(
      MuxOH(oneHotMatcher, instructions.map(x => x.form.asBits)))
    // o.uOps := (MuxOH(
    //   oneHotMatcher,
    //   instructions.map(x => uOpsMapping.lookup(x.mnemonic))))
  }
}


class uOpAndFormDecoder extends PipeStage(new FetchOutput, new DecoderData){

  val numstages = 6
  val numitems = (ISAPairings.pairings.size + numstages - 1) / numstages
  val groups = ISAPairings.pairings.grouped(numitems)



  val di = new DecodeInit
  val stages = groups.map( g => new uOpAndFormDecoderBySeq(g)).toSeq


  di << pipeInput
  di >-> stages(0)
  for(i <- 1 until stages.size) {
    stages(i-1) >-> stages(i)
  }
  stages.last >> pipeOutput

  import cpu.debug.{debug_all_decode, debug_last_decode}

  if (debug_all_decode) {
    // print the status of each stage (WARNING! THIS IS COSTLY FOR SIMULATION)
    for((stage, index) <- stages.zipWithIndex.reverse) {
      when(stage.pipeOutput.fire && stage.pipeOutput.payload.found_match) {
        for (mnemonic <- MnemonicEnums.elements){
          when (mnemonic === stage.pipeOutput.payload.opcode){
            printf(s"DECODE: Got opcode $mnemonic from stage $index\n")
          }
        }
      }
    }
  }

  if (debug_last_decode) {
    when(pipeOutput.fire && pipeOutput.payload.found_match) {
      for (mnemonic <- MnemonicEnums.elements){
        when (mnemonic === pipeOutput.payload.opcode){
          printf(s"DECODE OUT: Got opcode $mnemonic\n")
        }
      }
    }
  }

}
