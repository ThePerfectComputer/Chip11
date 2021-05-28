
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

test_load4_update_indexed_arithmetic:
lis 17, (ld_data-4)@h                             # 22
ori 17, 17, (ld_data-4)@l                         # 23
li 19, 4                                          # 24
lwaux 18, 17, 19                                  # 25
lwaux 18, 17, 19                                  # 26
lwaux 18, 17, 19                                  # 27
lwaux 18, 17, 19                                  # 28
lwaux 18, 17, 19                                  # 29
lwaux 18, 17, 19                                  # 30
lwaux 18, 17, 19                                  # 31
lwaux 18, 17, 19                                  # 32
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.long 0x53f333ed
.long 0x433de7a0
.long 0x54b6bc5f
.long 0x65560e7e
.long 0x7f0e36f6
.long 0x9cb0d2
.long 0x11588673
.long 0x4f6f4991
