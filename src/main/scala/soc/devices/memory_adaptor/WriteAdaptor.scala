package soc.devices.memory_adaptor

import soc.interfaces.MemBus128
import cpu.interfaces.{LineRequest, LineResponse}
import cpu.shared.memory_state.{TransactionStatus, TransactionType, TransactionSize}

import spinal.core._
import spinal.lib._

class WriteAdaptor extends Component {

  val io = new Bundle {
    val state                   = in(MemoryAdaptorState())
    val start_byte              = in(UInt(4 bits))
    val aligned                 = in(Bool())
    val size                    = in(TransactionSize())
    val line_request            = in(UInt(128 bits))

    val transaction1_data = out(Vec(UInt(8 bits), 16))
    val transaction1_mask = out(Vec(Bool(),       16))
    val transaction2_data = out(Vec(UInt(8 bits), 16))
    val transaction2_mask = out(Vec(Bool(),       16))
  }

  val line_request_by_byte = io.line_request.as(Vec(UInt(8 bits), 16))

  val transaction1_data = io.transaction1_data
  val transaction1_mask = io.transaction1_mask
  val transaction2_data = io.transaction2_data
  val transaction2_mask = io.transaction2_mask

  // default tieoffs
  transaction1_data.foreach{el => el := 0}
  transaction2_data.foreach{el => el := 0}
  transaction1_mask.foreach{el => el := False}
  transaction2_mask.foreach{el => el := False}

  for(permutation <- LineRequestTruthTable.TableEntries){

    val start_byte   = permutation.start_byte
    val request_size = permutation.request_size
    val bytes_in_transaction1 = permutation.bytes_in_transaction1
    val bytes_in_transaction2 = permutation.bytes_in_transaction2

    when((io.start_byte === start_byte) & (io.size.as(UInt()) === request_size)){
      val line_request_selection1 = line_request_by_byte.take(bytes_in_transaction1)
      val transaction1_selection = transaction1_data.drop(start_byte).take(bytes_in_transaction1)

      transaction1_selection
        .zip(line_request_selection1)
          .foreach{case(outbound, inbound) => outbound := inbound}

      transaction1_mask
        .drop(start_byte)
          .take(bytes_in_transaction1).foreach{el => el := True}

      val line_request_selection2 = line_request_by_byte.drop(bytes_in_transaction1).take(bytes_in_transaction2)
      val transaction2_selection = transaction2_data.take(bytes_in_transaction2)

      transaction2_selection
        .zip(line_request_selection2)
          .foreach{case(outbound, inbound) => outbound := inbound}

      transaction2_mask
        .take(bytes_in_transaction2)
          .foreach{el => el := True}
    }
  }

}