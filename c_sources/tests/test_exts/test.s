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

test_exts:
li 10, 0                                          # 20
extsb. 10, 10                                     # 21
li 10, -1                                         # 22
extsb. 10, 10                                     # 23
li 10, 0                                          # 24
extsw. 10, 10                                     # 25
li 10, -1                                         # 26
extsw. 10, 10                                     # 27
nop
li 31, 1
hang: b hang
