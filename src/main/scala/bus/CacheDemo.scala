package bus
import spinal.core._
import spinal.lib._
import spinal.lib.com.uart._
import scala.math.BigInt

import spinal.lib.bus.amba4.axi._

import spinal.core.sim._

object CacheConfig {
  // def getICacheConfig = InstructionCacheConfig(
  //   cacheSize=2048,
  //   bytePerLine=16,
  //   wayCount=1,  
  //   wrappedMemAccess=false,
  //   addressWidth=32,
  //   cpuDataWidth=32,
  //   memDataWidth=32)
  def getDCacheConfig = DataCacheConfig(
    cacheSize=2048,
    bytePerLine=16,
    wayCount=1,  
    addressWidth=32,
    cpuDataWidth=32,
    memDataWidth=32)

}

class CacheDemo(implicit axiConfig: Axi4Config) extends Component{
  // this is WEIRD AAAAAAA
  implicit val config = CacheConfig.getDCacheConfig

  val io = new Bundle {
    val cpuBus = slave(DataCacheCpuBus())
    val axi = slave(Axi4(axiConfig))
  }

  val cache = new DataCache()

  cache.io.cpu <> io.cpuBus
  val cacheAxi = cache.io.mem.toAxi4Shared()

  // cache.io.flush.cmd.valid := False

  val ramSize = 16384
  val ram = Axi4SharedOnChipRam (
    dataWidth = 32,
    byteCount = ramSize,
    idWidth = 5
  )

  val axiCrossbar = Axi4CrossbarFactory()

  axiCrossbar.addSlaves(
    ram.io.axi -> (0x0, ramSize)
  )

  axiCrossbar.addConnections(
    cacheAxi -> List(ram.io.axi),
    io.axi -> List(ram.io.axi)
  )
  axiCrossbar.build()
}
