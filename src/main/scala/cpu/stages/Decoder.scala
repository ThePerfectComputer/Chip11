package cpu.stages

import cpu.interfaces.regfile.{SourceSelect}
import cpu.interfaces.{FetchOutput, DecoderData, ReadInterface}
import cpu.uOps.{FunctionalUnit, uOpsMapping}

// import util.{PipeStage, RegisteredPipeStage, PipeData}

// import isa.{MnemonicEnums, FormEnums, SPREnums, Forms}
import isa.{ISAPairings, InstructionInfo}

import spinal.core._
import spinal.lib._

/* Merely translates from FetchOutput to DecoderData
 * Reccommend connecting this combinatorially to the second decoder stage
 */

// class DecodeInit extends PipeStage(new FetchOutput, new DecoderData) {
//   o.insn := i.insn
//   o.cia := i.cia
//   o.opcode := MnemonicEnums.td
//   o.form := FormEnums.NONE
//   o.found_match := 0.B
//   o.insn_unsupported := 0.B
//   o.uOps.functional_unit := FunctionalUnit.INTEGER
//   o.uOps.sub_function := 0.U
//   o.uOps.args := 0.U
// }

// /* Actually matches against the instructions */

// class uOpAndFormDecoderBySeq(val instructions: Seq[InstructionInfo]) extends PipeStage(new DecoderData, new DecoderData){
//   o <> i

//   val oneHotMatcher = Wire(Vec(instructions.size, Bool()))
//   oneHotMatcher := 0.U(instructions.size.W).asBools
  
//   // Generates a vector of booleans. Each boolean indicates whether
//   // the instruction data matches the bit pattern in instructions(i)
//   for((info, idx) <- instructions.zipWithIndex){
//     val opPattern = BitPat(info.opcode)
//     when(i.insn === opPattern){
//       oneHotMatcher(idx) := 1.U
//     }
//   }
//   // If we haven't found a match yet,
//   when(i.found_match === 0.B){
//     // set found_match if any bit in the vector is high
//     o.found_match := oneHotMatcher.reduceTree((a, b) => a|b)
//     // Lookup the mnemonic from instructions using the one hot index
//     o.opcode := Mux1H(
//       oneHotMatcher.asUInt(),
//       instructions.map(x => x.mnemonic))
//     o.form := Mux1H(
//       oneHotMatcher.asUInt(),
//       instructions.map(x => x.form))

//     o.uOps := Mux1H(
//       oneHotMatcher.asUInt(),
//       instructions.map(x => uOpsMapping.lookup(x.mnemonic)))

//   }
// }


// class uOpAndFormDecoder extends PipeStage(new FetchOutput, new DecoderData){

//   val numstages = 6
//   val numitems = (ISAPairings.pairings.size + numstages - 1) / numstages
//   val groups = ISAPairings.pairings.grouped(numitems)



//   val di = Module(new DecodeInit)
//   val stages = groups.map( g =>
//     Module(new RegisteredPipeStage(new uOpAndFormDecoderBySeq(g)))).toSeq


//   connectIn(di)
//   di.connect(stages(0))
//   for(i <- 1 until stages.size) {
//     stages(i-1).connect(stages(i))
//   }
//   stages.last.connectOut(pipeOutput)

//   import cpu.debug.{debug_all_decode, debug_last_decode}

//   if (debug_all_decode) {
//     // print the status of each stage (WARNING! THIS IS COSTLY FOR SIMULATION)
//     for((stage, index) <- stages.zipWithIndex.reverse) {
//       when(stage.pipeOutput.fire() && stage.pipeOutput.bits.found_match) {
//         for (mnemonic <- MnemonicEnums.all){
//           when (mnemonic === stage.pipeOutput.bits.opcode){
//             printf(s"DECODE: Got opcode $mnemonic from stage $index\n")
//           }
//         }
//       }
//     }
//   }

//   if (debug_last_decode) {
//     when(pipeOutput.fire() && pipeOutput.bits.found_match) {
//       for (mnemonic <- MnemonicEnums.all){
//         when (mnemonic === pipeOutput.bits.opcode){
//           printf(s"DECODE OUT: Got opcode $mnemonic\n")
//         }
//       }
//     }
//   }

// }
