
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

test_load2:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
lhz 18, 0(17)                                     # 24
lhz 18, 2(17)                                     # 25
lhz 18, 4(17)                                     # 26
lhz 18, 6(17)                                     # 27
lhz 18, 8(17)                                     # 28
lhz 18, 10(17)                                    # 29
lhz 18, 12(17)                                    # 30
lhz 18, 14(17)                                    # 31
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.short 0xde8
.short 0x6b9c
.short 0x4834
.short 0x266b
.short 0x3ee6
.short 0x1e64
.short 0x618
.short 0x1da8
