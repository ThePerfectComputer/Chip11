
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

test_rlwinm:
rlwinm. 18, 4, 2, 31, 11                          # 22
rlwinm. 18, 2, 6, 6, 19                           # 23
rlwinm. 18, 6, 15, 5, 16                          # 24
rlwinm. 18, 3, 31, 27, 24                         # 25
rlwinm. 18, 3, 23, 3, 1                           # 26
nop
li 31, 1
hang: b hang
.section .data
data:
