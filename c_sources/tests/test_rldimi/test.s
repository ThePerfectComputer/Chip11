
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

test_rldimi:
mr 18, 8                                          # 22
rldimi 18, 7, 4, 5                                # 23
rldimi 18, 2, 4, 26                               # 24
rldimi 18, 5, 6, 23                               # 25
rldimi 18, 3, 3, 21                               # 26
rldimi 18, 5, 8, 7                                # 27
nop
li 31, 1
hang: b hang
.section .data
data:
