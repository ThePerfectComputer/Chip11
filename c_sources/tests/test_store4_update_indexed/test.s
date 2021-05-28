
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

test_store4_update_indexed:
lis 17, (ld_data-4)@h                             # 22
ori 17, 17, (ld_data-4)@l                         # 23
mr 19, 17                                         # 24
li 20, 4                                          # 25
stwux 2, 17, 20                                   # 26
stwux 7, 17, 20                                   # 27
stwux 7, 17, 20                                   # 28
stwux 1, 17, 20                                   # 29
stwux 8, 17, 20                                   # 30
stwux 3, 17, 20                                   # 31
stwux 7, 17, 20                                   # 32
stwux 7, 17, 20                                   # 33
lwzux 18, 19, 20                                  # 34
lwzux 18, 19, 20                                  # 35
lwzux 18, 19, 20                                  # 36
lwzux 18, 19, 20                                  # 37
lwzux 18, 19, 20                                  # 38
lwzux 18, 19, 20                                  # 39
lwzux 18, 19, 20                                  # 40
lwzux 18, 19, 20                                  # 41
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
