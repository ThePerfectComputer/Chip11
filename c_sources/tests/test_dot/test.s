
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

test_dot:
addc 17, 1, 1                                     # 22
adde 17, 1, 1                                     # 23
addic 17, 1, 1                                    # 24
add 17, 1, 1                                      # 25
addze 17, 2                                       # 26
and 17, 3, 2                                      # 27
cntlzd 17, 2                                      # 28
cntlzw 17, 3                                      # 29
extsb 17, 4                                       # 30
extsw 17, 5                                       # 31
neg 17, 2                                         # 32
nor 17, 1, 3                                      # 33
orc 17, 8, 7                                      # 34
or 17, 6, 5                                       # 35
rldcr 17, 8, 2, 15                                # 36
rldicl 17, 8, 18, 20                              # 37
rldicr 17, 8, 18, 20                              # 38
rldimi 17, 8, 18, 20                              # 39
rlwimi 17, 8, 14, 28, 20                          # 40
rlwinm 17, 8, 14, 28, 20                          # 41
sld 17, 8, 5                                      # 42
slw 17, 8, 5                                      # 43
srad 17, 8, 5                                     # 44
sradi 17, 8, 0x10                                 # 45
sraw 17, 8, 5                                     # 46
srawi 17, 8, 0x10                                 # 47
srd 17, 8, 5                                      # 48
srw 17, 8, 5                                      # 49
subfc 17, 1, 2                                    # 50
subfe 17, 2, 3                                    # 51
subf 17, 1, 2                                     # 52
xor 17, 4, 7                                      # 53
addc. 17, 1, 1                                    # 54
adde. 17, 1, 1                                    # 55
addic. 17, 1, 1                                   # 56
add. 17, 1, 1                                     # 57
addze. 17, 2                                      # 58
and. 17, 3, 2                                     # 59
cntlzd. 17, 2                                     # 60
cntlzw. 17, 3                                     # 61
extsb. 17, 4                                      # 62
extsw. 17, 5                                      # 63
neg. 17, 2                                        # 64
nor. 17, 1, 3                                     # 65
orc. 17, 8, 7                                     # 66
or. 17, 6, 5                                      # 67
rldcr. 17, 8, 2, 15                               # 68
rldicl. 17, 8, 18, 20                             # 69
rldicr. 17, 8, 18, 20                             # 70
rldimi. 17, 8, 18, 20                             # 71
rlwimi. 17, 8, 14, 28, 20                         # 72
rlwinm. 17, 8, 14, 28, 20                         # 73
sld. 17, 8, 5                                     # 74
slw. 17, 8, 5                                     # 75
srad. 17, 8, 5                                    # 76
sradi. 17, 8, 0x10                                # 77
sraw. 17, 8, 5                                    # 78
srawi. 17, 8, 0x10                                # 79
srd. 17, 8, 5                                     # 80
srw. 17, 8, 5                                     # 81
subfc. 17, 1, 2                                   # 82
subfe. 17, 2, 3                                   # 83
subf. 17, 1, 2                                    # 84
xor. 17, 4, 7                                     # 85
lis 17, -0x8000                                   # 86
sldi 17, 17, 32                                   # 87
addo 19, 17, 17                                   # 88
addc 17, 1, 1                                     # 89
adde 17, 1, 1                                     # 90
addic 17, 1, 1                                    # 91
add 17, 1, 1                                      # 92
addze 17, 2                                       # 93
and 17, 3, 2                                      # 94
cntlzd 17, 2                                      # 95
cntlzw 17, 3                                      # 96
extsb 17, 4                                       # 97
extsw 17, 5                                       # 98
neg 17, 2                                         # 99
nor 17, 1, 3                                      # 100
orc 17, 8, 7                                      # 101
or 17, 6, 5                                       # 102
rldcr 17, 8, 2, 15                                # 103
rldicl 17, 8, 18, 20                              # 104
rldicr 17, 8, 18, 20                              # 105
rldimi 17, 8, 18, 20                              # 106
rlwimi 17, 8, 14, 28, 20                          # 107
rlwinm 17, 8, 14, 28, 20                          # 108
sld 17, 8, 5                                      # 109
slw 17, 8, 5                                      # 110
srad 17, 8, 5                                     # 111
sradi 17, 8, 0x10                                 # 112
sraw 17, 8, 5                                     # 113
srawi 17, 8, 0x10                                 # 114
srd 17, 8, 5                                      # 115
srw 17, 8, 5                                      # 116
subfc 17, 1, 2                                    # 117
subfe 17, 2, 3                                    # 118
subf 17, 1, 2                                     # 119
xor 17, 4, 7                                      # 120
addc. 17, 1, 1                                    # 121
adde. 17, 1, 1                                    # 122
addic. 17, 1, 1                                   # 123
add. 17, 1, 1                                     # 124
addze. 17, 2                                      # 125
and. 17, 3, 2                                     # 126
cntlzd. 17, 2                                     # 127
cntlzw. 17, 3                                     # 128
extsb. 17, 4                                      # 129
extsw. 17, 5                                      # 130
neg. 17, 2                                        # 131
nor. 17, 1, 3                                     # 132
orc. 17, 8, 7                                     # 133
or. 17, 6, 5                                      # 134
rldcr. 17, 8, 2, 15                               # 135
rldicl. 17, 8, 18, 20                             # 136
rldicr. 17, 8, 18, 20                             # 137
rldimi. 17, 8, 18, 20                             # 138
rlwimi. 17, 8, 14, 28, 20                         # 139
rlwinm. 17, 8, 14, 28, 20                         # 140
sld. 17, 8, 5                                     # 141
slw. 17, 8, 5                                     # 142
srad. 17, 8, 5                                    # 143
sradi. 17, 8, 0x10                                # 144
sraw. 17, 8, 5                                    # 145
srawi. 17, 8, 0x10                                # 146
srd. 17, 8, 5                                     # 147
srw. 17, 8, 5                                     # 148
subfc. 17, 1, 2                                   # 149
subfe. 17, 2, 3                                   # 150
subf. 17, 1, 2                                    # 151
xor. 17, 4, 7                                     # 152
nop
li 31, 1
hang: b hang
.section .data
data:
