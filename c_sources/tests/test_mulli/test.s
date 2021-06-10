
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

test_mulli:
mulli 17, 5, -27419                               # 22
mulli 17, 1, 5859                                 # 23
mulli 17, 3, 16805                                # 24
mulli 17, 6, 18186                                # 25
mulli 17, 7, 15747                                # 26
mulli 17, 8, -4407                                # 27
mulli 17, 7, 1361                                 # 28
mulli 17, 4, -14354                               # 29
mulli 17, 4, 16446                                # 30
mulli 17, 6, 27910                                # 31
mulli 17, 5, -19853                               # 32
mulli 17, 4, -5656                                # 33
mulli 17, 5, -2708                                # 34
mulli 17, 5, -29345                               # 35
mulli 17, 3, -5628                                # 36
mulli 17, 3, 22204                                # 37
nop
li 31, 1
hang: b hang
.section .data
data:
