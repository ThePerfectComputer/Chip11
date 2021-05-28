
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

test_store4:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
stw 1, 0(17)                                      # 24
stw 8, 4(17)                                      # 25
stw 7, 8(17)                                      # 26
stw 6, 12(17)                                     # 27
stw 2, 16(17)                                     # 28
stw 7, 20(17)                                     # 29
stw 1, 24(17)                                     # 30
stw 7, 28(17)                                     # 31
lwz 18, 0(17)                                     # 32
lwz 18, 4(17)                                     # 33
lwz 18, 8(17)                                     # 34
lwz 18, 12(17)                                    # 35
lwz 18, 16(17)                                    # 36
lwz 18, 20(17)                                    # 37
lwz 18, 24(17)                                    # 38
lwz 18, 28(17)                                    # 39
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
