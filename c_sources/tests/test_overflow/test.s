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
sldi 19, 17, 32                                   # 21
li 0, 0                                           # 22
addo 18, 17, 17                                   # 23
mfxer 21                                          # 24
mtxer 0                                           # 25
addo 18, 19, 19                                   # 26
mfxer 21                                          # 27
mtxer 0                                           # 28
addco 18, 17, 17                                  # 29
mfxer 21                                          # 30
mtxer 0                                           # 31
addco 18, 19, 19                                  # 32
mfxer 21                                          # 33
mtxer 0                                           # 34
addeo 18, 17, 17                                  # 35
mfxer 21                                          # 36
mtxer 0                                           # 37
addeo 18, 19, 19                                  # 38
mfxer 21                                          # 39
mtxer 0                                           # 40
subfo 18, 17, 17                                  # 41
mfxer 21                                          # 42
mtxer 0                                           # 43
subfo 18, 19, 19                                  # 44
mfxer 21                                          # 45
mtxer 0                                           # 46
subfco 18, 17, 17                                 # 47
mfxer 21                                          # 48
mtxer 0                                           # 49
subfco 18, 19, 19                                 # 50
mfxer 21                                          # 51
mtxer 0                                           # 52
subfeo 18, 17, 17                                 # 53
mfxer 21                                          # 54
mtxer 0                                           # 55
subfeo 18, 19, 19                                 # 56
mfxer 21                                          # 57
mtxer 0                                           # 58
addo 18, 19, 19                                   # 59
addo 18, 0, 0                                     # 60
mfxer 21                                          # 61
nop
li 31, 1
hang: b hang
