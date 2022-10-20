# Getting Started

## Install SpinalHDL Locally


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
