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

test_overflow:
lis 17, -0x8000                                   # 20
addo 18, 17, 17                                   # 21
mfxer 21                                          # 22
mtxer 0                                           # 23
addco 18, 17, 17                                  # 24
mfxer 21                                          # 25
mtxer 0                                           # 26
addeo 18, 17, 17                                  # 27
mfxer 21                                          # 28
mtxer 0                                           # 29
subfo 18, 17, 17                                  # 30
mfxer 21                                          # 31
mtxer 0                                           # 32
subfco 18, 17, 17                                 # 33
mfxer 21                                          # 34
mtxer 0                                           # 35
subfeo 18, 17, 17                                 # 36
mfxer 21                                          # 37
mtxer 0                                           # 38
nop
li 31, 1
hang: b hang
