
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

test_load2_update_indexed:
lis 17, (ld_data-2)@h                             # 22
ori 17, 17, (ld_data-2)@l                         # 23
li 19, 2                                          # 24
lhzux 18, 17, 19                                  # 25
lhzux 18, 17, 19                                  # 26
lhzux 18, 17, 19                                  # 27
lhzux 18, 17, 19                                  # 28
lhzux 18, 17, 19                                  # 29
lhzux 18, 17, 19                                  # 30
lhzux 18, 17, 19                                  # 31
lhzux 18, 17, 19                                  # 32
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.short 0x757e
.short 0x6eed
.short 0x7915
.short 0x40ed
.short 0x7ce1
.short 0x66cb
.short 0x4ee8
.short 0x34b9
