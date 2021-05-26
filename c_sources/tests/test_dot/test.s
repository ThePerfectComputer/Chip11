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

test_dot:
addc 17, 1, 1                                     # 20
adde 17, 1, 1                                     # 21
addic 17, 1, 1                                    # 22
add 17, 1, 1                                      # 23
addze 17, 2                                       # 24
and 17, 3, 2                                      # 25
cntlzd 17, 2                                      # 26
cntlzw 17, 3                                      # 27
extsb 17, 4                                       # 28
extsw 17, 5                                       # 29
neg 17, 2                                         # 30
nor 17, 1, 3                                      # 31
orc 17, 8, 7                                      # 32
or 17, 6, 5                                       # 33
rldcr 17, 8, 2, 15                                # 34
rldicl 17, 8, 18, 20                              # 35
rldicr 17, 8, 18, 20                              # 36
rldimi 17, 8, 18, 20                              # 37
rlwimi 17, 8, 14, 28, 20                          # 38
rlwinm 17, 8, 14, 28, 20                          # 39
sld 17, 8, 5                                      # 40
slw 17, 8, 5                                      # 41
srad 17, 8, 5                                     # 42
sradi 17, 8, 0x10                                 # 43
sraw 17, 8, 5                                     # 44
srawi 17, 8, 0x10                                 # 45
srd 17, 8, 5                                      # 46
srw 17, 8, 5                                      # 47
subfc 17, 1, 2                                    # 48
subfe 17, 2, 3                                    # 49
subf 17, 1, 2                                     # 50
xor 17, 4, 7                                      # 51
addc. 17, 1, 1                                    # 52
adde. 17, 1, 1                                    # 53
addic. 17, 1, 1                                   # 54
add. 17, 1, 1                                     # 55
addze. 17, 2                                      # 56
and. 17, 3, 2                                     # 57
cntlzd. 17, 2                                     # 58
cntlzw. 17, 3                                     # 59
extsb. 17, 4                                      # 60
extsw. 17, 5                                      # 61
neg. 17, 2                                        # 62
nor. 17, 1, 3                                     # 63
orc. 17, 8, 7                                     # 64
or. 17, 6, 5                                      # 65
rldcr. 17, 8, 2, 15                               # 66
rldicl. 17, 8, 18, 20                             # 67
rldicr. 17, 8, 18, 20                             # 68
rldimi. 17, 8, 18, 20                             # 69
rlwimi. 17, 8, 14, 28, 20                         # 70
rlwinm. 17, 8, 14, 28, 20                         # 71
sld. 17, 8, 5                                     # 72
slw. 17, 8, 5                                     # 73
srad. 17, 8, 5                                    # 74
sradi. 17, 8, 0x10                                # 75
sraw. 17, 8, 5                                    # 76
srawi. 17, 8, 0x10                                # 77
srd. 17, 8, 5                                     # 78
srw. 17, 8, 5                                     # 79
subfc. 17, 1, 2                                   # 80
subfe. 17, 2, 3                                   # 81
subf. 17, 1, 2                                    # 82
xor. 17, 4, 7                                     # 83
lis 17, -0x8000                                   # 84
sldi 17, 17, 32                                   # 85
addo 19, 17, 17                                   # 86
addc 17, 1, 1                                     # 87
adde 17, 1, 1                                     # 88
addic 17, 1, 1                                    # 89
add 17, 1, 1                                      # 90
addze 17, 2                                       # 91
and 17, 3, 2                                      # 92
cntlzd 17, 2                                      # 93
cntlzw 17, 3                                      # 94
extsb 17, 4                                       # 95
extsw 17, 5                                       # 96
neg 17, 2                                         # 97
nor 17, 1, 3                                      # 98
orc 17, 8, 7                                      # 99
or 17, 6, 5                                       # 100
rldcr 17, 8, 2, 15                                # 101
rldicl 17, 8, 18, 20                              # 102
rldicr 17, 8, 18, 20                              # 103
rldimi 17, 8, 18, 20                              # 104
rlwimi 17, 8, 14, 28, 20                          # 105
rlwinm 17, 8, 14, 28, 20                          # 106
sld 17, 8, 5                                      # 107
slw 17, 8, 5                                      # 108
srad 17, 8, 5                                     # 109
sradi 17, 8, 0x10                                 # 110
sraw 17, 8, 5                                     # 111
srawi 17, 8, 0x10                                 # 112
srd 17, 8, 5                                      # 113
srw 17, 8, 5                                      # 114
subfc 17, 1, 2                                    # 115
subfe 17, 2, 3                                    # 116
subf 17, 1, 2                                     # 117
xor 17, 4, 7                                      # 118
addc. 17, 1, 1                                    # 119
adde. 17, 1, 1                                    # 120
addic. 17, 1, 1                                   # 121
add. 17, 1, 1                                     # 122
addze. 17, 2                                      # 123
and. 17, 3, 2                                     # 124
cntlzd. 17, 2                                     # 125
cntlzw. 17, 3                                     # 126
extsb. 17, 4                                      # 127
extsw. 17, 5                                      # 128
neg. 17, 2                                        # 129
nor. 17, 1, 3                                     # 130
orc. 17, 8, 7                                     # 131
or. 17, 6, 5                                      # 132
rldcr. 17, 8, 2, 15                               # 133
rldicl. 17, 8, 18, 20                             # 134
rldicr. 17, 8, 18, 20                             # 135
rldimi. 17, 8, 18, 20                             # 136
rlwimi. 17, 8, 14, 28, 20                         # 137
rlwinm. 17, 8, 14, 28, 20                         # 138
sld. 17, 8, 5                                     # 139
slw. 17, 8, 5                                     # 140
srad. 17, 8, 5                                    # 141
sradi. 17, 8, 0x10                                # 142
sraw. 17, 8, 5                                    # 143
srawi. 17, 8, 0x10                                # 144
srd. 17, 8, 5                                     # 145
srw. 17, 8, 5                                     # 146
subfc. 17, 1, 2                                   # 147
subfe. 17, 2, 3                                   # 148
subf. 17, 1, 2                                    # 149
xor. 17, 4, 7                                     # 150
nop
li 31, 1
hang: b hang
