
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

test_load8_update_indexed:
lis 17, (ld_data-8)@h                             # 22
ori 17, 17, (ld_data-8)@l                         # 23
li 19, 8                                          # 24
ldux 18, 17, 19                                   # 25
ldux 18, 17, 19                                   # 26
ldux 18, 17, 19                                   # 27
ldux 18, 17, 19                                   # 28
ldux 18, 17, 19                                   # 29
ldux 18, 17, 19                                   # 30
ldux 18, 17, 19                                   # 31
ldux 18, 17, 19                                   # 32
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.quad 0x74b9ac4ff86f32bd
.quad 0x640c4af8b4190c37
.quad 0x4a5072d1b12e1ea1
.quad 0x3e2d5047846a8c87
.quad 0x17ba503ded8ab285
.quad 0x241a69c1ecfe7644
.quad 0x7743ed540feda7e3
.quad 0x6eb64cb4e93f5128
