#!/bin/bash
set -e
sbt "test:testOnly *HazardDetectorVerilog"
yosys -p 'synth_ecp5 -json hazard.json -abc9' HazardDUT.v
nextpnr-ecp5 --json hazard.json --out-of-context --85k --freq 100 --router router2 2>&1 | tee pnr.log
