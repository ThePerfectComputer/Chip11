package cpu.uOps

import cpu.uOps.functional_units.Integer.{IntegerFUSub, AdderSelectB, AdderCarryIn, AdderArgs, LogicSelectB, LogicArgs, MultiplierSelectB, MultiplierArgs, BranchArgs, ShifterSelectB, ShifterME, ShifterMB, ShifterArgs, ComparatorArgs, ComparatorSelectB}

import cpu.uOps.functional_units.Integer.{ZCntArgs, ZCntDirection, ZCntSize, PopcntSize, PopcntArgs}
import spinal.core._
//import spinal.lib._
import scala.collection._

import isa.MnemonicEnums
import cpu.{CPUConfig}
// should contain the ExecuteArgsMapping
class UOpsMapping(implicit config: CPUConfig) extends Component {
  import isa.MnemonicEnums._
  import FunctionalUnit._

  val adder = Map(
    add_o__dot_   -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    addi          -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    addis         -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ImmShift, AdderCarryIn.ZERO, false))),
    addc_o__dot_  -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    addic         -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    addicdot      -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    adde_o__dot_  -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.CA, false))),
    addze_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ZERO, AdderCarryIn.CA, false))),
    subf_o__dot_  -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ONE, true))),
    subfc_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ONE, true))),
    subfic        -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ONE, true))),
    subfe_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.CA, true))),
    neg_o__dot_   -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ZERO, AdderCarryIn.ONE, true))),
    addme_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.NEGATIVE_ONE, AdderCarryIn.CA, false))),
    subfme_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.NEGATIVE_ONE, AdderCarryIn.CA, true))),
    subfze_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ZERO, AdderCarryIn.CA, true))),
    addze_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ZERO, AdderCarryIn.CA, false))),
    // Loadstore instructions use the adder to calculate effective addresses
    ld            -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ImmShift2, AdderCarryIn.ZERO, false))),
    ldu            -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ImmShift2, AdderCarryIn.ZERO, false))),
    lwz           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    // Different form than lwz
    lwa           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ImmShift2, AdderCarryIn.ZERO, false))),
    lwzu           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    lhz           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    lha           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    lhzu           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    lhau           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    lbz           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    ldx            -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    lbzx           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    lwzx           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    lwax           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    lhzx           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    lhax           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    ldux            -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    lbzux           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    lwzux           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    lwaux           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    lhzux           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    lhaux           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    lbzu           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    stb           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    stw           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    sth           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    std           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ImmShift2, AdderCarryIn.ZERO, false))),
    stbx           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    stwx           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    sthx           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    stdx           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    stbux           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    stwux           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    sthux           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    stdux           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Slot2, AdderCarryIn.ZERO, false))),
    stbu           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    stwu           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    sthu           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.Imm, AdderCarryIn.ZERO, false))),
    stdu           -> out(uOps(INTEGER, IntegerFUSub.Adder, AdderArgs(AdderSelectB.ImmShift2, AdderCarryIn.ZERO, false)))
  )
  val logic = Map(
    and_dot_ -> out(uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, false, false, false, false))),
    andc_dot_ -> out(uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, false, true, false, false))),
    andidot -> out(uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Imm, false, false, false, false))),
    nor_dot_ -> out(uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, true, true, false, false))),
    or_dot_ -> out(uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, true, true, true, false))),
    orc_dot_ -> out(uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, true, false, true, false))),
    ori -> out(uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Imm, true, true, true, false))),
    oris -> out(uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.ImmShift, true, true, true, false))),
    xor_dot_ -> out(uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Slot2, false, false, false, true))),
    xori -> out(uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.Imm, false, false, false, true))),
    xoris -> out(uOps(INTEGER, IntegerFUSub.LogicUnit, LogicArgs(LogicSelectB.ImmShift, false, false, false, true)))
  )
  val multiplier = Map(
    //is_div, word_operands, is_unsigned, output_high, output_word, shift_a
    divd_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, true, false, false, false, false, false))),
    divde_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, true, false, false, false, false, true))),
    divde_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, true, true, true, false, true, false))),

    mulhd_dot_ -> out(uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, false, false, false, true, false, false))),
    mulld_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, false, false, false, false, false, false))),
    mulli -> out(uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Imm, false, false, false, false, false, false))),
    mullw_o__dot_ -> out(uOps(INTEGER, IntegerFUSub.Multiplier, MultiplierArgs(MultiplierSelectB.Slot2, false, true, false, false, false, false)))
  )
  val shifter = Map(
    //slotB, ME, MB, keep_source, word_op, left, is_arithmetic, is_shift, byte_op
    rldcr_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.ME, ShifterMB.MSB, false, false, true, false, false, false))),
    rldicl_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.LSB, ShifterMB.MB, false, false, true, false, false, false))),

    rldicr_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.ME, ShifterMB.MSB, false, false, true, false, false, false))),

    rldimi_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.IMM_REV, ShifterMB.MB_32, true, false, true, false, false, false))),
    rlwimi_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.ME_32, ShifterMB.MB_32, true, true, true, false, false, false))),
    rlwinm_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.ME_32, ShifterMB.MB_32, false, true, true, false, false, false))),

    sld_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, false, true, false, true, false))),
    slw_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, true, true, false, true, false))),
    srad_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, false, false, true, true, false))),
    sradi_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.LSB, ShifterMB.MSB, false, false, false, true, true, false))),
    sraw_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, true, false, true, true, false))),
    srawi_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Imm, ShifterME.LSB_32, ShifterMB.MSB, false, true, false, true, true, false))),
    srd_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, false, false, false, true, false))),
    srw_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.Slot2, ShifterME.LSB, ShifterMB.MSB, false, true, false, false, true, false))),

    extsw_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.ZERO, ShifterME.WORD, ShifterMB.MSB, false, true, false, true, true, false))),
    extsb_dot_ -> out(uOps(INTEGER, IntegerFUSub.Shifter, ShifterArgs(ShifterSelectB.ZERO, ShifterME.WORD, ShifterMB.MSB, false, false, false, true, true, true)))
  )
  val branch = Map(
    // Branch
    b_l__a_ -> out(uOps(INTEGER, IntegerFUSub.Branch, BranchArgs(false, true))),
    bc_l__a_ -> out(uOps(INTEGER, IntegerFUSub.Branch, BranchArgs(true, true))),
    bclr_l_ -> out(uOps(INTEGER, IntegerFUSub.Branch, BranchArgs(true, false))),
    bctar_l_ -> out(uOps(INTEGER, IntegerFUSub.Branch, BranchArgs(true, false))),
    bcctr_l_ -> out(uOps(INTEGER, IntegerFUSub.Branch, BranchArgs(true, false)))
  )
  val comparator = Map(
    cmpi -> out(uOps(INTEGER, IntegerFUSub.Comparator, ComparatorArgs(ComparatorSelectB.Imm, false))),
    cmp -> out(uOps(INTEGER, IntegerFUSub.Comparator, ComparatorArgs(ComparatorSelectB.Slot2, false))),
    cmpli -> out(uOps(INTEGER, IntegerFUSub.Comparator, ComparatorArgs(ComparatorSelectB.Imm, true))),
    cmpl -> out(uOps(INTEGER, IntegerFUSub.Comparator, ComparatorArgs(ComparatorSelectB.Slot2, true)))
  )
  val move = Map(
    mtspr -> out(uOps(INTEGER, IntegerFUSub.Move, U(0))),
    mfspr -> out(uOps(INTEGER, IntegerFUSub.Move, U(0))),
    mtcrf -> out(uOps(INTEGER, IntegerFUSub.Move, U(0))),
    mtocrf -> out(uOps(INTEGER, IntegerFUSub.Move, U(0))),
    mfcr -> out(uOps(INTEGER, IntegerFUSub.Move, U(0))),
    mfocrf -> out(uOps(INTEGER, IntegerFUSub.Move, U(0)))
  )
  val zCnt = Map(
    cntlzd_dot_ -> out(uOps(INTEGER, IntegerFUSub.ZCnt, ZCntArgs(ZCntDirection.LEADING, ZCntSize.DWORD))),
    cnttzd_dot_ -> out(uOps(INTEGER, IntegerFUSub.ZCnt, ZCntArgs(ZCntDirection.TRAILING, ZCntSize.DWORD))),
    cntlzw_dot_ -> out(uOps(INTEGER, IntegerFUSub.ZCnt, ZCntArgs(ZCntDirection.LEADING, ZCntSize.WORD))),
    cnttzw_dot_ -> out(uOps(INTEGER, IntegerFUSub.ZCnt, ZCntArgs(ZCntDirection.TRAILING, ZCntSize.WORD)))
  )
  val popcnt = Map(
    popcntd -> out(uOps(INTEGER, IntegerFUSub.Popcnt, PopcntArgs(PopcntSize.DWORD))),
    popcntw -> out(uOps(INTEGER, IntegerFUSub.Popcnt, PopcntArgs(PopcntSize.WORD))),
    popcntb -> out(uOps(INTEGER, IntegerFUSub.Popcnt, PopcntArgs(PopcntSize.BYTE)))
  )
  val default = out(uOps(INTEGER, U(0), U(0)))

  val info = Map[MnemonicEnums.E, uOps]()
    .++(if(config.adder) branch else Nil)
    .++(if(config.adder) adder else Nil)
    .++(if(config.adder) logic else Nil)
    .++(if(config.adder) multiplier else Nil)
    .++(if(config.adder) shifter else Nil)
    .++(if(config.adder) move else Nil)
    .++(if(config.comparator) comparator else Nil)
    .++(if(config.zcnt) zCnt else Nil)
    .++(if(config.popcnt) popcnt else Nil)

  def lookup(x: MnemonicEnums.E) = 
    info getOrElse (x, default)
}
