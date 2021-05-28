
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

test_rlwimi:
mr 18, 8                                          # 22
rlwimi 18, 2, 29, 6, 9                            # 23
rlwimi 18, 2, 3, 20, 8                            # 24
rlwimi 18, 7, 31, 29, 21                          # 25
rlwimi 18, 6, 27, 29, 7                           # 26
rlwimi 18, 5, 20, 14, 28                          # 27
nop
li 31, 1
hang: b hang
.section .data
data:
