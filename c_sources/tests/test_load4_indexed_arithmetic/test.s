
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

test_load4_indexed_arithmetic:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
li 18, 0                                          # 24
lwzx 19, 17, 18                                   # 25
addi 18, 18, 4                                    # 26
lwzx 19, 17, 18                                   # 27
addi 18, 18, 4                                    # 28
lwzx 19, 17, 18                                   # 29
addi 18, 18, 4                                    # 30
lwzx 19, 17, 18                                   # 31
addi 18, 18, 4                                    # 32
lwzx 19, 17, 18                                   # 33
addi 18, 18, 4                                    # 34
lwzx 19, 17, 18                                   # 35
addi 18, 18, 4                                    # 36
lwzx 19, 17, 18                                   # 37
addi 18, 18, 4                                    # 38
lwzx 19, 17, 18                                   # 39
addi 18, 18, 4                                    # 40
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.long 0x751569df
.long 0x8f5896c
.long 0x5c11af38
.long 0x322cd01e
.long 0x3cb2ed84
.long 0x42219886
.long 0x661c8f47
.long 0x73c43f43
