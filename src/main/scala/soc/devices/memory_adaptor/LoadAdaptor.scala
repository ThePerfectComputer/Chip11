package soc.devices.memory_adaptor

import soc.interfaces.MemBus128
import cpu.interfaces.{LineRequest, LineResponse}
import cpu.shared.memory_state.{TransactionStatus, TransactionType, TransactionSize}

import spinal.core._
import spinal.lib._

class LoadAdaptor extends Component {

  val io = new Bundle {
    val aligned            = in(Bool())
    val start_byte         = in(UInt(4 bits))
    val size               = in(TransactionSize())

    val request_accepted   = in(Bool())
    val transaction2_ack   = in(Bool())

    val membus_load_line   = in(UInt(128 bits))
    val line_response      = out(UInt(128 bits))
  }

  // register inputs such that data we send back up
  // through line response has the correct configuration
  val aligned_reg     = Reg(Bool())
  val start_byte_reg  = Reg(UInt(4 bits))
  val size_reg        = Reg(TransactionSize())

  when (io.request_accepted) {aligned_reg      := io.aligned}
  when (io.request_accepted) {start_byte_reg   := io.start_byte}
  when (io.request_accepted) {size_reg         := io.size}

  val transaction1_data         = Vec(UInt(8 bits), 16)
  val transaction1_data_shifted = Vec(UInt(8 bits), 16)
  val transaction2_data_reg     = Reg(Vec(UInt(8 bits), 16))
  val merge_and_shift           = Vec(UInt(8 bits), 16)

  // chisel complains about initialization
  transaction1_data := io.membus_load_line.asBits.as(transaction1_data)
  transaction1_data_shifted.foreach{el => el := 0}
  merge_and_shift.foreach{el => el := 0}
  io.line_response := 0

  when (aligned_reg)  {io.line_response := transaction1_data_shifted.as(UInt(128 bits))}
  when (!aligned_reg) {io.line_response := merge_and_shift.as(UInt(128 bits))}
  when (io.transaction2_ack) {transaction2_data_reg := io.membus_load_line.as(Vec(UInt(8 bits), 16))}

  for(permutation <- LineRequestTruthTable.TableEntries){

    val start_byte            = permutation.start_byte
    val request_size          = permutation.request_size
    val bytes_in_transaction1 = permutation.bytes_in_transaction1
    val bytes_in_transaction2 = permutation.bytes_in_transaction2

    when((start_byte_reg === start_byte) & (size_reg.as(UInt()) === request_size)){

      val transaction1_data_selection = transaction1_data.drop(start_byte).take(request_size)

      // TransactionSize.all.foreach{state_iter => 
      //   when (size_reg === state_iter){
      //     printf(s"$state_iter\n")
      //   }
      // }


      transaction1_data_shifted
        .zip(transaction1_data_selection)
          .foreach{
            case(outbound, inbound) => outbound := inbound
            // printf(p"outbound = ${Hexadecimal(outbound)} \n")
            // printf(p"inbound = ${Hexadecimal(inbound)} \n")
            }

      merge_and_shift
          .take(bytes_in_transaction1)
            .zip(transaction1_data.drop(start_byte))
              .foreach{case(outbound, inbound) => outbound := inbound}

      merge_and_shift
        .drop(bytes_in_transaction1)
          .take(bytes_in_transaction2)
            .zip(transaction2_data_reg)
              .foreach{case(outbound, inbound) => outbound := inbound}

    }
  }

}