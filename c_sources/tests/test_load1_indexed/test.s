
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

test_load1_indexed:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
li 18, 0                                          # 24
lbzx 19, 17, 18                                   # 25
addi 18, 18, 1                                    # 26
lbzx 19, 17, 18                                   # 27
addi 18, 18, 1                                    # 28
lbzx 19, 17, 18                                   # 29
addi 18, 18, 1                                    # 30
lbzx 19, 17, 18                                   # 31
addi 18, 18, 1                                    # 32
lbzx 19, 17, 18                                   # 33
addi 18, 18, 1                                    # 34
lbzx 19, 17, 18                                   # 35
addi 18, 18, 1                                    # 36
lbzx 19, 17, 18                                   # 37
addi 18, 18, 1                                    # 38
lbzx 19, 17, 18                                   # 39
addi 18, 18, 1                                    # 40
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.byte 0x6
.byte 0xe
.byte 0x54
.byte 0x14
.byte 0x7a
.byte 0x61
.byte 0xc
.byte 0x61
