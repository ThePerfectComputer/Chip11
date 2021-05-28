
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

test_add:
addi 17, 6, -7870                                 # 22
addis 17, 6, -7870                                # 23
addic 17, 6, -7870                                # 24
addic. 17, 6, -7870                               # 25
subfic 17, 6, -7870                               # 26
add 17, 6, 5                                      # 27
subf 17, 6, 5                                     # 28
addc 17, 6, 5                                     # 29
subfc 17, 6, 5                                    # 30
adde 17, 6, 5                                     # 31
subfe 17, 6, 5                                    # 32
addme 17, 6                                       # 33
subfme 17, 6                                      # 34
subfze 17, 6                                      # 35
addze 17, 6                                       # 36
addi 17, 5, -11330                                # 37
addis 17, 5, -11330                               # 38
addic 17, 5, -11330                               # 39
addic. 17, 5, -11330                              # 40
subfic 17, 5, -11330                              # 41
add 17, 5, 1                                      # 42
subf 17, 5, 1                                     # 43
addc 17, 5, 1                                     # 44
subfc 17, 5, 1                                    # 45
adde 17, 5, 1                                     # 46
subfe 17, 5, 1                                    # 47
addme 17, 5                                       # 48
subfme 17, 5                                      # 49
subfze 17, 5                                      # 50
addze 17, 5                                       # 51
addi 17, 5, 4046                                  # 52
addis 17, 5, 4046                                 # 53
addic 17, 5, 4046                                 # 54
addic. 17, 5, 4046                                # 55
subfic 17, 5, 4046                                # 56
add 17, 5, 6                                      # 57
subf 17, 5, 6                                     # 58
addc 17, 5, 6                                     # 59
subfc 17, 5, 6                                    # 60
adde 17, 5, 6                                     # 61
subfe 17, 5, 6                                    # 62
addme 17, 5                                       # 63
subfme 17, 5                                      # 64
subfze 17, 5                                      # 65
addze 17, 5                                       # 66
addi 17, 6, -8584                                 # 67
addis 17, 6, -8584                                # 68
addic 17, 6, -8584                                # 69
addic. 17, 6, -8584                               # 70
subfic 17, 6, -8584                               # 71
add 17, 6, 1                                      # 72
subf 17, 6, 1                                     # 73
addc 17, 6, 1                                     # 74
subfc 17, 6, 1                                    # 75
adde 17, 6, 1                                     # 76
subfe 17, 6, 1                                    # 77
addme 17, 6                                       # 78
subfme 17, 6                                      # 79
subfze 17, 6                                      # 80
addze 17, 6                                       # 81
addi 17, 5, 6740                                  # 82
addis 17, 5, 6740                                 # 83
addic 17, 5, 6740                                 # 84
addic. 17, 5, 6740                                # 85
subfic 17, 5, 6740                                # 86
add 17, 5, 8                                      # 87
subf 17, 5, 8                                     # 88
addc 17, 5, 8                                     # 89
subfc 17, 5, 8                                    # 90
adde 17, 5, 8                                     # 91
subfe 17, 5, 8                                    # 92
addme 17, 5                                       # 93
subfme 17, 5                                      # 94
subfze 17, 5                                      # 95
addze 17, 5                                       # 96
addi 17, 1, 24123                                 # 97
addis 17, 1, 24123                                # 98
addic 17, 1, 24123                                # 99
addic. 17, 1, 24123                               # 100
subfic 17, 1, 24123                               # 101
add 17, 1, 5                                      # 102
subf 17, 1, 5                                     # 103
addc 17, 1, 5                                     # 104
subfc 17, 1, 5                                    # 105
adde 17, 1, 5                                     # 106
subfe 17, 1, 5                                    # 107
addme 17, 1                                       # 108
subfme 17, 1                                      # 109
subfze 17, 1                                      # 110
addze 17, 1                                       # 111
addi 17, 6, 15098                                 # 112
addis 17, 6, 15098                                # 113
addic 17, 6, 15098                                # 114
addic. 17, 6, 15098                               # 115
subfic 17, 6, 15098                               # 116
add 17, 6, 7                                      # 117
subf 17, 6, 7                                     # 118
addc 17, 6, 7                                     # 119
subfc 17, 6, 7                                    # 120
adde 17, 6, 7                                     # 121
subfe 17, 6, 7                                    # 122
addme 17, 6                                       # 123
subfme 17, 6                                      # 124
subfze 17, 6                                      # 125
addze 17, 6                                       # 126
addi 17, 2, -20866                                # 127
addis 17, 2, -20866                               # 128
addic 17, 2, -20866                               # 129
addic. 17, 2, -20866                              # 130
subfic 17, 2, -20866                              # 131
add 17, 2, 1                                      # 132
subf 17, 2, 1                                     # 133
addc 17, 2, 1                                     # 134
subfc 17, 2, 1                                    # 135
adde 17, 2, 1                                     # 136
subfe 17, 2, 1                                    # 137
addme 17, 2                                       # 138
subfme 17, 2                                      # 139
subfze 17, 2                                      # 140
addze 17, 2                                       # 141
addi 17, 8, -11942                                # 142
addis 17, 8, -11942                               # 143
addic 17, 8, -11942                               # 144
addic. 17, 8, -11942                              # 145
subfic 17, 8, -11942                              # 146
add 17, 8, 4                                      # 147
subf 17, 8, 4                                     # 148
addc 17, 8, 4                                     # 149
subfc 17, 8, 4                                    # 150
adde 17, 8, 4                                     # 151
subfe 17, 8, 4                                    # 152
addme 17, 8                                       # 153
subfme 17, 8                                      # 154
subfze 17, 8                                      # 155
addze 17, 8                                       # 156
addi 17, 6, -31767                                # 157
addis 17, 6, -31767                               # 158
addic 17, 6, -31767                               # 159
addic. 17, 6, -31767                              # 160
subfic 17, 6, -31767                              # 161
add 17, 6, 1                                      # 162
subf 17, 6, 1                                     # 163
addc 17, 6, 1                                     # 164
subfc 17, 6, 1                                    # 165
adde 17, 6, 1                                     # 166
subfe 17, 6, 1                                    # 167
addme 17, 6                                       # 168
subfme 17, 6                                      # 169
subfze 17, 6                                      # 170
addze 17, 6                                       # 171
addi 17, 5, -6952                                 # 172
addis 17, 5, -6952                                # 173
addic 17, 5, -6952                                # 174
addic. 17, 5, -6952                               # 175
subfic 17, 5, -6952                               # 176
add 17, 5, 5                                      # 177
subf 17, 5, 5                                     # 178
addc 17, 5, 5                                     # 179
subfc 17, 5, 5                                    # 180
adde 17, 5, 5                                     # 181
subfe 17, 5, 5                                    # 182
addme 17, 5                                       # 183
subfme 17, 5                                      # 184
subfze 17, 5                                      # 185
addze 17, 5                                       # 186
addi 17, 1, -30805                                # 187
addis 17, 1, -30805                               # 188
addic 17, 1, -30805                               # 189
addic. 17, 1, -30805                              # 190
subfic 17, 1, -30805                              # 191
add 17, 1, 4                                      # 192
subf 17, 1, 4                                     # 193
addc 17, 1, 4                                     # 194
subfc 17, 1, 4                                    # 195
adde 17, 1, 4                                     # 196
subfe 17, 1, 4                                    # 197
addme 17, 1                                       # 198
subfme 17, 1                                      # 199
subfze 17, 1                                      # 200
addze 17, 1                                       # 201
addi 17, 4, 25517                                 # 202
addis 17, 4, 25517                                # 203
addic 17, 4, 25517                                # 204
addic. 17, 4, 25517                               # 205
subfic 17, 4, 25517                               # 206
add 17, 4, 8                                      # 207
subf 17, 4, 8                                     # 208
addc 17, 4, 8                                     # 209
subfc 17, 4, 8                                    # 210
adde 17, 4, 8                                     # 211
subfe 17, 4, 8                                    # 212
addme 17, 4                                       # 213
subfme 17, 4                                      # 214
subfze 17, 4                                      # 215
addze 17, 4                                       # 216
addi 17, 2, 14633                                 # 217
addis 17, 2, 14633                                # 218
addic 17, 2, 14633                                # 219
addic. 17, 2, 14633                               # 220
subfic 17, 2, 14633                               # 221
add 17, 2, 4                                      # 222
subf 17, 2, 4                                     # 223
addc 17, 2, 4                                     # 224
subfc 17, 2, 4                                    # 225
adde 17, 2, 4                                     # 226
subfe 17, 2, 4                                    # 227
addme 17, 2                                       # 228
subfme 17, 2                                      # 229
subfze 17, 2                                      # 230
addze 17, 2                                       # 231
addi 17, 2, -10641                                # 232
addis 17, 2, -10641                               # 233
addic 17, 2, -10641                               # 234
addic. 17, 2, -10641                              # 235
subfic 17, 2, -10641                              # 236
add 17, 2, 5                                      # 237
subf 17, 2, 5                                     # 238
addc 17, 2, 5                                     # 239
subfc 17, 2, 5                                    # 240
adde 17, 2, 5                                     # 241
subfe 17, 2, 5                                    # 242
addme 17, 2                                       # 243
subfme 17, 2                                      # 244
subfze 17, 2                                      # 245
addze 17, 2                                       # 246
addi 17, 8, -31770                                # 247
addis 17, 8, -31770                               # 248
addic 17, 8, -31770                               # 249
addic. 17, 8, -31770                              # 250
subfic 17, 8, -31770                              # 251
add 17, 8, 2                                      # 252
subf 17, 8, 2                                     # 253
addc 17, 8, 2                                     # 254
subfc 17, 8, 2                                    # 255
adde 17, 8, 2                                     # 256
subfe 17, 8, 2                                    # 257
addme 17, 8                                       # 258
subfme 17, 8                                      # 259
subfze 17, 8                                      # 260
addze 17, 8                                       # 261
addi 17, 4, -5084                                 # 262
addis 17, 4, -5084                                # 263
addic 17, 4, -5084                                # 264
addic. 17, 4, -5084                               # 265
subfic 17, 4, -5084                               # 266
add 17, 4, 8                                      # 267
subf 17, 4, 8                                     # 268
addc 17, 4, 8                                     # 269
subfc 17, 4, 8                                    # 270
adde 17, 4, 8                                     # 271
subfe 17, 4, 8                                    # 272
addme 17, 4                                       # 273
subfme 17, 4                                      # 274
subfze 17, 4                                      # 275
addze 17, 4                                       # 276
addi 17, 4, 30458                                 # 277
addis 17, 4, 30458                                # 278
addic 17, 4, 30458                                # 279
addic. 17, 4, 30458                               # 280
subfic 17, 4, 30458                               # 281
add 17, 4, 1                                      # 282
subf 17, 4, 1                                     # 283
addc 17, 4, 1                                     # 284
subfc 17, 4, 1                                    # 285
adde 17, 4, 1                                     # 286
subfe 17, 4, 1                                    # 287
addme 17, 4                                       # 288
subfme 17, 4                                      # 289
subfze 17, 4                                      # 290
addze 17, 4                                       # 291
addi 17, 7, 24956                                 # 292
addis 17, 7, 24956                                # 293
addic 17, 7, 24956                                # 294
addic. 17, 7, 24956                               # 295
subfic 17, 7, 24956                               # 296
add 17, 7, 5                                      # 297
subf 17, 7, 5                                     # 298
addc 17, 7, 5                                     # 299
subfc 17, 7, 5                                    # 300
adde 17, 7, 5                                     # 301
subfe 17, 7, 5                                    # 302
addme 17, 7                                       # 303
subfme 17, 7                                      # 304
subfze 17, 7                                      # 305
addze 17, 7                                       # 306
addi 17, 4, -12831                                # 307
addis 17, 4, -12831                               # 308
addic 17, 4, -12831                               # 309
addic. 17, 4, -12831                              # 310
subfic 17, 4, -12831                              # 311
add 17, 4, 3                                      # 312
subf 17, 4, 3                                     # 313
addc 17, 4, 3                                     # 314
subfc 17, 4, 3                                    # 315
adde 17, 4, 3                                     # 316
subfe 17, 4, 3                                    # 317
addme 17, 4                                       # 318
subfme 17, 4                                      # 319
subfze 17, 4                                      # 320
addze 17, 4                                       # 321
nop
li 31, 1
hang: b hang
.section .data
data:
