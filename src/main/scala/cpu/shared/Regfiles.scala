package cpu.shared

import cpu.interfaces.regfile.{ReadPort, WritePort, WritePortMasked}

import spinal.core._
import spinal.lib._

class Regfile(val numRegs: Int, val regWidth: Int, val readPorts:Int, val writePorts:Int) extends Component {
  val idxWidth = log2Up(numRegs)
  val io = new Bundle{
    val rp = Vec(slave(new ReadPort(idxWidth, regWidth)), readPorts)

    val wp = Vec(slave(new WritePort(idxWidth, regWidth)), writePorts)
  }

  val mem = Mem(UInt(regWidth bits), numRegs)

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

  val mem = Mem(Bits(regWidth bits), numRegs)

  for(port <- io.rp) {
    port.data := mem.readSync(port.idx).asBits.asUInt
  }
  for(port <- io.wp) {
    mem.write(port.idx, port.data.asBits, enable=port.en, mask=port.mask.asBits)
  }
}


// Another Multi-Port regfile that should synthesize a bit better
class BRAMMultiRegfile(numRegs: Int, regWidth: Int, readPorts:Int, writePorts:Int) extends
    Regfile(numRegs, regWidth, readPorts, writePorts){

  val memIdxBits = log2Up(writePorts)
  val memArr = for (i <- 0 until writePorts) yield Mem(UInt(regWidth bits), numRegs)


  // Create a table holding the memory that was most recently written to for each register
  val lvt = Mem(UInt(memIdxBits bits), numRegs)


  for((port, i) <- io.rp.zipWithIndex) {
    val mem_idx = UInt(memIdxBits bits)
    mem_idx := lvt.readSync(port.idx)

    val read_data = Vec(UInt(regWidth bits), writePorts)
    for((mem, idx) <- memArr.zipWithIndex) read_data(idx) := mem.readSync(port.idx)

    port.data.allowOverride
    port.data := read_data(mem_idx)
  }

  for((port, i) <- io.wp.zipWithIndex) {
    lvt.write(port.idx, i)
    memArr(i).write(port.idx, port.data, enable=port.en)
  }
}
