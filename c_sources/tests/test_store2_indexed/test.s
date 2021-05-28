
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

test_store2_indexed:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
li 18, 0                                          # 24
sthx 5, 17, 18                                    # 25
addi 18, 18, 2                                    # 26
sthx 4, 17, 18                                    # 27
addi 18, 18, 2                                    # 28
sthx 3, 17, 18                                    # 29
addi 18, 18, 2                                    # 30
sthx 3, 17, 18                                    # 31
addi 18, 18, 2                                    # 32
sthx 6, 17, 18                                    # 33
addi 18, 18, 2                                    # 34
sthx 2, 17, 18                                    # 35
addi 18, 18, 2                                    # 36
sthx 6, 17, 18                                    # 37
addi 18, 18, 2                                    # 38
sthx 5, 17, 18                                    # 39
addi 18, 18, 2                                    # 40
lhz 18, 0(17)                                     # 41
lhz 18, 2(17)                                     # 42
lhz 18, 4(17)                                     # 43
lhz 18, 6(17)                                     # 44
lhz 18, 8(17)                                     # 45
lhz 18, 10(17)                                    # 46
lhz 18, 12(17)                                    # 47
lhz 18, 14(17)                                    # 48
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
