
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

test_load2_update:
lis 17, (ld_data-2)@h                             # 22
ori 17, 17, (ld_data-2)@l                         # 23
lhzu 18, 2(17)                                    # 24
lhzu 18, 2(17)                                    # 25
lhzu 18, 2(17)                                    # 26
lhzu 18, 2(17)                                    # 27
lhzu 18, 2(17)                                    # 28
lhzu 18, 2(17)                                    # 29
lhzu 18, 2(17)                                    # 30
lhzu 18, 2(17)                                    # 31
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.short 0x3e
.short 0x5ca2
.short 0x113c
.short 0x317e
.short 0x2944
.short 0x4ac7
.short 0x53a4
.short 0x75fe
