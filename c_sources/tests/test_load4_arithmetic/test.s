
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

test_load4_arithmetic:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
lwa 18, 0(17)                                     # 24
lwa 18, 4(17)                                     # 25
lwa 18, 8(17)                                     # 26
lwa 18, 12(17)                                    # 27
lwa 18, 16(17)                                    # 28
lwa 18, 20(17)                                    # 29
lwa 18, 24(17)                                    # 30
lwa 18, 28(17)                                    # 31
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.long 0x4cd29b3
.long 0x1813f41d
.long 0x27c5148f
.long 0x1db325fd
.long 0x725df626
.long 0x8037209
.long 0x2fa20c62
.long 0x3d6913bb
