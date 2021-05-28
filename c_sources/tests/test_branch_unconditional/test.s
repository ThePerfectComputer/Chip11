
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

test_branch_unconditional:
b 1f                                              # 22
li 17, 0x100 # should not happen                  # 23
1: li 18, 0x200                                   # 24
b 3f                                              # 25
2: li 18, 0x4129                                  # 26
b 4f                                              # 27
li 17, 0x1234                                     # 28
3: b 2b                                           # 29
4: li 18, 0xfff                                   # 30
nop
li 31, 1
hang: b hang
.section .data
data:
