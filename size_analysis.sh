#!/bin/bash
set -e
set -x

sbt "test:testOnly *SoCTestVerilog"

MODULES="${1:-Stage1 Stage2 Stage3 uOpAndFormDecoderMulti uOpAndFormDecoderMux}"

DIR="size_output"

rm -rf "$DIR"
mkdir -p "$DIR"
SCRIPTFILE=$(mktemp)

trap 'rm -f "$SCRIPTFILE"' EXIT


echo "read_verilog CPU.v;" >> $SCRIPTFILE
echo "design -save cpu" >> $SCRIPTFILE
for mod in $MODULES; do
    echo "design -load cpu;" >> $SCRIPTFILE
    echo "prep -top $mod; synth_ecp5" >> $SCRIPTFILE
    echo "tee -o $DIR/$mod.txt stat" >> $SCRIPTFILE
done
cat $SCRIPTFILE
yosys -q -s $SCRIPTFILE
