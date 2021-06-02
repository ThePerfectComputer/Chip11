package bus
import core.CommonSimConfig
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._
import spinal.lib.bus.amba4.axi.sim._
import spinal.lib.cpu.riscv.impl._
import spinal.sim._

import spinal.core.sim._
import org.scalatest._
import flatspec._
import matchers._


class CacheDemoTest extends AnyFlatSpec with should.Matchers{
  implicit val config = Axi4Ctrl.getAxi4Config
  behavior of "CacheDemo"

  it should "do a thing" in {
    CommonSimConfig().withWave.compile(new CacheDemo).doSim { dut =>
      dut.clockDomain.forkStimulus(250)
      val driver = Axi4Driver(dut.io.axi, dut.clockDomain)
      println("Driver reset")
      driver.reset()

      dut.clockDomain.waitSampling(5)
      dut.clockDomain.assertReset()
      dut.clockDomain.risingEdge()
      sleep(10)
      dut.clockDomain.clockToggle()
      sleep(10)
      dut.clockDomain.risingEdge()
      sleep(10)
      dut.clockDomain.deassertReset()
      sleep(10)
      
      dut.clockDomain.waitSampling(200)

      println("Write initial data")
      // Write a burst of test data to the cache before loading from it
      val burstLen = 32
      val memItems = 32

      for(i <- 0 until memItems/burstLen){
        val lst : List[BigInt] = List.tabulate(burstLen)(n => 0x100 + n)
        driver.writeBurst(i*4*4, lst)
      }
      println("Write finished")


      //dut.io.cpuBus.rsp.ready #= true


      // Read from cache and block until receiving a response
      def readFromCache(address: BigInt) : BigInt = {
        enqueueReadFromCache(address)
        getResponseFromCache()
      }

      // Begin a read from the cache, only block until the cache begins the read
      def enqueueReadFromCache(address: BigInt) {
        dut.io.cpuBus.cmd.address #= address
        dut.io.cpuBus.cmd.valid #= true
        dut.io.cpuBus.cmd.kind #= DataCacheCpuCmdKind.MEMORY
        dut.io.cpuBus.cmd.wr #= false
        dut.io.cpuBus.cmd.bypass #= false
        dut.io.cpuBus.cmd.data #= 0
        dut.io.cpuBus.cmd.all #= false
        dut.clockDomain.waitSamplingWhere(dut.io.cpuBus.cmd.ready.toBoolean)
        dut.io.cpuBus.cmd.valid #= false
      }
      // Retrieve the cache's response
      def getResponseFromCache() : BigInt = {
        dut.clockDomain.waitSamplingWhere(dut.io.cpuBus.rsp.valid.toBoolean)
        return dut.io.cpuBus.rsp.payload.data.toBigInt
      }

      def writeToCache(address: BigInt, data: BigInt, bypass: Boolean=false, mask: Int=0xf){
        dut.io.cpuBus.cmd.address #= address
        dut.io.cpuBus.cmd.valid #= true
        dut.io.cpuBus.cmd.kind #= DataCacheCpuCmdKind.MEMORY
        dut.io.cpuBus.cmd.wr #= true
        dut.io.cpuBus.cmd.bypass #= bypass
        dut.io.cpuBus.cmd.data #= data
        dut.io.cpuBus.cmd.mask #= mask
        dut.io.cpuBus.cmd.all #= false
        dut.clockDomain.waitSamplingWhere(dut.io.cpuBus.cmd.ready.toBoolean)
        dut.io.cpuBus.cmd.valid #= false

      }

      def testCache(base: Int=0x100) {
        println("Begin read test")
        val cmdThread = fork{
          for(i <- 0 until 32){
            val addr = i * 4
            enqueueReadFromCache(addr)
          }
        }

        val respThread = fork {
          for(i <- 0 until 32){
            val expected = base + i
            val resp = getResponseFromCache()
            assert(resp == expected)
          }
        }

        cmdThread.join()
        respThread.join()
        println("End read test")
      }

      def flushCache() {
        dut.io.cpuBus.cmd.address #= 0
        dut.io.cpuBus.cmd.valid #= true
        dut.io.cpuBus.cmd.kind #= DataCacheCpuCmdKind.FLUSH
        dut.io.cpuBus.cmd.all #= true
        dut.io.cpuBus.cmd.wr #= true
        dut.io.cpuBus.cmd.bypass #= false
        dut.io.cpuBus.cmd.data #= 0
        dut.io.cpuBus.cmd.mask #= 15
        dut.clockDomain.waitSamplingWhere(dut.io.cpuBus.cmd.ready.toBoolean)
        dut.io.cpuBus.cmd.valid #= false
      }

      // Test that the cache reads the correct data
      dut.clockDomain.waitSampling(100)
      testCache()

      // Test that the cache doesn't need to reload the data
      dut.clockDomain.waitSampling(100)
      testCache()

      // Write some data to the cache to change the test data and
      // evict it from the cache
      dut.clockDomain.waitSampling(100)
      // Overwrite the test data
      for(i <- 0 until 32){
        writeToCache(i*4, 0xAB00 + i)
      }
      // Flush the data from the cache
      for(i <- 0 until 32){
        writeToCache(0x10000 + i*4, 0)
      }
      dut.clockDomain.waitSampling(50)
      // Test that the test data was written correctly
      testCache(base=0xAB00)
      testCache(base=0xAB00)

      // Do the above again, but force a line flush+reload on each write
      dut.clockDomain.waitSampling(100)
      for(i <- 0 until 32){
        writeToCache(i*4, 0x500 + i)
        writeToCache(0x10000 + i*4, 0xBEEF)
      }
      dut.clockDomain.waitSampling(50)
      testCache(base=0x500)
      testCache(base=0x500)

      // Test that commanding a cache flush works
      flushCache()
      testCache(base=0x500)
      testCache(base=0x500)

      // Write data, bypassing cache
      dut.clockDomain.waitSampling(100)
      for(i <- 0 until 32){
        writeToCache(i*4, 0x800 + i, bypass=true)
      }
      // The data in the cache should still be the old stuff
      testCache(base=0x500)
      // Flush to load new data
      flushCache()
      dut.clockDomain.waitSampling(50)
      // Assert that the new stuff is there
      testCache(base=0x800)

      // Test that the write mask correctly works
      // Write some data leaving the least significant byte untouched
      for(i <- 0 until 32){
        writeToCache(i*4, 0xff00, bypass=false, mask=2)
      }
      testCache(base=0xff00)

      // Apparently the cache writes out dirty data or something?
      flushCache()
      // Do the same as above but bypass the cache and write directly to ram
      for(i <- 0 until 32){
        writeToCache(i*4, 0xee00, bypass=true, mask=0x2)
      }
      // Flush the data so we can read it back correctly
      flushCache()
      testCache(base=0xee00)

    }
  }
}
