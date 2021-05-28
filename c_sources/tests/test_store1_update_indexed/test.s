
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

test_store1_update_indexed:
lis 17, (ld_data-1)@h                             # 22
ori 17, 17, (ld_data-1)@l                         # 23
mr 19, 17                                         # 24
li 20, 1                                          # 25
stbux 2, 17, 20                                   # 26
stbux 5, 17, 20                                   # 27
stbux 8, 17, 20                                   # 28
stbux 7, 17, 20                                   # 29
stbux 7, 17, 20                                   # 30
stbux 2, 17, 20                                   # 31
stbux 5, 17, 20                                   # 32
stbux 7, 17, 20                                   # 33
lbzux 18, 19, 20                                  # 34
lbzux 18, 19, 20                                  # 35
lbzux 18, 19, 20                                  # 36
lbzux 18, 19, 20                                  # 37
lbzux 18, 19, 20                                  # 38
lbzux 18, 19, 20                                  # 39
lbzux 18, 19, 20                                  # 40
lbzux 18, 19, 20                                  # 41
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
