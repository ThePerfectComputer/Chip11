
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

test_store8_update_indexed:
lis 17, (ld_data-8)@h                             # 22
ori 17, 17, (ld_data-8)@l                         # 23
mr 19, 17                                         # 24
li 20, 8                                          # 25
stdux 6, 17, 20                                   # 26
stdux 2, 17, 20                                   # 27
stdux 5, 17, 20                                   # 28
stdux 7, 17, 20                                   # 29
stdux 6, 17, 20                                   # 30
stdux 5, 17, 20                                   # 31
stdux 4, 17, 20                                   # 32
stdux 6, 17, 20                                   # 33
ldux 18, 19, 20                                   # 34
ldux 18, 19, 20                                   # 35
ldux 18, 19, 20                                   # 36
ldux 18, 19, 20                                   # 37
ldux 18, 19, 20                                   # 38
ldux 18, 19, 20                                   # 39
ldux 18, 19, 20                                   # 40
ldux 18, 19, 20                                   # 41
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
