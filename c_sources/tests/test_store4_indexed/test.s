
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

test_store4_indexed:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
li 18, 0                                          # 24
stwx 8, 17, 18                                    # 25
addi 18, 18, 4                                    # 26
stwx 5, 17, 18                                    # 27
addi 18, 18, 4                                    # 28
stwx 2, 17, 18                                    # 29
addi 18, 18, 4                                    # 30
stwx 5, 17, 18                                    # 31
addi 18, 18, 4                                    # 32
stwx 8, 17, 18                                    # 33
addi 18, 18, 4                                    # 34
stwx 5, 17, 18                                    # 35
addi 18, 18, 4                                    # 36
stwx 8, 17, 18                                    # 37
addi 18, 18, 4                                    # 38
stwx 4, 17, 18                                    # 39
addi 18, 18, 4                                    # 40
lwz 18, 0(17)                                     # 41
lwz 18, 4(17)                                     # 42
lwz 18, 8(17)                                     # 43
lwz 18, 12(17)                                    # 44
lwz 18, 16(17)                                    # 45
lwz 18, 20(17)                                    # 46
lwz 18, 24(17)                                    # 47
lwz 18, 28(17)                                    # 48
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
