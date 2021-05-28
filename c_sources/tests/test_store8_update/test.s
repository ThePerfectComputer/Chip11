
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

test_store8_update:
lis 17, (ld_data-8)@h                             # 22
ori 17, 17, (ld_data-8)@l                         # 23
mr 19, 17                                         # 24
stdu 3, 8(17)                                     # 25
stdu 4, 8(17)                                     # 26
stdu 4, 8(17)                                     # 27
stdu 5, 8(17)                                     # 28
stdu 2, 8(17)                                     # 29
stdu 6, 8(17)                                     # 30
stdu 8, 8(17)                                     # 31
stdu 8, 8(17)                                     # 32
ldu 18, 8(19)                                     # 33
ldu 18, 8(19)                                     # 34
ldu 18, 8(19)                                     # 35
ldu 18, 8(19)                                     # 36
ldu 18, 8(19)                                     # 37
ldu 18, 8(19)                                     # 38
ldu 18, 8(19)                                     # 39
ldu 18, 8(19)                                     # 40
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.quad 0
.quad 0
.quad 0
.quad 0
.quad 0
.quad 0
.quad 0
.quad 0
