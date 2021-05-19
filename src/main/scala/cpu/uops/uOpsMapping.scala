package cpu.uOps

import cpu.uOps.functional_units.Integer.{IntegerFUSub, AdderSelectB, AdderCarryIn, AdderArgs, LogicSelectB, LogicArgs, MultiplierSelectB, MultiplierArgs, BranchArgs, ShifterSelectB, ShifterME, ShifterMB, ShifterArgs, ComparatorArgs, ComparatorSelectB}

import spinal.core._
//import spinal.lib._
import scala.collection._

import isa.MnemonicEnums
// should contain the ExecuteArgsMapping
object uOpsMapping {
  import isa.MnemonicEnums._
  import FunctionalUnit._
  val adder = Map(
    add_o__dot_   -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false)),
    addi          -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false)),
    addis         -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ImmShift, AdderCarryIn.ZERO, false)),
    addc_o__dot_  -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false)),
    addic         -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false)),
    addicdot      -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false)),
    adde_o__dot_  -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.CA, false)),
    addze_o__dot_ -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ZERO, AdderCarryIn.CA, false)),
    subf_o__dot_  -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ONE, true)),
    subfc_o__dot_ -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ONE, true)),
    subfic        -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ONE, true)),
    subfe_o__dot_ -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.CA, true)),
    neg_o__dot_   -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ZERO, AdderCarryIn.ONE, true)),
    // Loadstore instructions use the adder to calculate effective addresses
    ld            -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ImmShift2, AdderCarryIn.ZERO, false)),
    lbz           -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false)),
    stb           -> uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))
  )
  val logic = Map(
    and_dot_ -> uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, false, false, false, false)),
    andc_dot_ -> uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, false, true, false, false)),
    andidot -> uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Imm, false, false, false, false)),
    nor_dot_ -> uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, true, true, false, false)),
    or_dot_ -> uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, true, true, true, false)),
    orc_dot_ -> uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, true, false, true, false)),
    ori -> uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Imm, true, true, true, false)),
    oris -> uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.ImmShift, true, true, true, false)),
    xor_dot_ -> uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, false, false, false, true)),
    xori -> uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Imm, false, false, false, true)),
    xoris -> uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.ImmShift, false, false, false, true))
  )
  val multiplier = Map(
    //is_div, word_operands, is_unsigned, output_high, output_word, shift_a
    divd_o__dot_ -> uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, true, false, false, false, false, false)),
    divde_o__dot_ -> uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, true, false, false, false, false, true)),
    divde_o__dot_ -> uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, true, true, true, false, true, false)),

    mulhd_dot_ -> uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, false, false, false, true, false, false)),
    mulld_o__dot_ -> uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, false, false, false, false, false, false)),
    mulli -> uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Imm, false, false, false, false, false, false)),
    mullw_o__dot_ -> uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, false, true, false, false, false, false))
  )
  val shifter = Map(
    //slotB, ME, MB, keep_source, word_op, left, is_arithmetic, is_shift, byte_op
    rldcr_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.ME, ShifterMB.MSB, false, false, true, false, false, false)),
    rldicl_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.LSB, ShifterMB.MB, false, false, true, false, false, false)),

    rldicr_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.ME, ShifterMB.MSB, false, false, true, false, false, false)),

    rldimi_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.IMM_REV, ShifterMB.MB_32, true, false, true, false, false, false)),
    rlwimi_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.ME_32, ShifterMB.MB_32, true, true, true, false, false, false)),
    rlwinm_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.ME_32, ShifterMB.MB_32, false, true, true, false, false, false)),

    sld_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, false, true, false, true, false)),
    slw_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, true, true, false, true, false)),
    srad_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, false, false, true, true, false)),
    sradi_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.LSB, ShifterMB.MSB, false, false, false, true, true, false)),
    sraw_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, true, false, true, true, false)),
    srawi_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.LSB, ShifterMB.MSB, false, true, false, true, true, false)),
    srd_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, false, false, false, true, false)),
    srw_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, true, false, false, true, false)),

    extsw_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.ZERO, ShifterME.WORD, ShifterMB.MSB, false, true, false, true, true, false)),
    extsb_dot_ -> uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.ZERO, ShifterME.WORD, ShifterMB.MSB, false, false, false, true, true, true))
  )
  val branch = Map(
    // Branch
    b_l__a_ -> uOps(INTEGER, IntegerFUSub.Branch, BranchArgs(false, true)),
    bc_l__a_ -> uOps(INTEGER, IntegerFUSub.Branch, BranchArgs(true, true)),
    bclr_l_ -> uOps(INTEGER, IntegerFUSub.Branch, BranchArgs(true, false)),
    bctar_l_ -> uOps(INTEGER, IntegerFUSub.Branch, BranchArgs(true, false)),
    bcctr_l_ -> uOps(INTEGER, IntegerFUSub.Branch, BranchArgs(true, false))
  )

  val comparator = Map(
    cmpi -> uOps(INTEGER, IntegerFUSub.Comparator, ComparatorArgs(ComparatorSelectB.Imm, false)),
    cmp -> uOps(INTEGER, IntegerFUSub.Comparator, ComparatorArgs(ComparatorSelectB.Slot2, false)),
    cmpli -> uOps(INTEGER, IntegerFUSub.Comparator, ComparatorArgs(ComparatorSelectB.Imm, true)),
    cmpl -> uOps(INTEGER, IntegerFUSub.Comparator, ComparatorArgs(ComparatorSelectB.Slot2, true))
  )

  val move = Map(
    mtspr -> uOps(INTEGER, IntegerFUSub.Move, U(0)),
    mfspr -> uOps(INTEGER, IntegerFUSub.Move, U(0)),
    mtcrf -> uOps(INTEGER, IntegerFUSub.Move, U(0)),
    mtocrf -> uOps(INTEGER, IntegerFUSub.Move, U(0)),
    mfcr -> uOps(INTEGER, IntegerFUSub.Move, U(0)),
    mfocrf -> uOps(INTEGER, IntegerFUSub.Move, U(0))
  )
  import cpu.config
  val info = Map[MnemonicEnums.E, uOps]()
    .++(if(config.adder) branch else Nil)
    .++(if(config.adder) adder else Nil)
    .++(if(config.adder) logic else Nil)
    .++(if(config.adder) multiplier else Nil)
    .++(if(config.adder) shifter else Nil)
    .++(if(config.adder) move else Nil)
    .++(if(config.comparator) comparator else Nil)

  val default = uOps(INTEGER, U(0), U(0))

  def lookup(x: MnemonicEnums.E) = 
    info getOrElse (x, default)
}
