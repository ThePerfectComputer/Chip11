package cpu.shared

import cpu.interfaces.regfile.{ReadPort, WritePort, WritePortMasked}

import spinal.core._
import spinal.lib._
import spinal.core.sim._

class Regfile(val numRegs: Int, val regWidth: Int, val readPorts:Int=1, val writePorts:Int=1, initData:Seq[BigInt]=null) extends Component {
  val idxWidth = log2Up(numRegs)
  val io = new Bundle{
    val rp = Vec(slave(new ReadPort(idxWidth, regWidth)), readPorts)

    val wp = Vec(slave(new WritePort(idxWidth, regWidth)), writePorts)
  }

  val initialData = if(initData != null){
    initData.map(x => U(x, regWidth bits))
  }else Seq.fill(numRegs)(U(0, regWidth bits))

  val mem = Mem(UInt(regWidth bits), numRegs) init(initialData)
  mem.simPublic()

  for(port <- io.rp) {
    port.data := mem.readSync(port.idx)
  }
  for(port <- io.wp) {
    mem.write(port.idx, port.data, enable=port.en)
  }
}

class RegfileMasked(val numRegs: Int, val regWidth: Int, val readPorts:Int, val writePorts:Int, val maskWidth: Int) extends Component {
  val idxWidth = log2Up(numRegs)

  val elemWidth = regWidth/maskWidth // The width of each element that can be masked

  val io = new Bundle{
    val rp = Vec(slave(new ReadPort(idxWidth, regWidth)), readPorts)

    val wp = Vec(slave(new WritePortMasked(idxWidth, regWidth, maskWidth)), writePorts)
  }

  val mem = Mem(Bits(regWidth bits), numRegs).simPublic()

  for(port <- io.rp) {
    port.data := mem.readSync(port.idx).asBits.asUInt
  }
  for(port <- io.wp) {
    mem.write(port.idx, port.data.asBits, enable=port.en, mask=port.mask.asBits)
  }
}


// Another Multi-Port regfile that should synthesize a bit better
class BRAMMultiRegfile(numRegs: Int, regWidth: Int, readPorts:Int=1, writePorts:Int=1, initData:Seq[BigInt]=null) extends
    Regfile(numRegs, regWidth, readPorts, writePorts, initData){

  val memIdxBits = log2Up(writePorts)
  println(memIdxBits)
  val memArr = for (i <- 0 until writePorts) yield Mem(UInt(regWidth bits), numRegs)
  for(i <- 0 until writePorts){
    memArr(i) init(initialData)
  }


  // Create a table holding the memory that was most recently written to for each register
  val lvt = Mem(UInt(memIdxBits bits), numRegs)


  for((port, i) <- io.rp.zipWithIndex) {
    val mem_idx = UInt(memIdxBits bits).setName(s"memIdx_$i")
    mem_idx := lvt.readSync(port.idx)

    val read_data = Vec(UInt(regWidth bits), writePorts)
    for((mem, idx) <- memArr.zipWithIndex) read_data(idx) := mem.readSync(port.idx)

    val data = UInt(regWidth bits).setName(s"read_data_$i")
    data := 0
    // So for some reason, vec doesn't seem to support a dynamic index?
    //data := read_data(mem_idx)
    for(idx <- 0 until writePorts){
      when(mem_idx === idx){
        data := read_data(idx)
      }
    }

    port.data.allowOverride
    port.data := data
  }

  for((port, i) <- io.wp.zipWithIndex) {
    val idx = UInt(memIdxBits bits).setName(s"writeIdx_$i")
    idx := i
    lvt.write(port.idx, idx)
    memArr(i).write(port.idx, port.data, enable=port.en)
  }
}
