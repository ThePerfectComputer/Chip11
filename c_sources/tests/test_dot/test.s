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

test_dot:
addc 17, 1, 1                                     # 20
adde 17, 1, 1                                     # 21
addic 17, 1, 1                                    # 22
add 17, 1, 1                                      # 23
addze 17, 2                                       # 24
and 17, 3, 2                                      # 25
nop
li 31, 1
hang: b hang
