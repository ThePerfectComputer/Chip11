package soc.devices.memory_adaptor

import Console.{BLUE, GREEN, CYAN_B, RED, RESET}
import soc.devices.DualPortSram128

import cpu.interfaces.{LineRequest, LineResponse}
import cpu.shared.memory_state.{TransactionStatus, TransactionType, TransactionSize}
import util._

import spinal.core._
import spinal.lib._
import spinal.sim._
import spinal.core.sim._

import org.scalatest._
import flatspec._
import matchers._

class MemoryAdaptorWithSram(debug : Boolean = false) extends Component {
  // instantiate modules
  val memory_adaptor = new MemoryAdaptor(debug = true)
  val ram = new DualPortSram128()
  
  val io = new Bundle {
    val request  = in(new LineRequest())
    val response = out(new LineResponse())

    // debugging signals
    val ack = out(Bool())
  }
  
  // dontTouch(ram.io)
  io.ack := io.response.status === TransactionStatus.DONE

  // connect up modules
  io.request <> memory_adaptor.io.request
  io.response <> memory_adaptor.io.response
  memory_adaptor.io.membus <> ram.io.port_2
  
  // port_1, which is read-only, should remain unconnected
  ram.io.port_1.ldst_req       := TransactionType.NONE
  ram.io.port_1.quad_word_address        := 0
  ram.io.port_1.write_mask.foreach{byte_mask => byte_mask := False}
  ram.io.port_1.write_data.foreach{byte => byte := 0}

  // if (debug) {
  //   // need cycle count when debugging
  //   val cycle_count_reg = RegInit(U(0, 8 bits))
  //   cycle_count_reg := cycle_count_reg + 1
  
  //   // print state
  //   MemoryAdaptorState.all.foreach{value =>
  //     when (memory_adaptor.io.state.get === value){
  //       printf(p"memory_adaptor.state = ${MemoryAdaptorState(value.litValue.asUInt).toString}\n")
  //     }
  //   }

  //   TransactionType.all.foreach{value =>
  //     when (io.request.ldst_req === value){
  //       printf(p"io.request.ldst_req = ${TransactionType(value.litValue.asUInt).toString}\n")
  //     }
  //   }

  //   TransactionStatus.all.foreach{value =>
  //     when (io.response.status === value){
  //       printf(p"io.response.status = ${TransactionStatus(value.litValue.asUInt).toString}\n")
  //     }
  //   }
    
  //   printf(p"ack = ${io.ack}\n")
  //   printf(p"cycle_count = $cycle_count_reg\n")
  //   printf(p"\n")
  // }

}

class TruthTableTest extends AnyFlatSpec with should.Matchers {
  behavior of "Memory Adaptor"

  it should "create verilog" in {
    SpinalVerilog(new MemoryAdaptorWithSram)
  }

  it should "test all 80 possible transactions from the truth table" in {
    val debug = true

    SimConfig.withWave.doSim(new MemoryAdaptorWithSram()){dut =>
      SimTimeout(300)
      dut.clockDomain.onRisingEdges{dut.memory_adaptor.debug()}
      dut.clockDomain.forkStimulus(period = 10)

      def get_ack() = {
        while(!dut.io.ack.toBoolean){
          if (debug) {println(s"$CYAN_B stepping ack$RESET")}
          dut.clockDomain.waitRisingEdge()
        }
        dut.io.request.ldst_req #= TransactionType.NONE
      }

      for(start_byte <- 0 until 16) {
        println(s"start_byte = ${start_byte}")
        val address = start_byte
        dut.io.request.byte_address #= address

        // verify all transaction sizes work at current address
        for(bytes_in_transaction <- TransactionSize.elements) {
          val bytes_in_transaction_asInt = TransactionSize.defaultEncoding.getValue(bytes_in_transaction).toInt

          // store some data
          if (debug) {println(s"${BLUE}storing $bytes_in_transaction_asInt @ $start_byte ${RESET}")}
          dut.io.request.ldst_req #= TransactionType.STORE
          dut.io.request.size #= bytes_in_transaction
          val test_data = BigInt.apply(8 * bytes_in_transaction_asInt, scala.util.Random)
          dut.io.request.data #= test_data
          dut.clockDomain.waitRisingEdge()
          get_ack()


          // read some data
          if (debug) {println(s"${GREEN}loading $bytes_in_transaction @ $start_byte ${RESET}")}
          dut.io.request.ldst_req #= (TransactionType.LOAD)
          dut.clockDomain.waitRisingEdge()
          get_ack()
          
          // verify results
          if (debug) {println("trying assertion")}
          assert(dut.io.response.data.toBigInt == test_data.toInt)
          }
        }
    }

  }
}

