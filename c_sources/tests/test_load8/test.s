
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

test_load8:
lis 17, ld_data@h                                 # 22
ori 17, 17, ld_data@l                             # 23
ld 18, 0(17)                                      # 24
ld 18, 8(17)                                      # 25
ld 18, 16(17)                                     # 26
ld 18, 24(17)                                     # 27
ld 18, 32(17)                                     # 28
ld 18, 40(17)                                     # 29
ld 18, 48(17)                                     # 30
ld 18, 56(17)                                     # 31
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.quad 0x752d8772f82c7970
.quad 0x1b9eb6ecd4748779
.quad 0x375c3b46e64a1315
.quad 0x6f96aee420c78e24
.quad 0x1d8c431c06983371
.quad 0x6c137ad4d2760e6f
.quad 0x5c7ac0ea149b6ab9
.quad 0x446e3c9075c52322
