
.section .text
.org 0
b _start
.org 0x10
_start:
generate_data:
lis 1, 0xdead,
ori 1, 1, 0xbeef
lis 2, 0x1234
ori 2, 2, 0x5678
li 3, 0x1000
li 4, -0x1000
li 5, -0x5a5a
li 6, 0x5a5a
li 7, -1
lis 8, 0x1234
ori 8, 8, 0x5678
sldi 8, 8, 32
oris 8, 8, 0x9abc
ori 8, 8, 0xdef0
mtcr 1

test_load2_update_indexed_arithmetic:
lis 17, (ld_data-2)@h                             # 22
ori 17, 17, (ld_data-2)@l                         # 23
li 19, 2                                          # 24
lhaux 18, 17, 19                                  # 25
lhaux 18, 17, 19                                  # 26
lhaux 18, 17, 19                                  # 27
lhaux 18, 17, 19                                  # 28
lhaux 18, 17, 19                                  # 29
lhaux 18, 17, 19                                  # 30
lhaux 18, 17, 19                                  # 31
lhaux 18, 17, 19                                  # 32
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.short 0x4efd
.short 0x7d14
.short 0x299f
.short 0x4c6b
.short 0xed4
.short 0x564a
.short 0x6741
.short 0xbe1