// class NoOverwriteTest extends FlatSpec with ChiselScalatestTester with Matchers {
//   behavior of "Memory Adaptor"

//   it should "test that variable length writes aren't destructive" in {
//     val debug = true

//     test(new MemoryAdaptorWithSram(debug = debug)).withFlags(Array("--t-write-vcd")) { dut =>

//       def get_ack() = {
//         while(dut.io.ack.peek().litValue == 0){
//           if (debug) {println(s"$CYAN_B stepping ack$RESET")}
//           dut.clock.step(1)
//         }
//         dut.io.request.ldst_req.poke(TransactionType.NONE)
//       }

//       val quadword_seq = Seq(0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35).reverse
//       val quadword_bigint = BigInt("202122" + "2324252627282930" + "3132333435", 16)

//       quadword_seq.zipWithIndex.foreach{case(byte, start_byte_address) =>
//         //set address
//         dut.io.request.byte_address.poke(start_byte_address.U)

//         // store some data
//         if (debug) {println(s"${BLUE}storing byte=$byte @ $start_byte_address ${RESET}")}
//         dut.io.request.ldst_req.poke(TransactionType.STORE)
//         dut.io.request.size.poke(TransactionSize.BYTE)
//         dut.io.request.data.poke(byte.U)
//         dut.clock.step(1)
//         get_ack()
//       }

//       // readback data sanity check
//       if (debug) {println(s"${GREEN}loading quadword @ 0x0 ${RESET}")}
//       dut.io.request.byte_address.poke(0.U)
//       dut.io.request.ldst_req.poke(TransactionType.LOAD)
//       dut.io.request.size.poke(TransactionSize.QUADWORD)
//       dut.clock.step(1)
//       get_ack()
      
//       if (debug) {println("trying assertion")}
//       dut.io.response.data.expect(quadword_bigint.asUInt)

//       val double_word_bigint = BigInt("492E5B12701693F6", 16)

//       // write a doubleword in the middle of the first line
//       // specifically at an offset of 5 bytes
//       val offset = 5
//       dut.io.request.byte_address.poke(offset.U)

//       // store some data
//       if (debug) {println(s"${BLUE}storing doubleword=$double_word_bigint @ 5 ${RESET}")}
//       dut.io.request.ldst_req.poke(TransactionType.STORE)
//       dut.io.request.size.poke(TransactionSize.DOUBLEWORD)
//       dut.io.request.data.poke(double_word_bigint.U)
//       dut.clock.step(1)
//       get_ack()

//       // verify results
//       if (debug) {println(s"${GREEN}loading quadword @ 0x0 ${RESET}")}
//       dut.io.request.byte_address.poke(0.U)
//       dut.io.request.ldst_req.poke(TransactionType.LOAD)
//       dut.io.request.size.poke(TransactionSize.QUADWORD)
//       dut.clock.step(1)
//       get_ack()

//       val expected_quadword_bigint = BigInt("202122" + "492E5B12701693F6" + "3132333435", 16)
//       if (debug) {println("trying assertion")}
//       dut.io.response.data.expect(expected_quadword_bigint.asUInt)

//       // store quadword at second memory line
//       if (debug) {println(s"${BLUE}storing doubleword=$double_word_bigint @ 5 ${RESET}")}
//       dut.io.request.byte_address.poke(16.U)
//       dut.io.request.ldst_req.poke(TransactionType.STORE)
//       dut.io.request.size.poke(TransactionSize.DOUBLEWORD)
//       dut.io.request.data.poke(quadword_bigint.U)
//       dut.clock.step(1)
//       get_ack()

//       // load bytes 15 and 16 as a halfword and see if we get 0x3520
//       if (debug) {println(s"${GREEN}loading quadword @ 0x0 ${RESET}")}
//       dut.io.request.byte_address.poke(15.U)
//       dut.io.request.ldst_req.poke(TransactionType.LOAD)
//       dut.io.request.size.poke(TransactionSize.HALFWORD)
//       dut.clock.step(1)
//       get_ack()

//       if (debug) {println("trying assertion")}
//       dut.io.response.data.expect(0x3520.U)
      
//     }
//   }
// }