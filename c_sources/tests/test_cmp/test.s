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
li 10, -5                                         # 20
cmpi 0, 1, 10, -6                                 # 21
cmpi 1, 1, 10, -4                                 # 22
cmpi 2, 1, 10, -5                                 # 23
li 10, -5                                         # 24
li 11, -6                                         # 25
cmp 0, 1, 10, 11                                  # 26
li 11, -6                                         # 27
cmp 1, 1, 10, 11                                  # 28
li 11, -6                                         # 29
cmp 2, 1, 10, 11                                  # 30
li 10, -5                                         # 31
cmpli 0, 1, 10, -6                                # 32
cmpli 1, 1, 10, -4                                # 33
cmpli 2, 1, 10, -5                                # 34
li 10, -5                                         # 35
li 11, -6                                         # 36
cmpl 0, 1, 10, 11                                 # 37
li 11, -6                                         # 38
cmpl 1, 1, 10, 11                                 # 39
li 11, -6                                         # 40
cmpl 2, 1, 10, 11                                 # 41
lis 17, -0x8000                                   # 42
sldi 17, 17, 32                                   # 43
addo 19, 17, 17                                   # 44
li 10, -5                                         # 45
cmpi 0, 1, 10, -6                                 # 46
cmpi 1, 1, 10, -4                                 # 47
cmpi 2, 1, 10, -5                                 # 48
li 10, -5                                         # 49
li 11, -6                                         # 50
cmp 0, 1, 10, 11                                  # 51
li 11, -6                                         # 52
cmp 1, 1, 10, 11                                  # 53
li 11, -6                                         # 54
cmp 2, 1, 10, 11                                  # 55
li 10, -5                                         # 56
cmpli 0, 1, 10, -6                                # 57
cmpli 1, 1, 10, -4                                # 58
cmpli 2, 1, 10, -5                                # 59
li 10, -5                                         # 60
li 11, -6                                         # 61
cmpl 0, 1, 10, 11                                 # 62
li 11, -6                                         # 63
cmpl 1, 1, 10, 11                                 # 64
li 11, -6                                         # 65
cmpl 2, 1, 10, 11                                 # 66
nop
li 31, 1
hang: b hang
