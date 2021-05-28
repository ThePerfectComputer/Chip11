
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

test_load8_update:
lis 17, (ld_data-8)@h                             # 22
ori 17, 17, (ld_data-8)@l                         # 23
ldu 18, 8(17)                                     # 24
ldu 18, 8(17)                                     # 25
ldu 18, 8(17)                                     # 26
ldu 18, 8(17)                                     # 27
ldu 18, 8(17)                                     # 28
ldu 18, 8(17)                                     # 29
ldu 18, 8(17)                                     # 30
ldu 18, 8(17)                                     # 31
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.quad 0x661e2213938eb9ef
.quad 0x71133ac64cf850f5
.quad 0x1d8ceebfb843bada
.quad 0x44e0fccc8b28d921
.quad 0x32328777c5fd07c2
.quad 0xc9c2f197003696c
.quad 0x4e5e03a9a3fc991e
.quad 0x26c4be0e482e51dd
