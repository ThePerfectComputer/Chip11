#!/bin/bash
set -e
pushd ../
sbt "test:testOnly *SoCWithUARTTest"
popd
yosys -p 'synth_ecp5 -json soc.json -retime' ../SoCWithUART.v 2>&1 | tee synth.log
nextpnr-ecp5 --json soc.json --package CABGA381 --85k --freq 25 --router router2 --lpf ulx3s.lpf --textcfg soc.config 2>&1 | tee pnr.log
ecppack soc.config soc.bit
