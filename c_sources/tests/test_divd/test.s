
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

test_divd:
lis 17, -138632829064775795@highest               # 22
ori 17, 17, -138632829064775795@higher            # 23
sldi 17, 17, 32                                   # 24
oris 17, 17, -138632829064775795@h                # 25
ori 17, 17, -138632829064775795@l                 # 26
clrldi 18, 17, 0                                  # 27
divd 19, 6, 18                                    # 28
divdu 19, 6, 18                                   # 29
clrldi 18, 17, 1                                  # 30
divd 19, 7, 18                                    # 31
divdu 19, 7, 18                                   # 32
clrldi 18, 17, 2                                  # 33
divd 19, 4, 18                                    # 34
divdu 19, 4, 18                                   # 35
clrldi 18, 17, 3                                  # 36
divd 19, 1, 18                                    # 37
divdu 19, 1, 18                                   # 38
clrldi 18, 17, 4                                  # 39
divd 19, 2, 18                                    # 40
divdu 19, 2, 18                                   # 41
clrldi 18, 17, 5                                  # 42
divd 19, 3, 18                                    # 43
divdu 19, 3, 18                                   # 44
clrldi 18, 17, 6                                  # 45
divd 19, 2, 18                                    # 46
divdu 19, 2, 18                                   # 47
clrldi 18, 17, 7                                  # 48
divd 19, 8, 18                                    # 49
divdu 19, 8, 18                                   # 50
clrldi 18, 17, 8                                  # 51
divd 19, 8, 18                                    # 52
divdu 19, 8, 18                                   # 53
clrldi 18, 17, 9                                  # 54
divd 19, 3, 18                                    # 55
divdu 19, 3, 18                                   # 56
clrldi 18, 17, 10                                 # 57
divd 19, 3, 18                                    # 58
divdu 19, 3, 18                                   # 59
clrldi 18, 17, 11                                 # 60
divd 19, 6, 18                                    # 61
divdu 19, 6, 18                                   # 62
clrldi 18, 17, 12                                 # 63
divd 19, 7, 18                                    # 64
divdu 19, 7, 18                                   # 65
clrldi 18, 17, 13                                 # 66
divd 19, 4, 18                                    # 67
divdu 19, 4, 18                                   # 68
clrldi 18, 17, 14                                 # 69
divd 19, 4, 18                                    # 70
divdu 19, 4, 18                                   # 71
clrldi 18, 17, 15                                 # 72
divd 19, 1, 18                                    # 73
divdu 19, 1, 18                                   # 74
clrldi 18, 17, 16                                 # 75
divd 19, 3, 18                                    # 76
divdu 19, 3, 18                                   # 77
clrldi 18, 17, 17                                 # 78
divd 19, 2, 18                                    # 79
divdu 19, 2, 18                                   # 80
clrldi 18, 17, 18                                 # 81
divd 19, 8, 18                                    # 82
divdu 19, 8, 18                                   # 83
clrldi 18, 17, 19                                 # 84
divd 19, 6, 18                                    # 85
divdu 19, 6, 18                                   # 86
clrldi 18, 17, 20                                 # 87
divd 19, 3, 18                                    # 88
divdu 19, 3, 18                                   # 89
clrldi 18, 17, 21                                 # 90
divd 19, 8, 18                                    # 91
divdu 19, 8, 18                                   # 92
clrldi 18, 17, 22                                 # 93
divd 19, 8, 18                                    # 94
divdu 19, 8, 18                                   # 95
clrldi 18, 17, 23                                 # 96
divd 19, 4, 18                                    # 97
divdu 19, 4, 18                                   # 98
clrldi 18, 17, 24                                 # 99
divd 19, 4, 18                                    # 100
divdu 19, 4, 18                                   # 101
clrldi 18, 17, 25                                 # 102
divd 19, 5, 18                                    # 103
divdu 19, 5, 18                                   # 104
clrldi 18, 17, 26                                 # 105
divd 19, 2, 18                                    # 106
divdu 19, 2, 18                                   # 107
clrldi 18, 17, 27                                 # 108
divd 19, 3, 18                                    # 109
divdu 19, 3, 18                                   # 110
clrldi 18, 17, 28                                 # 111
divd 19, 8, 18                                    # 112
divdu 19, 8, 18                                   # 113
clrldi 18, 17, 29                                 # 114
divd 19, 5, 18                                    # 115
divdu 19, 5, 18                                   # 116
clrldi 18, 17, 30                                 # 117
divd 19, 5, 18                                    # 118
divdu 19, 5, 18                                   # 119
clrldi 18, 17, 31                                 # 120
divd 19, 5, 18                                    # 121
divdu 19, 5, 18                                   # 122
clrldi 18, 17, 32                                 # 123
divd 19, 4, 18                                    # 124
divdu 19, 4, 18                                   # 125
clrldi 18, 17, 33                                 # 126
divd 19, 6, 18                                    # 127
divdu 19, 6, 18                                   # 128
clrldi 18, 17, 34                                 # 129
divd 19, 3, 18                                    # 130
divdu 19, 3, 18                                   # 131
clrldi 18, 17, 35                                 # 132
divd 19, 1, 18                                    # 133
divdu 19, 1, 18                                   # 134
clrldi 18, 17, 36                                 # 135
divd 19, 4, 18                                    # 136
divdu 19, 4, 18                                   # 137
clrldi 18, 17, 37                                 # 138
divd 19, 3, 18                                    # 139
divdu 19, 3, 18                                   # 140
clrldi 18, 17, 38                                 # 141
divd 19, 4, 18                                    # 142
divdu 19, 4, 18                                   # 143
clrldi 18, 17, 39                                 # 144
divd 19, 8, 18                                    # 145
divdu 19, 8, 18                                   # 146
clrldi 18, 17, 40                                 # 147
divd 19, 3, 18                                    # 148
divdu 19, 3, 18                                   # 149
clrldi 18, 17, 41                                 # 150
divd 19, 2, 18                                    # 151
divdu 19, 2, 18                                   # 152
clrldi 18, 17, 42                                 # 153
divd 19, 3, 18                                    # 154
divdu 19, 3, 18                                   # 155
clrldi 18, 17, 43                                 # 156
divd 19, 2, 18                                    # 157
divdu 19, 2, 18                                   # 158
clrldi 18, 17, 44                                 # 159
divd 19, 8, 18                                    # 160
divdu 19, 8, 18                                   # 161
clrldi 18, 17, 45                                 # 162
divd 19, 4, 18                                    # 163
divdu 19, 4, 18                                   # 164
clrldi 18, 17, 46                                 # 165
divd 19, 1, 18                                    # 166
divdu 19, 1, 18                                   # 167
clrldi 18, 17, 47                                 # 168
divd 19, 1, 18                                    # 169
divdu 19, 1, 18                                   # 170
clrldi 18, 17, 48                                 # 171
divd 19, 4, 18                                    # 172
divdu 19, 4, 18                                   # 173
clrldi 18, 17, 49                                 # 174
divd 19, 3, 18                                    # 175
divdu 19, 3, 18                                   # 176
clrldi 18, 17, 50                                 # 177
divd 19, 8, 18                                    # 178
divdu 19, 8, 18                                   # 179
clrldi 18, 17, 51                                 # 180
divd 19, 7, 18                                    # 181
divdu 19, 7, 18                                   # 182
clrldi 18, 17, 52                                 # 183
divd 19, 7, 18                                    # 184
divdu 19, 7, 18                                   # 185
clrldi 18, 17, 53                                 # 186
divd 19, 3, 18                                    # 187
divdu 19, 3, 18                                   # 188
clrldi 18, 17, 54                                 # 189
divd 19, 6, 18                                    # 190
divdu 19, 6, 18                                   # 191
clrldi 18, 17, 55                                 # 192
divd 19, 8, 18                                    # 193
divdu 19, 8, 18                                   # 194
clrldi 18, 17, 56                                 # 195
divd 19, 7, 18                                    # 196
divdu 19, 7, 18                                   # 197
clrldi 18, 17, 57                                 # 198
divd 19, 7, 18                                    # 199
divdu 19, 7, 18                                   # 200
clrldi 18, 17, 58                                 # 201
divd 19, 1, 18                                    # 202
divdu 19, 1, 18                                   # 203
clrldi 18, 17, 59                                 # 204
divd 19, 6, 18                                    # 205
divdu 19, 6, 18                                   # 206
clrldi 18, 17, 60                                 # 207
divd 19, 6, 18                                    # 208
divdu 19, 6, 18                                   # 209
clrldi 18, 17, 61                                 # 210
divd 19, 2, 18                                    # 211
divdu 19, 2, 18                                   # 212
clrldi 18, 17, 62                                 # 213
divd 19, 7, 18                                    # 214
divdu 19, 7, 18                                   # 215
clrldi 18, 17, 63                                 # 216
divd 19, 2, 18                                    # 217
divdu 19, 2, 18                                   # 218
nop
li 31, 1
hang: b hang
.section .data
data:
