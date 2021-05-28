
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

test_overflow:
lis 17, -0x8000                                   # 22
sldi 19, 17, 32                                   # 23
li 0, 0                                           # 24
addo 18, 17, 17                                   # 25
mfxer 21                                          # 26
mtxer 0                                           # 27
addo 18, 19, 19                                   # 28
mfxer 21                                          # 29
mtxer 0                                           # 30
addco 18, 17, 17                                  # 31
mfxer 21                                          # 32
mtxer 0                                           # 33
addco 18, 19, 19                                  # 34
mfxer 21                                          # 35
mtxer 0                                           # 36
addeo 18, 17, 17                                  # 37
mfxer 21                                          # 38
mtxer 0                                           # 39
addeo 18, 19, 19                                  # 40
mfxer 21                                          # 41
mtxer 0                                           # 42
subfo 18, 17, 17                                  # 43
mfxer 21                                          # 44
mtxer 0                                           # 45
subfo 18, 19, 19                                  # 46
mfxer 21                                          # 47
mtxer 0                                           # 48
subfco 18, 17, 17                                 # 49
mfxer 21                                          # 50
mtxer 0                                           # 51
subfco 18, 19, 19                                 # 52
mfxer 21                                          # 53
mtxer 0                                           # 54
subfeo 18, 17, 17                                 # 55
mfxer 21                                          # 56
mtxer 0                                           # 57
subfeo 18, 19, 19                                 # 58
mfxer 21                                          # 59
mtxer 0                                           # 60
addo 18, 19, 19                                   # 61
addo 18, 0, 0                                     # 62
mfxer 21                                          # 63
nop
li 31, 1
hang: b hang
.section .data
data:
