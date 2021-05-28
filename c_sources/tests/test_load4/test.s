
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

test_load4:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
lwz 18, 0(17)                                     # 24
lwz 18, 4(17)                                     # 25
lwz 18, 8(17)                                     # 26
lwz 18, 12(17)                                    # 27
lwz 18, 16(17)                                    # 28
lwz 18, 20(17)                                    # 29
lwz 18, 24(17)                                    # 30
lwz 18, 28(17)                                    # 31
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.long 0x78ae6383
.long 0x4db132bf
.long 0x24a889ee
.long 0x2f1de983
.long 0x2e2cd230
.long 0x21b1bad0
.long 0x4199cb2f
.long 0x6b4ae169
