package soc.devices

import soc.interfaces.MemBus128
import cpu.shared.memory_state.{TransactionStatus, TransactionType}

import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.collection.mutable.ListBuffer

import java.nio.file.{Files, Paths}
object DebugDualPortSram128 {
  var debug = false
}

class DualPortSram128(depth: Int=8, dataWidth: Int=128, val mem_file:String = null) extends Component {
  val io = new Bundle {
    val port_1 = new MemBus128()
    val port_2 = new MemBus128()
  }
  val bytesPerLine = dataWidth/8

  var initialData = new ListBuffer[BigInt]
  for(i <- 0 until depth)
    initialData += 0
  if(mem_file != null){
    val filePath = Paths.get(mem_file)
    val arr = Files.readAllBytes(filePath)
    for((byte, idx) <- arr.zipWithIndex){
      val line_address = idx / bytesPerLine
      val byte_address = idx % bytesPerLine
      val data = initialData(line_address)
      val newData = BigInt(byte & 0xff) << (byte_address * 8)
      initialData(line_address) = data | newData
    }
  }
  for(item <- initialData){
    println(f"$item%x")
  }


  //val mem = Mem(Bits(dataWidth bits), depth)
  val mem = Mem(Bits(dataWidth bits), initialContent=initialData.map(x => B(x, dataWidth bits)))
  mem.simPublic()
  val address_width = mem.addressWidth

  val port1_do_load  = (io.port_1.ldst_req === TransactionType.LOAD)
  val port1_do_store = (io.port_1.ldst_req === TransactionType.STORE)

  val port2_do_load  = (io.port_2.ldst_req === TransactionType.LOAD)
  val port2_do_store = (io.port_2.ldst_req === TransactionType.STORE)

  val port1_status_reg   = RegInit(TransactionStatus.IDLE)
  val port2_status_reg   = RegInit(TransactionStatus.IDLE)

  io.port_1.status := port1_status_reg
  io.port_2.status := port2_status_reg
  
  // for the fetch stage
  val rport1_data = Bits(dataWidth bits)
  rport1_data := mem.readSync(io.port_1.quad_word_address.take(mem.addressWidth).asUInt, enable=port1_do_load)
  io.port_1.read_data.assignFromBits(rport1_data)
  when(port1_do_load | port1_do_store) {
    port1_status_reg := TransactionStatus.DONE
  }.otherwise{
    port1_status_reg := TransactionStatus.IDLE
  }

  // for the loadstore stage
  when (port2_do_store) {mem.write(io.port_2.quad_word_address.take(mem.addressWidth).asUInt, io.port_2.write_data.asBits, mask=io.port_2.write_mask.asBits)}
  val rport2_data = Bits(dataWidth bits)
  rport2_data := mem.readSync(io.port_2.quad_word_address.take(mem.addressWidth).asUInt, enable=port2_do_load)
  io.port_2.read_data.assignFromBits(rport2_data)
  when(port2_do_load | port2_do_store) {
    port2_status_reg := TransactionStatus.DONE
  }.otherwise{
    port2_status_reg := TransactionStatus.IDLE
  }

  import spinal.core.sim._
  import util.SimHelpers.{vecToStringU, vecToStringB}

  def debugEnabled() : Boolean = DebugDualPortSram128.debug

  var last_store_address_port2 = BigInt(0)
  var last_store_value_port2 = BigInt(0)
  var last_load_address_port2 = BigInt(0)
  var last_load_value_port2 = BigInt(0)

  if (debugEnabled()) {
    mem       simPublic()
    io.port_1 simPublic()
    io.port_2 simPublic()
  }
  
  def debugPort(port : MemBus128){

    if (port.ldst_req.toEnum == TransactionType.LOAD){
      println(s"${port.getName()} RECIEVED ${port.ldst_req.toEnum} REQUEST")
      println(s"${port.read_data.getName()} = ${vecToStringU(port.read_data)}")
      println(s"${port.quad_word_address.getName()} = ${port.quad_word_address.toBigInt}")
    }

    if (port.ldst_req.toEnum == TransactionType.STORE){
      println(s"${port.getName()} RECIEVED ${port.ldst_req.toEnum} REQUEST")
      println(s"${port.read_data.getName()} = ${vecToStringU(port.read_data)}")
      println(s"${port.quad_word_address.getName()} = ${port.quad_word_address.toBigInt}")
    }

  }

  def debug() {
    println("RAM:")
    debugPort(io.port_1)
    debugPort(io.port_2)
  }

  def loadFromFile(fileName:String){
    val filePath = Paths.get(fileName)
    val arr = Files.readAllBytes(filePath)
    var data = BigInt(0)
    for((byte, idx) <- arr.zipWithIndex){
      val line_address = idx / bytesPerLine
      val byte_address = idx % bytesPerLine
      if(byte_address == 0) data = BigInt(0)
      val newData = BigInt(byte & 0xff) << (byte_address * 8)
      data = data | newData
      println(f"$data%x")
      mem.setBigInt(line_address, data)
    }
  }

}
