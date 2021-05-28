
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

test_branch_to_tar:
lis 17, 1f@h                                      # 22
ori 17, 17, 1f@l                                  # 23
mtspr 815, 17 # mttar 17                          # 24
bctar 20, lt # branch unconditional               # 25
b 2f                                              # 26
li 18, 1                                          # 27
1: li 19, 2                                       # 28
mflr 17                                           # 29
blr                                               # 30
2: li 18, 5                                       # 31
nop
li 31, 1
hang: b hang
.section .data
data:
