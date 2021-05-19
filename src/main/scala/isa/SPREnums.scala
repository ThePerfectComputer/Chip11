//generated 2021-03-30 00:13:10.349816

package isa
import spinal.core._
import scala.collection._

// All the different addresses within the SPR that can be used
object SPREnums extends SpinalEnum{
  val NONE = newElement()
  // from Power ISA v3.0B, page 117
  val XER =     U(1)
  val DSCR =    U(3)
  val LR =      U(8)
  val CTR =     U(9)
  val AMR =     U(13)
  val TFHAR =   U(128)
  val TFIAR =   U(129)
  val TEXASR =  U(130)
  val TEXASRU = U(131)
  val VRSAVE =  U(256)
  val MMCR2 =   U(769)
  val MMCRA =   U(770)
  val PMC1 =    U(771)
  val PMC2 =    U(772)
  val PMC3 =    U(773)
  val PMC4 =    U(774)
  val PMC5 =    U(775)
  val PMC6 =    U(776)
  val MMCR0 =   U(779)
  val BESCRS =  U(800)
  val BESCRSU = U(801)
  val BESCRR =  U(802)
  val BESCRRU = U(803)
  val EBBHR =   U(804)
  val EBBRR =   U(805)
  val BESCR =   U(806)
  val TAR =     U(815)
  val PPR =     U(896)
  val PPR32 =   U(898)
}
