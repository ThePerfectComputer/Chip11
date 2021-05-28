
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

test_rldicr:
rldicr. 18, 5, 3, 31                              # 22
rldicr. 18, 1, 18, 21                             # 23
rldicr. 18, 3, 26, 15                             # 24
rldicr. 18, 1, 10, 14                             # 25
rldicr. 18, 3, 1, 21                              # 26
nop
li 31, 1
hang: b hang
.section .data
data:
