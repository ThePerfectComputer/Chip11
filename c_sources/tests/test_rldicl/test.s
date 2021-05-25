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

test_rldicl:
rldicl. 18, 8, 17, 9                              # 20
rldicl. 18, 5, 25, 21                             # 21
rldicl. 18, 7, 4, 4                               # 22
rldicl. 18, 6, 11, 28                             # 23
rldicl. 18, 4, 4, 30                              # 24
nop
li 31, 1
hang: b hang
