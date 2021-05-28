
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

test_load1:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
lbz 18, 0(17)                                     # 24
lbz 18, 1(17)                                     # 25
lbz 18, 2(17)                                     # 26
lbz 18, 3(17)                                     # 27
lbz 18, 4(17)                                     # 28
lbz 18, 5(17)                                     # 29
lbz 18, 6(17)                                     # 30
lbz 18, 7(17)                                     # 31
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.byte 0x4a
.byte 0x42
.byte 0x32
.byte 0x14
.byte 0x3b
.byte 0x49
.byte 0x49
.byte 0x63
