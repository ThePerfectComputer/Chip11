
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

test_store2:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
sth 3, 0(17)                                      # 24
sth 2, 2(17)                                      # 25
sth 8, 4(17)                                      # 26
sth 8, 6(17)                                      # 27
sth 6, 8(17)                                      # 28
sth 1, 10(17)                                     # 29
sth 7, 12(17)                                     # 30
sth 3, 14(17)                                     # 31
lhz 18, 0(17)                                     # 32
lhz 18, 2(17)                                     # 33
lhz 18, 4(17)                                     # 34
lhz 18, 6(17)                                     # 35
lhz 18, 8(17)                                     # 36
lhz 18, 10(17)                                    # 37
lhz 18, 12(17)                                    # 38
lhz 18, 14(17)                                    # 39
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
