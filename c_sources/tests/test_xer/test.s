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
mflr 21                                           # 22
adde 18, 3, 7                                     # 23
mfxer 21                                          # 24
addic. 17, 8, -24391                              # 25
mfxer 20                                          # 26
mflr 21                                           # 27
adde 18, 2, 6                                     # 28
mfxer 21                                          # 29
addic. 17, 4, 9789                                # 30
mfxer 20                                          # 31
mflr 21                                           # 32
adde 18, 4, 2                                     # 33
mfxer 21                                          # 34
addic. 17, 1, -30281                              # 35
mfxer 20                                          # 36
mflr 21                                           # 37
adde 18, 3, 5                                     # 38
mfxer 21                                          # 39
addic. 17, 2, -21245                              # 40
mfxer 20                                          # 41
mflr 21                                           # 42
adde 18, 7, 6                                     # 43
mfxer 21                                          # 44
addic. 17, 5, 13093                               # 45
mfxer 20                                          # 46
mflr 21                                           # 47
adde 18, 4, 4                                     # 48
mfxer 21                                          # 49
addic. 17, 4, 26857                               # 50
mfxer 20                                          # 51
mflr 21                                           # 52
adde 18, 4, 1                                     # 53
mfxer 21                                          # 54
addic. 17, 4, 19853                               # 55
mfxer 20                                          # 56
mflr 21                                           # 57
adde 18, 3, 4                                     # 58
mfxer 21                                          # 59
addic. 17, 5, -28409                              # 60
mfxer 20                                          # 61
mflr 21                                           # 62
adde 18, 6, 5                                     # 63
mfxer 21                                          # 64
addic. 17, 7, -22081                              # 65
mfxer 20                                          # 66
mflr 21                                           # 67
adde 18, 7, 1                                     # 68
mfxer 21                                          # 69
addic. 17, 2, -8005                               # 70
mfxer 20                                          # 71
mflr 21                                           # 72
adde 18, 1, 1                                     # 73
mfxer 21                                          # 74
addic. 17, 3, 30906                               # 75
mfxer 20                                          # 76
mflr 21                                           # 77
adde 18, 5, 1                                     # 78
mfxer 21                                          # 79
addic. 17, 8, -7388                               # 80
mfxer 20                                          # 81
mflr 21                                           # 82
adde 18, 5, 8                                     # 83
mfxer 21                                          # 84
addic. 17, 4, -9555                               # 85
mfxer 20                                          # 86
mflr 21                                           # 87
adde 18, 3, 5                                     # 88
mfxer 21                                          # 89
addic. 17, 1, 9085                                # 90
mfxer 20                                          # 91
mflr 21                                           # 92
adde 18, 8, 2                                     # 93
mfxer 21                                          # 94
addic. 17, 5, 4435                                # 95
mfxer 20                                          # 96
mflr 21                                           # 97
adde 18, 1, 3                                     # 98
mfxer 21                                          # 99
addic. 17, 2, 24039                               # 100
mfxer 20                                          # 101
mflr 21                                           # 102
adde 18, 8, 1                                     # 103
mfxer 21                                          # 104
addic. 17, 3, 4864                                # 105
mfxer 20                                          # 106
mflr 21                                           # 107
adde 18, 5, 2                                     # 108
mfxer 21                                          # 109
addic. 17, 8, -20779                              # 110
mfxer 20                                          # 111
mflr 21                                           # 112
adde 18, 7, 2                                     # 113
mfxer 21                                          # 114
addic. 17, 6, 17337                               # 115
mfxer 20                                          # 116
mflr 21                                           # 117
adde 18, 5, 4                                     # 118
mfxer 21                                          # 119
nop
li 31, 1
hang: b hang
