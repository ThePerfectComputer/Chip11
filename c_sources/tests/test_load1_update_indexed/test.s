
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

test_load1_update_indexed:
lis 17, (ld_data-1)@h                             # 22
ori 17, 17, (ld_data-1)@l                         # 23
li 19, 1                                          # 24
lbzux 18, 17, 19                                  # 25
lbzux 18, 17, 19                                  # 26
lbzux 18, 17, 19                                  # 27
lbzux 18, 17, 19                                  # 28
lbzux 18, 17, 19                                  # 29
lbzux 18, 17, 19                                  # 30
lbzux 18, 17, 19                                  # 31
lbzux 18, 17, 19                                  # 32
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.byte 0x46
.byte 0x9
.byte 0x1
.byte 0x25
.byte 0x7
.byte 0x17
.byte 0x16
.byte 0x5
