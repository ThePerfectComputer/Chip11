
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

test_store2_update_indexed:
lis 17, (ld_data-2)@h                             # 22
ori 17, 17, (ld_data-2)@l                         # 23
mr 19, 17                                         # 24
li 20, 2                                          # 25
sthux 1, 17, 20                                   # 26
sthux 3, 17, 20                                   # 27
sthux 2, 17, 20                                   # 28
sthux 5, 17, 20                                   # 29
sthux 5, 17, 20                                   # 30
sthux 6, 17, 20                                   # 31
sthux 2, 17, 20                                   # 32
sthux 2, 17, 20                                   # 33
lhzux 18, 19, 20                                  # 34
lhzux 18, 19, 20                                  # 35
lhzux 18, 19, 20                                  # 36
lhzux 18, 19, 20                                  # 37
lhzux 18, 19, 20                                  # 38
lhzux 18, 19, 20                                  # 39
lhzux 18, 19, 20                                  # 40
lhzux 18, 19, 20                                  # 41
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.short 0
.short 0
.short 0
.short 0
.short 0
.short 0
.short 0
.short 0
