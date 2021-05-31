#!/bin/bash
set -e
set -x

sbt "test:testOnly *SoCTestVerilog"

MODULES="${1:-Stage1 Stage2 Stage3 ReadStage WriteStage HazardDetector PopulateByForm uOpAndFormDecoderMulti uOpAndFormDecoderMux}"

rm -rf ltp_output
mkdir -p ltp_output
SCRIPTFILE=$(mktemp)

trap 'rm -f "$SCRIPTFILE"' EXIT


echo "read_verilog CPU.v;" >> $SCRIPTFILE
echo "design -save cpu" >> $SCRIPTFILE
for mod in $MODULES; do
    echo "design -load cpu;" >> $SCRIPTFILE
    echo "prep -top $mod; synth_ecp5" >> $SCRIPTFILE
    echo "tee -o ltp_output/$mod.txt ltp" >> $SCRIPTFILE
done
cat $SCRIPTFILE
yosys -q -s $SCRIPTFILE
