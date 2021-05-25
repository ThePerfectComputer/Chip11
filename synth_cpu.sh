#!/bin/bash
sbt "test:testOnly *SoCTestVerilog"
yosys -p 'synth_ecp5 -json cpu.json' CPU.v
nextpnr-ecp5 --json cpu.json --out-of-context --85k --freq 100
