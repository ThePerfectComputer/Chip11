
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

test_load8_indexed:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
li 18, 0                                          # 24
ldx 19, 17, 18                                    # 25
addi 18, 18, 8                                    # 26
ldx 19, 17, 18                                    # 27
addi 18, 18, 8                                    # 28
ldx 19, 17, 18                                    # 29
addi 18, 18, 8                                    # 30
ldx 19, 17, 18                                    # 31
addi 18, 18, 8                                    # 32
ldx 19, 17, 18                                    # 33
addi 18, 18, 8                                    # 34
ldx 19, 17, 18                                    # 35
addi 18, 18, 8                                    # 36
ldx 19, 17, 18                                    # 37
addi 18, 18, 8                                    # 38
ldx 19, 17, 18                                    # 39
addi 18, 18, 8                                    # 40
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.quad 0x7def964669c98bde
.quad 0x6718825a3658cf7f
.quad 0x227f230be8a34235
.quad 0x5efece34c09f100f
.quad 0x60e3abfe6760791a
.quad 0x71c9fb970eb4870b
.quad 0x57e815d0612d02d5
.quad 0x3b33e7b0791e22c1
