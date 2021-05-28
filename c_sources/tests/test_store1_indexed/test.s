
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

test_store1_indexed:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
li 18, 0                                          # 24
stbx 6, 17, 18                                    # 25
addi 18, 18, 1                                    # 26
stbx 7, 17, 18                                    # 27
addi 18, 18, 1                                    # 28
stbx 7, 17, 18                                    # 29
addi 18, 18, 1                                    # 30
stbx 7, 17, 18                                    # 31
addi 18, 18, 1                                    # 32
stbx 6, 17, 18                                    # 33
addi 18, 18, 1                                    # 34
stbx 3, 17, 18                                    # 35
addi 18, 18, 1                                    # 36
stbx 2, 17, 18                                    # 37
addi 18, 18, 1                                    # 38
stbx 7, 17, 18                                    # 39
addi 18, 18, 1                                    # 40
lbz 18, 0(17)                                     # 41
lbz 18, 1(17)                                     # 42
lbz 18, 2(17)                                     # 43
lbz 18, 3(17)                                     # 44
lbz 18, 4(17)                                     # 45
lbz 18, 5(17)                                     # 46
lbz 18, 6(17)                                     # 47
lbz 18, 7(17)                                     # 48
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.byte 0
.byte 0
.byte 0
.byte 0
.byte 0
.byte 0
.byte 0
.byte 0
