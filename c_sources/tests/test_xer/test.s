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

test_xer:
addic. 17, 3, 23989                               # 20
mfxer 20                                          # 21
adde 18, 3, 7                                     # 22
mfxer 21                                          # 23
addic. 17, 8, -24391                              # 24
mfxer 20                                          # 25
adde 18, 2, 6                                     # 26
mfxer 21                                          # 27
addic. 17, 4, 9789                                # 28
mfxer 20                                          # 29
adde 18, 4, 2                                     # 30
mfxer 21                                          # 31
addic. 17, 1, -30281                              # 32
mfxer 20                                          # 33
adde 18, 3, 5                                     # 34
mfxer 21                                          # 35
addic. 17, 2, -21245                              # 36
mfxer 20                                          # 37
adde 18, 7, 6                                     # 38
mfxer 21                                          # 39
addic. 17, 5, 13093                               # 40
mfxer 20                                          # 41
adde 18, 4, 4                                     # 42
mfxer 21                                          # 43
addic. 17, 4, 26857                               # 44
mfxer 20                                          # 45
adde 18, 4, 1                                     # 46
mfxer 21                                          # 47
addic. 17, 4, 19853                               # 48
mfxer 20                                          # 49
adde 18, 3, 4                                     # 50
mfxer 21                                          # 51
addic. 17, 5, -28409                              # 52
mfxer 20                                          # 53
adde 18, 6, 5                                     # 54
mfxer 21                                          # 55
addic. 17, 7, -22081                              # 56
mfxer 20                                          # 57
adde 18, 7, 1                                     # 58
mfxer 21                                          # 59
addic. 17, 2, -8005                               # 60
mfxer 20                                          # 61
adde 18, 1, 1                                     # 62
mfxer 21                                          # 63
addic. 17, 3, 30906                               # 64
mfxer 20                                          # 65
adde 18, 5, 1                                     # 66
mfxer 21                                          # 67
addic. 17, 8, -7388                               # 68
mfxer 20                                          # 69
adde 18, 5, 8                                     # 70
mfxer 21                                          # 71
addic. 17, 4, -9555                               # 72
mfxer 20                                          # 73
adde 18, 3, 5                                     # 74
mfxer 21                                          # 75
addic. 17, 1, 9085                                # 76
mfxer 20                                          # 77
adde 18, 8, 2                                     # 78
mfxer 21                                          # 79
addic. 17, 5, 4435                                # 80
mfxer 20                                          # 81
adde 18, 1, 3                                     # 82
mfxer 21                                          # 83
addic. 17, 2, 24039                               # 84
mfxer 20                                          # 85
adde 18, 8, 1                                     # 86
mfxer 21                                          # 87
addic. 17, 3, 4864                                # 88
mfxer 20                                          # 89
adde 18, 5, 2                                     # 90
mfxer 21                                          # 91
addic. 17, 8, -20779                              # 92
mfxer 20                                          # 93
adde 18, 7, 2                                     # 94
mfxer 21                                          # 95
addic. 17, 6, 17337                               # 96
mfxer 20                                          # 97
adde 18, 5, 4                                     # 98
mfxer 21                                          # 99
nop
li 31, 1
hang: b hang
