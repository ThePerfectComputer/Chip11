//generated 2021-03-30 00:13:10.349816

package isa
import spinal.core._
import scala.collection._

// All the different addresses within the SPR that can be used
object SPREnums extends SpinalEnum{
  val NONE = newElement()
  // from Power ISA v3.0B, page 117
  val XER =     newElement()
  val DSCR =    newElement()
  val LR =      newElement()
  val CTR =     newElement()
  val AMR =     newElement()
  val TFHAR =   newElement()
  val TFIAR =   newElement()
  val TEXASR =  newElement()
  val TEXASRU = newElement()
  val VRSAVE =  newElement()
  val MMCR2 =   newElement()
  val MMCRA =   newElement()
  val PMC1 =    newElement()
  val PMC2 =    newElement()
  val PMC3 =    newElement()
  val PMC4 =    newElement()
  val PMC5 =    newElement()
  val PMC6 =    newElement()
  val MMCR0 =   newElement()
  val BESCRS =  newElement()
  val BESCRSU = newElement()
  val BESCRR =  newElement()
  val BESCRRU = newElement()
  val EBBHR =   newElement()
  val EBBRR =   newElement()
  val BESCR =   newElement()
  val TAR =     newElement()
  val PPR =     newElement()
  val PPR32 =   newElement()


  defaultEncoding = SpinalEnumEncoding("sprEncoding")(
  NONE -> 0,
  XER -> 1,
  DSCR -> 3,
  LR -> 8,
  CTR -> 9,
  AMR -> 13,
  TFHAR -> 128,
  TFIAR -> 129,
  TEXASR -> 130,
  TEXASRU -> 131,
  VRSAVE -> 256,
  MMCR2 -> 769,
  MMCRA -> 770,
  PMC1 -> 771,
  PMC2 -> 772,
  PMC3 -> 773,
  PMC4 -> 774,
  PMC5 -> 775,
  PMC6 -> 776,
  MMCR0 -> 779,
  BESCRS -> 800,
  BESCRSU -> 801,
  BESCRR -> 802,
  BESCRRU -> 803,
  EBBHR -> 804,
  EBBRR -> 805,
  BESCR -> 806,
  TAR -> 815,
  PPR -> 896,
  PPR32 -> 898)
}
