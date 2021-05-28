
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

test_store8_indexed:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
li 18, 0                                          # 24
stdx 1, 17, 18                                    # 25
addi 18, 18, 8                                    # 26
stdx 8, 17, 18                                    # 27
addi 18, 18, 8                                    # 28
stdx 7, 17, 18                                    # 29
addi 18, 18, 8                                    # 30
stdx 2, 17, 18                                    # 31
addi 18, 18, 8                                    # 32
stdx 6, 17, 18                                    # 33
addi 18, 18, 8                                    # 34
stdx 8, 17, 18                                    # 35
addi 18, 18, 8                                    # 36
stdx 6, 17, 18                                    # 37
addi 18, 18, 8                                    # 38
stdx 4, 17, 18                                    # 39
addi 18, 18, 8                                    # 40
ld 18, 0(17)                                      # 41
ld 18, 8(17)                                      # 42
ld 18, 16(17)                                     # 43
ld 18, 24(17)                                     # 44
ld 18, 32(17)                                     # 45
ld 18, 40(17)                                     # 46
ld 18, 48(17)                                     # 47
ld 18, 56(17)                                     # 48
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
