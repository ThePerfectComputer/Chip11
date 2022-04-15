package cpu.shared.memory_state

import spinal.core._
import spinal.lib._

object TransactionSize extends SpinalEnum{
  val BYTE       = newElement()
  val HALFWORD   = newElement()
  val WORD       = newElement()
  val DOUBLEWORD = newElement()
  val QUADWORD   = newElement()
  defaultEncoding = SpinalEnumEncoding("staticEncoding")(
    BYTE -> 1,
    HALFWORD -> 2,
    WORD -> 4,
    DOUBLEWORD -> 8,
    QUADWORD -> 16)
}

object TransactionType extends SpinalEnum{
  val NONE  = newElement()
  val LOAD  = newElement()
  val STORE = newElement()
}

object TransactionStatus extends SpinalEnum{
  val IDLE       = newElement()
  val DONE       = newElement()
  val WAITING    = newElement()
  val SPEC_MISS  = newElement()
  val PAGE_FAULT  = newElement()
}
