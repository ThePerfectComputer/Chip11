
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

test_store4_update:
lis 17, (ld_data-4)@h                             # 22
ori 17, 17, (ld_data-4)@l                         # 23
mr 19, 17                                         # 24
stwu 7, 4(17)                                     # 25
stwu 5, 4(17)                                     # 26
stwu 7, 4(17)                                     # 27
stwu 2, 4(17)                                     # 28
stwu 1, 4(17)                                     # 29
stwu 5, 4(17)                                     # 30
stwu 7, 4(17)                                     # 31
stwu 2, 4(17)                                     # 32
lwzu 18, 4(19)                                    # 33
lwzu 18, 4(19)                                    # 34
lwzu 18, 4(19)                                    # 35
lwzu 18, 4(19)                                    # 36
lwzu 18, 4(19)                                    # 37
lwzu 18, 4(19)                                    # 38
lwzu 18, 4(19)                                    # 39
lwzu 18, 4(19)                                    # 40
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.long 0
.long 0
.long 0
.long 0
.long 0
.long 0
.long 0
.long 0
