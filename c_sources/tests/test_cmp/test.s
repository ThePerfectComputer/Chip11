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
li 10, -5                                         # 16
cmpi 0, 1, 10, -6                                 # 17
cmpi 1, 1, 10, -4                                 # 18
cmpi 2, 1, 10, -5                                 # 19
li 10, -5                                         # 20
li 11, -6                                         # 21
cmp 0, 1, 10, 11                                  # 22
li 11, -6                                         # 23
cmp 1, 1, 10, 11                                  # 24
li 11, -6                                         # 25
cmp 2, 1, 10, 11                                  # 26
li 10, -5                                         # 27
cmpli 0, 1, 10, -6                                # 28
cmpli 1, 1, 10, -4                                # 29
cmpli 2, 1, 10, -5                                # 30
li 10, -5                                         # 31
li 11, -6                                         # 32
cmpl 0, 1, 10, 11                                 # 33
li 11, -6                                         # 34
cmpl 1, 1, 10, 11                                 # 35
li 11, -6                                         # 36
cmpl 2, 1, 10, 11                                 # 37
nop
