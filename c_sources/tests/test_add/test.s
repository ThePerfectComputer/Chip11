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

test_add:
addis 17, 2, 0xa5a5                               # 16
addis 17, 0, 0x0001                               # 17
addis 17, 0, 0xffff                               # 18
addi 17, 3, -1                                    # 19
nop
