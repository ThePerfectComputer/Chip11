
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

test_store1:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
stb 8, 0(17)                                      # 24
stb 3, 1(17)                                      # 25
stb 2, 2(17)                                      # 26
stb 6, 3(17)                                      # 27
stb 8, 4(17)                                      # 28
stb 7, 5(17)                                      # 29
stb 1, 6(17)                                      # 30
stb 6, 7(17)                                      # 31
lbz 18, 0(17)                                     # 32
lbz 18, 1(17)                                     # 33
lbz 18, 2(17)                                     # 34
lbz 18, 3(17)                                     # 35
lbz 18, 4(17)                                     # 36
lbz 18, 5(17)                                     # 37
lbz 18, 6(17)                                     # 38
lbz 18, 7(17)                                     # 39
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
