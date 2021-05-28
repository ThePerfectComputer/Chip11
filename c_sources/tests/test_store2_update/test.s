
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

test_store2_update:
lis 17, (ld_data-2)@h                             # 22
ori 17, 17, (ld_data-2)@l                         # 23
mr 19, 17                                         # 24
sthu 3, 2(17)                                     # 25
sthu 3, 2(17)                                     # 26
sthu 5, 2(17)                                     # 27
sthu 1, 2(17)                                     # 28
sthu 4, 2(17)                                     # 29
sthu 7, 2(17)                                     # 30
sthu 2, 2(17)                                     # 31
sthu 8, 2(17)                                     # 32
lhzu 18, 2(19)                                    # 33
lhzu 18, 2(19)                                    # 34
lhzu 18, 2(19)                                    # 35
lhzu 18, 2(19)                                    # 36
lhzu 18, 2(19)                                    # 37
lhzu 18, 2(19)                                    # 38
lhzu 18, 2(19)                                    # 39
lhzu 18, 2(19)                                    # 40
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.short 0
.short 0
.short 0
.short 0
.short 0
.short 0
.short 0
.short 0
