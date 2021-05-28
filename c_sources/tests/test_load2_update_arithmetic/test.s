
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

test_load2_update_arithmetic:
lis 17, (ld_data-2)@h                             # 22
ori 17, 17, (ld_data-2)@l                         # 23
lhau 18, 2(17)                                    # 24
lhau 18, 2(17)                                    # 25
lhau 18, 2(17)                                    # 26
lhau 18, 2(17)                                    # 27
lhau 18, 2(17)                                    # 28
lhau 18, 2(17)                                    # 29
lhau 18, 2(17)                                    # 30
lhau 18, 2(17)                                    # 31
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.short 0x7483
.short 0x1f8f
.short 0x5dd5
.short 0x1a81
.short 0x77c0
.short 0x453c
.short 0x111f
.short 0x72e7
