#!/bin/bash
set -x
BINARY=$1
qemu-system-ppc64 -machine powernv9 -nographic -s -S -bios "$BINARY" &
QEMU_PID=$!

powerpc64-linux-gnu-gdb -batch -x gdbscript -q
kill $QEMU_PID
sed -i -e '/()/d' -e '/\[Inferior/d' test.csv
mv test.csv $2
