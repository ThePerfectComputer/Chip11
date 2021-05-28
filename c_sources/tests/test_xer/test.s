
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

test_xer:
addic. 17, 3, 23989                               # 22
mfxer 20                                          # 23
addeo 18, 3, 7                                    # 24
mfxer 21                                          # 25
addic. 17, 8, -24391                              # 26
mfxer 20                                          # 27
addeo 18, 2, 6                                    # 28
mfxer 21                                          # 29
addic. 17, 4, 9789                                # 30
mfxer 20                                          # 31
addeo 18, 4, 2                                    # 32
mfxer 21                                          # 33
addic. 17, 1, -30281                              # 34
mfxer 20                                          # 35
addeo 18, 3, 5                                    # 36
mfxer 21                                          # 37
addic. 17, 2, -21245                              # 38
mfxer 20                                          # 39
addeo 18, 7, 6                                    # 40
mfxer 21                                          # 41
addic. 17, 5, 13093                               # 42
mfxer 20                                          # 43
addeo 18, 4, 4                                    # 44
mfxer 21                                          # 45
addic. 17, 4, 26857                               # 46
mfxer 20                                          # 47
addeo 18, 4, 1                                    # 48
mfxer 21                                          # 49
addic. 17, 4, 19853                               # 50
mfxer 20                                          # 51
addeo 18, 3, 4                                    # 52
mfxer 21                                          # 53
addic. 17, 5, -28409                              # 54
mfxer 20                                          # 55
addeo 18, 6, 5                                    # 56
mfxer 21                                          # 57
addic. 17, 7, -22081                              # 58
mfxer 20                                          # 59
addeo 18, 7, 1                                    # 60
mfxer 21                                          # 61
addic. 17, 2, -8005                               # 62
mfxer 20                                          # 63
addeo 18, 1, 1                                    # 64
mfxer 21                                          # 65
addic. 17, 3, 30906                               # 66
mfxer 20                                          # 67
addeo 18, 5, 1                                    # 68
mfxer 21                                          # 69
addic. 17, 8, -7388                               # 70
mfxer 20                                          # 71
addeo 18, 5, 8                                    # 72
mfxer 21                                          # 73
addic. 17, 4, -9555                               # 74
mfxer 20                                          # 75
addeo 18, 3, 5                                    # 76
mfxer 21                                          # 77
addic. 17, 1, 9085                                # 78
mfxer 20                                          # 79
addeo 18, 8, 2                                    # 80
mfxer 21                                          # 81
addic. 17, 5, 4435                                # 82
mfxer 20                                          # 83
addeo 18, 1, 3                                    # 84
mfxer 21                                          # 85
addic. 17, 2, 24039                               # 86
mfxer 20                                          # 87
addeo 18, 8, 1                                    # 88
mfxer 21                                          # 89
addic. 17, 3, 4864                                # 90
mfxer 20                                          # 91
addeo 18, 5, 2                                    # 92
mfxer 21                                          # 93
addic. 17, 8, -20779                              # 94
mfxer 20                                          # 95
addeo 18, 7, 2                                    # 96
mfxer 21                                          # 97
addic. 17, 6, 17337                               # 98
mfxer 20                                          # 99
addeo 18, 5, 4                                    # 100
mfxer 21                                          # 101
nop
li 31, 1
hang: b hang
.section .data
data:
