# Getting Started

## Install SpinalHDL Locally


``` bash
cd ../
git clone git@gitlab.com:chipeleven/spinalhdl.git SpinalHDL
cd SpinalHDL
sbt clean publishLocal
```

## Test

``` bash
sbt test
gtkwave waves/BusTimer/test.vcd
```
