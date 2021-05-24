#!/bin/bash

cd $(dirname $0)

dirs=$(ls -d */)
echo "dirs: $dirs"
make -B BE
for dir in $dirs; do
    if [ -f "$dir/test_be.bin" ]; then
	./run_qemu.sh $dir/test_be.bin $dir/test.csv
    fi
done
