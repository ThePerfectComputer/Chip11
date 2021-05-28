
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

test_store8:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
std 5, 0(17)                                      # 24
std 7, 8(17)                                      # 25
std 5, 16(17)                                     # 26
std 6, 24(17)                                     # 27
std 6, 32(17)                                     # 28
std 4, 40(17)                                     # 29
std 5, 48(17)                                     # 30
std 6, 56(17)                                     # 31
ld 18, 0(17)                                      # 32
ld 18, 8(17)                                      # 33
ld 18, 16(17)                                     # 34
ld 18, 24(17)                                     # 35
ld 18, 32(17)                                     # 36
ld 18, 40(17)                                     # 37
ld 18, 48(17)                                     # 38
ld 18, 56(17)                                     # 39
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
