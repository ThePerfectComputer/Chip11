
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

test_load4_update_indexed:
lis 17, (ld_data-4)@h                             # 22
ori 17, 17, (ld_data-4)@l                         # 23
li 19, 4                                          # 24
lwzux 18, 17, 19                                  # 25
lwzux 18, 17, 19                                  # 26
lwzux 18, 17, 19                                  # 27
lwzux 18, 17, 19                                  # 28
lwzux 18, 17, 19                                  # 29
lwzux 18, 17, 19                                  # 30
lwzux 18, 17, 19                                  # 31
lwzux 18, 17, 19                                  # 32
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.long 0x3d578da4
.long 0x55b6232c
.long 0x4bb059ae
.long 0x27c8cbb7
.long 0x58f8984b
.long 0x429f8c4
.long 0x53e81ad6
.long 0x6ab82cd8
