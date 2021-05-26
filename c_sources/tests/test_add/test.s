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
addi 17, 6, -7870                                 # 20
addis 17, 6, -7870                                # 21
addic 17, 6, -7870                                # 22
addic. 17, 6, -7870                               # 23
subfic 17, 6, -7870                               # 24
add 17, 6, 5                                      # 25
subf 17, 6, 5                                     # 26
addc 17, 6, 5                                     # 27
subfc 17, 6, 5                                    # 28
adde 17, 6, 5                                     # 29
subfe 17, 6, 5                                    # 30
addme 17, 6                                       # 31
subfme 17, 6                                      # 32
subfze 17, 6                                      # 33
addze 17, 6                                       # 34
addi 17, 5, -11330                                # 35
addis 17, 5, -11330                               # 36
addic 17, 5, -11330                               # 37
addic. 17, 5, -11330                              # 38
subfic 17, 5, -11330                              # 39
add 17, 5, 1                                      # 40
subf 17, 5, 1                                     # 41
addc 17, 5, 1                                     # 42
subfc 17, 5, 1                                    # 43
adde 17, 5, 1                                     # 44
subfe 17, 5, 1                                    # 45
addme 17, 5                                       # 46
subfme 17, 5                                      # 47
subfze 17, 5                                      # 48
addze 17, 5                                       # 49
addi 17, 5, 4046                                  # 50
addis 17, 5, 4046                                 # 51
addic 17, 5, 4046                                 # 52
addic. 17, 5, 4046                                # 53
subfic 17, 5, 4046                                # 54
add 17, 5, 6                                      # 55
subf 17, 5, 6                                     # 56
addc 17, 5, 6                                     # 57
subfc 17, 5, 6                                    # 58
adde 17, 5, 6                                     # 59
subfe 17, 5, 6                                    # 60
addme 17, 5                                       # 61
subfme 17, 5                                      # 62
subfze 17, 5                                      # 63
addze 17, 5                                       # 64
addi 17, 6, -8584                                 # 65
addis 17, 6, -8584                                # 66
addic 17, 6, -8584                                # 67
addic. 17, 6, -8584                               # 68
subfic 17, 6, -8584                               # 69
add 17, 6, 1                                      # 70
subf 17, 6, 1                                     # 71
addc 17, 6, 1                                     # 72
subfc 17, 6, 1                                    # 73
adde 17, 6, 1                                     # 74
subfe 17, 6, 1                                    # 75
addme 17, 6                                       # 76
subfme 17, 6                                      # 77
subfze 17, 6                                      # 78
addze 17, 6                                       # 79
addi 17, 5, 6740                                  # 80
addis 17, 5, 6740                                 # 81
addic 17, 5, 6740                                 # 82
addic. 17, 5, 6740                                # 83
subfic 17, 5, 6740                                # 84
add 17, 5, 8                                      # 85
subf 17, 5, 8                                     # 86
addc 17, 5, 8                                     # 87
subfc 17, 5, 8                                    # 88
adde 17, 5, 8                                     # 89
subfe 17, 5, 8                                    # 90
addme 17, 5                                       # 91
subfme 17, 5                                      # 92
subfze 17, 5                                      # 93
addze 17, 5                                       # 94
addi 17, 1, 24123                                 # 95
addis 17, 1, 24123                                # 96
addic 17, 1, 24123                                # 97
addic. 17, 1, 24123                               # 98
subfic 17, 1, 24123                               # 99
add 17, 1, 5                                      # 100
subf 17, 1, 5                                     # 101
addc 17, 1, 5                                     # 102
subfc 17, 1, 5                                    # 103
adde 17, 1, 5                                     # 104
subfe 17, 1, 5                                    # 105
addme 17, 1                                       # 106
subfme 17, 1                                      # 107
subfze 17, 1                                      # 108
addze 17, 1                                       # 109
addi 17, 6, 15098                                 # 110
addis 17, 6, 15098                                # 111
addic 17, 6, 15098                                # 112
addic. 17, 6, 15098                               # 113
subfic 17, 6, 15098                               # 114
add 17, 6, 7                                      # 115
subf 17, 6, 7                                     # 116
addc 17, 6, 7                                     # 117
subfc 17, 6, 7                                    # 118
adde 17, 6, 7                                     # 119
subfe 17, 6, 7                                    # 120
addme 17, 6                                       # 121
subfme 17, 6                                      # 122
subfze 17, 6                                      # 123
addze 17, 6                                       # 124
addi 17, 2, -20866                                # 125
addis 17, 2, -20866                               # 126
addic 17, 2, -20866                               # 127
addic. 17, 2, -20866                              # 128
subfic 17, 2, -20866                              # 129
add 17, 2, 1                                      # 130
subf 17, 2, 1                                     # 131
addc 17, 2, 1                                     # 132
subfc 17, 2, 1                                    # 133
adde 17, 2, 1                                     # 134
subfe 17, 2, 1                                    # 135
addme 17, 2                                       # 136
subfme 17, 2                                      # 137
subfze 17, 2                                      # 138
addze 17, 2                                       # 139
addi 17, 8, -11942                                # 140
addis 17, 8, -11942                               # 141
addic 17, 8, -11942                               # 142
addic. 17, 8, -11942                              # 143
subfic 17, 8, -11942                              # 144
add 17, 8, 4                                      # 145
subf 17, 8, 4                                     # 146
addc 17, 8, 4                                     # 147
subfc 17, 8, 4                                    # 148
adde 17, 8, 4                                     # 149
subfe 17, 8, 4                                    # 150
addme 17, 8                                       # 151
subfme 17, 8                                      # 152
subfze 17, 8                                      # 153
addze 17, 8                                       # 154
addi 17, 6, -31767                                # 155
addis 17, 6, -31767                               # 156
addic 17, 6, -31767                               # 157
addic. 17, 6, -31767                              # 158
subfic 17, 6, -31767                              # 159
add 17, 6, 1                                      # 160
subf 17, 6, 1                                     # 161
addc 17, 6, 1                                     # 162
subfc 17, 6, 1                                    # 163
adde 17, 6, 1                                     # 164
subfe 17, 6, 1                                    # 165
addme 17, 6                                       # 166
subfme 17, 6                                      # 167
subfze 17, 6                                      # 168
addze 17, 6                                       # 169
addi 17, 5, -6952                                 # 170
addis 17, 5, -6952                                # 171
addic 17, 5, -6952                                # 172
addic. 17, 5, -6952                               # 173
subfic 17, 5, -6952                               # 174
add 17, 5, 5                                      # 175
subf 17, 5, 5                                     # 176
addc 17, 5, 5                                     # 177
subfc 17, 5, 5                                    # 178
adde 17, 5, 5                                     # 179
subfe 17, 5, 5                                    # 180
addme 17, 5                                       # 181
subfme 17, 5                                      # 182
subfze 17, 5                                      # 183
addze 17, 5                                       # 184
addi 17, 1, -30805                                # 185
addis 17, 1, -30805                               # 186
addic 17, 1, -30805                               # 187
addic. 17, 1, -30805                              # 188
subfic 17, 1, -30805                              # 189
add 17, 1, 4                                      # 190
subf 17, 1, 4                                     # 191
addc 17, 1, 4                                     # 192
subfc 17, 1, 4                                    # 193
adde 17, 1, 4                                     # 194
subfe 17, 1, 4                                    # 195
addme 17, 1                                       # 196
subfme 17, 1                                      # 197
subfze 17, 1                                      # 198
addze 17, 1                                       # 199
addi 17, 4, 25517                                 # 200
addis 17, 4, 25517                                # 201
addic 17, 4, 25517                                # 202
addic. 17, 4, 25517                               # 203
subfic 17, 4, 25517                               # 204
add 17, 4, 8                                      # 205
subf 17, 4, 8                                     # 206
addc 17, 4, 8                                     # 207
subfc 17, 4, 8                                    # 208
adde 17, 4, 8                                     # 209
subfe 17, 4, 8                                    # 210
addme 17, 4                                       # 211
subfme 17, 4                                      # 212
subfze 17, 4                                      # 213
addze 17, 4                                       # 214
addi 17, 2, 14633                                 # 215
addis 17, 2, 14633                                # 216
addic 17, 2, 14633                                # 217
addic. 17, 2, 14633                               # 218
subfic 17, 2, 14633                               # 219
add 17, 2, 4                                      # 220
subf 17, 2, 4                                     # 221
addc 17, 2, 4                                     # 222
subfc 17, 2, 4                                    # 223
adde 17, 2, 4                                     # 224
subfe 17, 2, 4                                    # 225
addme 17, 2                                       # 226
subfme 17, 2                                      # 227
subfze 17, 2                                      # 228
addze 17, 2                                       # 229
addi 17, 2, -10641                                # 230
addis 17, 2, -10641                               # 231
addic 17, 2, -10641                               # 232
addic. 17, 2, -10641                              # 233
subfic 17, 2, -10641                              # 234
add 17, 2, 5                                      # 235
subf 17, 2, 5                                     # 236
addc 17, 2, 5                                     # 237
subfc 17, 2, 5                                    # 238
adde 17, 2, 5                                     # 239
subfe 17, 2, 5                                    # 240
addme 17, 2                                       # 241
subfme 17, 2                                      # 242
subfze 17, 2                                      # 243
addze 17, 2                                       # 244
addi 17, 8, -31770                                # 245
addis 17, 8, -31770                               # 246
addic 17, 8, -31770                               # 247
addic. 17, 8, -31770                              # 248
subfic 17, 8, -31770                              # 249
add 17, 8, 2                                      # 250
subf 17, 8, 2                                     # 251
addc 17, 8, 2                                     # 252
subfc 17, 8, 2                                    # 253
adde 17, 8, 2                                     # 254
subfe 17, 8, 2                                    # 255
addme 17, 8                                       # 256
subfme 17, 8                                      # 257
subfze 17, 8                                      # 258
addze 17, 8                                       # 259
addi 17, 4, -5084                                 # 260
addis 17, 4, -5084                                # 261
addic 17, 4, -5084                                # 262
addic. 17, 4, -5084                               # 263
subfic 17, 4, -5084                               # 264
add 17, 4, 8                                      # 265
subf 17, 4, 8                                     # 266
addc 17, 4, 8                                     # 267
subfc 17, 4, 8                                    # 268
adde 17, 4, 8                                     # 269
subfe 17, 4, 8                                    # 270
addme 17, 4                                       # 271
subfme 17, 4                                      # 272
subfze 17, 4                                      # 273
addze 17, 4                                       # 274
addi 17, 4, 30458                                 # 275
addis 17, 4, 30458                                # 276
addic 17, 4, 30458                                # 277
addic. 17, 4, 30458                               # 278
subfic 17, 4, 30458                               # 279
add 17, 4, 1                                      # 280
subf 17, 4, 1                                     # 281
addc 17, 4, 1                                     # 282
subfc 17, 4, 1                                    # 283
adde 17, 4, 1                                     # 284
subfe 17, 4, 1                                    # 285
addme 17, 4                                       # 286
subfme 17, 4                                      # 287
subfze 17, 4                                      # 288
addze 17, 4                                       # 289
addi 17, 7, 24956                                 # 290
addis 17, 7, 24956                                # 291
addic 17, 7, 24956                                # 292
addic. 17, 7, 24956                               # 293
subfic 17, 7, 24956                               # 294
add 17, 7, 5                                      # 295
subf 17, 7, 5                                     # 296
addc 17, 7, 5                                     # 297
subfc 17, 7, 5                                    # 298
adde 17, 7, 5                                     # 299
subfe 17, 7, 5                                    # 300
addme 17, 7                                       # 301
subfme 17, 7                                      # 302
subfze 17, 7                                      # 303
addze 17, 7                                       # 304
addi 17, 4, -12831                                # 305
addis 17, 4, -12831                               # 306
addic 17, 4, -12831                               # 307
addic. 17, 4, -12831                              # 308
subfic 17, 4, -12831                              # 309
add 17, 4, 3                                      # 310
subf 17, 4, 3                                     # 311
addc 17, 4, 3                                     # 312
subfc 17, 4, 3                                    # 313
adde 17, 4, 3                                     # 314
subfe 17, 4, 3                                    # 315
addme 17, 4                                       # 316
subfme 17, 4                                      # 317
subfze 17, 4                                      # 318
addze 17, 4                                       # 319
nop
li 31, 1
hang: b hang
