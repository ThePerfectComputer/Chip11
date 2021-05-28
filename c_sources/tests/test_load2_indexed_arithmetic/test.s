
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

test_load2_indexed_arithmetic:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
li 18, 0                                          # 24
lhax 19, 17, 18                                   # 25
addi 18, 18, 2                                    # 26
lhax 19, 17, 18                                   # 27
addi 18, 18, 2                                    # 28
lhax 19, 17, 18                                   # 29
addi 18, 18, 2                                    # 30
lhax 19, 17, 18                                   # 31
addi 18, 18, 2                                    # 32
lhax 19, 17, 18                                   # 33
addi 18, 18, 2                                    # 34
lhax 19, 17, 18                                   # 35
addi 18, 18, 2                                    # 36
lhax 19, 17, 18                                   # 37
addi 18, 18, 2                                    # 38
lhax 19, 17, 18                                   # 39
addi 18, 18, 2                                    # 40
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.short 0x5911
.short 0x7b44
.short 0x2acc
.short 0x7e97
.short 0x1fa8
.short 0x73b2
.short 0x7d4d
.short 0x4da4
