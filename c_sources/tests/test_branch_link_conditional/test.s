
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

test_branch_link_conditional:
li 17, 10                                         # 22
lis 18, 3f@h                                      # 23
ori 18, 18, 3f@l                                  # 24
mtlr 18                                           # 25
1: cmpwi 17, 5                                    # 26
beqlr                                             # 27
addi 17, 17, -1                                   # 28
b 1b                                              # 29
3: li 19, 5                                       # 30
nop
li 31, 1
hang: b hang
.section .data
data:
