
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

test_store1_update:
lis 17, (ld_data-1)@h                             # 22
ori 17, 17, (ld_data-1)@l                         # 23
mr 19, 17                                         # 24
stbu 7, 1(17)                                     # 25
stbu 7, 1(17)                                     # 26
stbu 1, 1(17)                                     # 27
stbu 4, 1(17)                                     # 28
stbu 4, 1(17)                                     # 29
stbu 4, 1(17)                                     # 30
stbu 6, 1(17)                                     # 31
stbu 8, 1(17)                                     # 32
lbzu 18, 1(19)                                    # 33
lbzu 18, 1(19)                                    # 34
lbzu 18, 1(19)                                    # 35
lbzu 18, 1(19)                                    # 36
lbzu 18, 1(19)                                    # 37
lbzu 18, 1(19)                                    # 38
lbzu 18, 1(19)                                    # 39
lbzu 18, 1(19)                                    # 40
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.byte 0
.byte 0
.byte 0
.byte 0
.byte 0
.byte 0
.byte 0
.byte 0
