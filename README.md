# About
 - SpinalHDL design which compiles to Verilog or VHDL of a PowerPC 3.0B processor that implements just enough instructions to run micropython.
 - Fmax of ~50Mhz on ECP5 if I remember correctly.
 - Uses custom branch of SpinalHDL.
 - At this point, I don't plan to complete this processor, but rather, plan to do a new implementation in BlueSpec. This [paper](https://github.com/bluespec/Toooba/blob/master/Doc/micro2018.pdf) demonstrates the power of bluespec for out-of-order processor design.

# Getting Started

## Install SpinalHDL Branch Locally


``` bash
cd ../
git clone git@gitlab.com:chipeleven/spinalhdl.git SpinalHDL
cd SpinalHDL
git checkout onehot-switch
sbt clean publishLocal
```

## Test

``` bash
sbt test
gtkwave waves/BusTimer/test.vcd
```

## Some Specific Tests
```
sbt "testOnly util.PipeStageTest"
```
