package cpu.stages

import cpu.interfaces.{FetchOutput, DecoderData, MultiDecoderData, ReadInterface}
import cpu.uOps.{FunctionalUnit, UOpsMapping}
import cpu.{CPUConfig}

import util.{PipeStage, PipeData, Mux1H}

import isa.{MnemonicEnums, FormEnums, SPREnums, Forms}
import isa.{ISAPairings, InstructionInfo, SourceSelect}

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

class uOpAndFormDecoderBySeq(val instructions: Seq[InstructionInfo])(implicit val config: CPUConfig) extends PipeStage(new DecoderData, new DecoderData){
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

  val uOpsMap = new UOpsMapping
  // If we haven't found a match yet,
  when(i.found_match === False){
    // set found_match if any bit in the vector is high
    o.found_match := oneHotMatcher.orR
    // // Lookup the mnemonic from instructions using the one hot index
    oneHotSwitch(True){
      for((info, idx) <- instructions.zipWithIndex){
        is(oneHotMatcher(idx)){
          o.opcode := info.mnemonic
        }
      }
    }
    oneHotSwitch(True){
      for((info, idx) <- instructions.zipWithIndex){
        is(oneHotMatcher(idx)){
          o.form := info.form
        }
      }
    }
    oneHotSwitch(True){
      for((info, idx) <- instructions.zipWithIndex){
        is(oneHotMatcher(idx)){
          o.uOps := uOpsMap.lookup(info.mnemonic)
        }
      }
    }
  }
}

class uOpAndFormDecoderMulti(numStages: Int)(implicit val config: CPUConfig) extends
    PipeStage(new DecoderData, new MultiDecoderData(numStages)){

  val numitems = (ISAPairings.pairings.size + numStages - 1) / numStages
  val groups = ISAPairings.pairings.grouped(numitems)
  val stages = groups.map( g => new uOpAndFormDecoderBySeq(g)).toSeq
  o.insn := i.insn
  o.cia := i.cia

  for((stage, idx) <- stages.zipWithIndex){
    stage << pipeInput
    stage.pipeOutput.ready := pipeOutput.ready
    stage.pipeOutput.flush := pipeOutput.flush
    pipeOutput.payload.data(idx) := stage.pipeOutput.payload
  }
}

class uOpAndFormDecoderMux(numStages: Int) extends
    PipeStage(new MultiDecoderData(numStages), new DecoderData){
  //val matches = B(0, numStages bits)
  o := o.getZero
  o.opcode := MnemonicEnums.td
  for((decdata, idx) <- i.data.zipWithIndex) {
    when(decdata.found_match){
      o := decdata
    }
  }
  when(o.found_match === False){
    pipeOutput.valid := False
  }
}


class uOpAndFormDecoder(implicit val config: CPUConfig) extends PipeStage(new FetchOutput, new DecoderData){

  val numStages = 12



  val di = new DecodeInit

  val multi = new uOpAndFormDecoderMulti(numStages)
  val mux = new uOpAndFormDecoderMux(numStages)


  di << pipeInput
  di >-> multi >-> mux >> pipeOutput

  import cpu.debug.{debug_all_decode, debug_last_decode}

  // if (debug_all_decode) {
  //   // print the status of each stage (WARNING! THIS IS COSTLY FOR SIMULATION)
  //   for((stage, index) <- stages.zipWithIndex.reverse) {
  //     when(stage.pipeOutput.fire && stage.pipeOutput.payload.found_match) {
  //       for (mnemonic <- MnemonicEnums.elements){
  //         when (mnemonic === stage.pipeOutput.payload.opcode){
  //           printf(s"DECODE: Got opcode $mnemonic from stage $index\n")
  //         }
  //       }
  //     }
  //   }
  // }

  // if (debug_last_decode) {
  //   when(pipeOutput.fire && pipeOutput.payload.found_match) {
  //     for (mnemonic <- MnemonicEnums.elements){
  //       when (mnemonic === pipeOutput.payload.opcode){
  //         printf(s"DECODE OUT: Got opcode $mnemonic\n")
  //       }
  //     }
  //   }
  // }

}
