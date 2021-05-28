
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

test_cmp:
li 10, -5                                         # 22
cmpi 0, 1, 10, -6                                 # 23
cmpi 1, 1, 10, -4                                 # 24
cmpi 2, 1, 10, -5                                 # 25
li 10, -5                                         # 26
li 11, -6                                         # 27
cmp 0, 1, 10, 11                                  # 28
li 11, -6                                         # 29
cmp 1, 1, 10, 11                                  # 30
li 11, -6                                         # 31
cmp 2, 1, 10, 11                                  # 32
li 10, -5                                         # 33
cmpli 0, 1, 10, -6                                # 34
cmpli 1, 1, 10, -4                                # 35
cmpli 2, 1, 10, -5                                # 36
li 10, -5                                         # 37
li 11, -6                                         # 38
cmpl 0, 1, 10, 11                                 # 39
li 11, -6                                         # 40
cmpl 1, 1, 10, 11                                 # 41
li 11, -6                                         # 42
cmpl 2, 1, 10, 11                                 # 43
lis 17, -0x8000                                   # 44
sldi 17, 17, 32                                   # 45
addo 19, 17, 17                                   # 46
li 10, -5                                         # 47
cmpi 0, 1, 10, -6                                 # 48
cmpi 1, 1, 10, -4                                 # 49
cmpi 2, 1, 10, -5                                 # 50
li 10, -5                                         # 51
li 11, -6                                         # 52
cmp 0, 1, 10, 11                                  # 53
li 11, -6                                         # 54
cmp 1, 1, 10, 11                                  # 55
li 11, -6                                         # 56
cmp 2, 1, 10, 11                                  # 57
li 10, -5                                         # 58
cmpli 0, 1, 10, -6                                # 59
cmpli 1, 1, 10, -4                                # 60
cmpli 2, 1, 10, -5                                # 61
li 10, -5                                         # 62
li 11, -6                                         # 63
cmpl 0, 1, 10, 11                                 # 64
li 11, -6                                         # 65
cmpl 1, 1, 10, 11                                 # 66
li 11, -6                                         # 67
cmpl 2, 1, 10, 11                                 # 68
nop
li 31, 1
hang: b hang
.section .data
data:
