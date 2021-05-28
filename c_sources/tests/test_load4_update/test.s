
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

test_load4_update:
lis 17, (ld_data-4)@h                             # 22
ori 17, 17, (ld_data-4)@l                         # 23
lwzu 18, 4(17)                                    # 24
lwzu 18, 4(17)                                    # 25
lwzu 18, 4(17)                                    # 26
lwzu 18, 4(17)                                    # 27
lwzu 18, 4(17)                                    # 28
lwzu 18, 4(17)                                    # 29
lwzu 18, 4(17)                                    # 30
lwzu 18, 4(17)                                    # 31
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.long 0x30bea974
.long 0x68f223c5
.long 0x30b92c02
.long 0x781e1749
.long 0x417a2f33
.long 0xf9e2f3c
.long 0x11ea61
.long 0x38aea2ea
