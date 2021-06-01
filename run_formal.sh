#!/bin/bash
set -e
set -x
sbt "test:testOnly *Formal"
 
pushd formal

for f in *.sby; do
	sby -f $f
done
