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

test_popcnt:
li 17, 1                                          # 20
sldi 18, 17, 0                                    # 21
addi 18, 18, -1                                   # 22
popcntd 19, 18                                    # 23
popcntw 20, 18                                    # 24
popcntb 21, 18                                    # 25
sldi 18, 17, 1                                    # 26
addi 18, 18, -1                                   # 27
popcntd 19, 18                                    # 28
popcntw 20, 18                                    # 29
popcntb 21, 18                                    # 30
sldi 18, 17, 2                                    # 31
addi 18, 18, -1                                   # 32
popcntd 19, 18                                    # 33
popcntw 20, 18                                    # 34
popcntb 21, 18                                    # 35
sldi 18, 17, 3                                    # 36
addi 18, 18, -1                                   # 37
popcntd 19, 18                                    # 38
popcntw 20, 18                                    # 39
popcntb 21, 18                                    # 40
sldi 18, 17, 4                                    # 41
addi 18, 18, -1                                   # 42
popcntd 19, 18                                    # 43
popcntw 20, 18                                    # 44
popcntb 21, 18                                    # 45
sldi 18, 17, 5                                    # 46
addi 18, 18, -1                                   # 47
popcntd 19, 18                                    # 48
popcntw 20, 18                                    # 49
popcntb 21, 18                                    # 50
sldi 18, 17, 6                                    # 51
addi 18, 18, -1                                   # 52
popcntd 19, 18                                    # 53
popcntw 20, 18                                    # 54
popcntb 21, 18                                    # 55
sldi 18, 17, 7                                    # 56
addi 18, 18, -1                                   # 57
popcntd 19, 18                                    # 58
popcntw 20, 18                                    # 59
popcntb 21, 18                                    # 60
sldi 18, 17, 8                                    # 61
addi 18, 18, -1                                   # 62
popcntd 19, 18                                    # 63
popcntw 20, 18                                    # 64
popcntb 21, 18                                    # 65
sldi 18, 17, 9                                    # 66
addi 18, 18, -1                                   # 67
popcntd 19, 18                                    # 68
popcntw 20, 18                                    # 69
popcntb 21, 18                                    # 70
sldi 18, 17, 10                                   # 71
addi 18, 18, -1                                   # 72
popcntd 19, 18                                    # 73
popcntw 20, 18                                    # 74
popcntb 21, 18                                    # 75
sldi 18, 17, 11                                   # 76
addi 18, 18, -1                                   # 77
popcntd 19, 18                                    # 78
popcntw 20, 18                                    # 79
popcntb 21, 18                                    # 80
sldi 18, 17, 12                                   # 81
addi 18, 18, -1                                   # 82
popcntd 19, 18                                    # 83
popcntw 20, 18                                    # 84
popcntb 21, 18                                    # 85
sldi 18, 17, 13                                   # 86
addi 18, 18, -1                                   # 87
popcntd 19, 18                                    # 88
popcntw 20, 18                                    # 89
popcntb 21, 18                                    # 90
sldi 18, 17, 14                                   # 91
addi 18, 18, -1                                   # 92
popcntd 19, 18                                    # 93
popcntw 20, 18                                    # 94
popcntb 21, 18                                    # 95
sldi 18, 17, 15                                   # 96
addi 18, 18, -1                                   # 97
popcntd 19, 18                                    # 98
popcntw 20, 18                                    # 99
popcntb 21, 18                                    # 100
sldi 18, 17, 16                                   # 101
addi 18, 18, -1                                   # 102
popcntd 19, 18                                    # 103
popcntw 20, 18                                    # 104
popcntb 21, 18                                    # 105
sldi 18, 17, 17                                   # 106
addi 18, 18, -1                                   # 107
popcntd 19, 18                                    # 108
popcntw 20, 18                                    # 109
popcntb 21, 18                                    # 110
sldi 18, 17, 18                                   # 111
addi 18, 18, -1                                   # 112
popcntd 19, 18                                    # 113
popcntw 20, 18                                    # 114
popcntb 21, 18                                    # 115
sldi 18, 17, 19                                   # 116
addi 18, 18, -1                                   # 117
popcntd 19, 18                                    # 118
popcntw 20, 18                                    # 119
popcntb 21, 18                                    # 120
sldi 18, 17, 20                                   # 121
addi 18, 18, -1                                   # 122
popcntd 19, 18                                    # 123
popcntw 20, 18                                    # 124
popcntb 21, 18                                    # 125
sldi 18, 17, 21                                   # 126
addi 18, 18, -1                                   # 127
popcntd 19, 18                                    # 128
popcntw 20, 18                                    # 129
popcntb 21, 18                                    # 130
sldi 18, 17, 22                                   # 131
addi 18, 18, -1                                   # 132
popcntd 19, 18                                    # 133
popcntw 20, 18                                    # 134
popcntb 21, 18                                    # 135
sldi 18, 17, 23                                   # 136
addi 18, 18, -1                                   # 137
popcntd 19, 18                                    # 138
popcntw 20, 18                                    # 139
popcntb 21, 18                                    # 140
sldi 18, 17, 24                                   # 141
addi 18, 18, -1                                   # 142
popcntd 19, 18                                    # 143
popcntw 20, 18                                    # 144
popcntb 21, 18                                    # 145
sldi 18, 17, 25                                   # 146
addi 18, 18, -1                                   # 147
popcntd 19, 18                                    # 148
popcntw 20, 18                                    # 149
popcntb 21, 18                                    # 150
sldi 18, 17, 26                                   # 151
addi 18, 18, -1                                   # 152
popcntd 19, 18                                    # 153
popcntw 20, 18                                    # 154
popcntb 21, 18                                    # 155
sldi 18, 17, 27                                   # 156
addi 18, 18, -1                                   # 157
popcntd 19, 18                                    # 158
popcntw 20, 18                                    # 159
popcntb 21, 18                                    # 160
sldi 18, 17, 28                                   # 161
addi 18, 18, -1                                   # 162
popcntd 19, 18                                    # 163
popcntw 20, 18                                    # 164
popcntb 21, 18                                    # 165
sldi 18, 17, 29                                   # 166
addi 18, 18, -1                                   # 167
popcntd 19, 18                                    # 168
popcntw 20, 18                                    # 169
popcntb 21, 18                                    # 170
sldi 18, 17, 30                                   # 171
addi 18, 18, -1                                   # 172
popcntd 19, 18                                    # 173
popcntw 20, 18                                    # 174
popcntb 21, 18                                    # 175
sldi 18, 17, 31                                   # 176
addi 18, 18, -1                                   # 177
popcntd 19, 18                                    # 178
popcntw 20, 18                                    # 179
popcntb 21, 18                                    # 180
sldi 18, 17, 32                                   # 181
addi 18, 18, -1                                   # 182
popcntd 19, 18                                    # 183
popcntw 20, 18                                    # 184
popcntb 21, 18                                    # 185
sldi 18, 17, 33                                   # 186
addi 18, 18, -1                                   # 187
popcntd 19, 18                                    # 188
popcntw 20, 18                                    # 189
popcntb 21, 18                                    # 190
sldi 18, 17, 34                                   # 191
addi 18, 18, -1                                   # 192
popcntd 19, 18                                    # 193
popcntw 20, 18                                    # 194
popcntb 21, 18                                    # 195
sldi 18, 17, 35                                   # 196
addi 18, 18, -1                                   # 197
popcntd 19, 18                                    # 198
popcntw 20, 18                                    # 199
popcntb 21, 18                                    # 200
sldi 18, 17, 36                                   # 201
addi 18, 18, -1                                   # 202
popcntd 19, 18                                    # 203
popcntw 20, 18                                    # 204
popcntb 21, 18                                    # 205
sldi 18, 17, 37                                   # 206
addi 18, 18, -1                                   # 207
popcntd 19, 18                                    # 208
popcntw 20, 18                                    # 209
popcntb 21, 18                                    # 210
sldi 18, 17, 38                                   # 211
addi 18, 18, -1                                   # 212
popcntd 19, 18                                    # 213
popcntw 20, 18                                    # 214
popcntb 21, 18                                    # 215
sldi 18, 17, 39                                   # 216
addi 18, 18, -1                                   # 217
popcntd 19, 18                                    # 218
popcntw 20, 18                                    # 219
popcntb 21, 18                                    # 220
sldi 18, 17, 40                                   # 221
addi 18, 18, -1                                   # 222
popcntd 19, 18                                    # 223
popcntw 20, 18                                    # 224
popcntb 21, 18                                    # 225
sldi 18, 17, 41                                   # 226
addi 18, 18, -1                                   # 227
popcntd 19, 18                                    # 228
popcntw 20, 18                                    # 229
popcntb 21, 18                                    # 230
sldi 18, 17, 42                                   # 231
addi 18, 18, -1                                   # 232
popcntd 19, 18                                    # 233
popcntw 20, 18                                    # 234
popcntb 21, 18                                    # 235
sldi 18, 17, 43                                   # 236
addi 18, 18, -1                                   # 237
popcntd 19, 18                                    # 238
popcntw 20, 18                                    # 239
popcntb 21, 18                                    # 240
sldi 18, 17, 44                                   # 241
addi 18, 18, -1                                   # 242
popcntd 19, 18                                    # 243
popcntw 20, 18                                    # 244
popcntb 21, 18                                    # 245
sldi 18, 17, 45                                   # 246
addi 18, 18, -1                                   # 247
popcntd 19, 18                                    # 248
popcntw 20, 18                                    # 249
popcntb 21, 18                                    # 250
sldi 18, 17, 46                                   # 251
addi 18, 18, -1                                   # 252
popcntd 19, 18                                    # 253
popcntw 20, 18                                    # 254
popcntb 21, 18                                    # 255
sldi 18, 17, 47                                   # 256
addi 18, 18, -1                                   # 257
popcntd 19, 18                                    # 258
popcntw 20, 18                                    # 259
popcntb 21, 18                                    # 260
sldi 18, 17, 48                                   # 261
addi 18, 18, -1                                   # 262
popcntd 19, 18                                    # 263
popcntw 20, 18                                    # 264
popcntb 21, 18                                    # 265
sldi 18, 17, 49                                   # 266
addi 18, 18, -1                                   # 267
popcntd 19, 18                                    # 268
popcntw 20, 18                                    # 269
popcntb 21, 18                                    # 270
sldi 18, 17, 50                                   # 271
addi 18, 18, -1                                   # 272
popcntd 19, 18                                    # 273
popcntw 20, 18                                    # 274
popcntb 21, 18                                    # 275
sldi 18, 17, 51                                   # 276
addi 18, 18, -1                                   # 277
popcntd 19, 18                                    # 278
popcntw 20, 18                                    # 279
popcntb 21, 18                                    # 280
sldi 18, 17, 52                                   # 281
addi 18, 18, -1                                   # 282
popcntd 19, 18                                    # 283
popcntw 20, 18                                    # 284
popcntb 21, 18                                    # 285
sldi 18, 17, 53                                   # 286
addi 18, 18, -1                                   # 287
popcntd 19, 18                                    # 288
popcntw 20, 18                                    # 289
popcntb 21, 18                                    # 290
sldi 18, 17, 54                                   # 291
addi 18, 18, -1                                   # 292
popcntd 19, 18                                    # 293
popcntw 20, 18                                    # 294
popcntb 21, 18                                    # 295
sldi 18, 17, 55                                   # 296
addi 18, 18, -1                                   # 297
popcntd 19, 18                                    # 298
popcntw 20, 18                                    # 299
popcntb 21, 18                                    # 300
sldi 18, 17, 56                                   # 301
addi 18, 18, -1                                   # 302
popcntd 19, 18                                    # 303
popcntw 20, 18                                    # 304
popcntb 21, 18                                    # 305
sldi 18, 17, 57                                   # 306
addi 18, 18, -1                                   # 307
popcntd 19, 18                                    # 308
popcntw 20, 18                                    # 309
popcntb 21, 18                                    # 310
sldi 18, 17, 58                                   # 311
addi 18, 18, -1                                   # 312
popcntd 19, 18                                    # 313
popcntw 20, 18                                    # 314
popcntb 21, 18                                    # 315
sldi 18, 17, 59                                   # 316
addi 18, 18, -1                                   # 317
popcntd 19, 18                                    # 318
popcntw 20, 18                                    # 319
popcntb 21, 18                                    # 320
sldi 18, 17, 60                                   # 321
addi 18, 18, -1                                   # 322
popcntd 19, 18                                    # 323
popcntw 20, 18                                    # 324
popcntb 21, 18                                    # 325
sldi 18, 17, 61                                   # 326
addi 18, 18, -1                                   # 327
popcntd 19, 18                                    # 328
popcntw 20, 18                                    # 329
popcntb 21, 18                                    # 330
sldi 18, 17, 62                                   # 331
addi 18, 18, -1                                   # 332
popcntd 19, 18                                    # 333
popcntw 20, 18                                    # 334
popcntb 21, 18                                    # 335
sldi 18, 17, 63                                   # 336
addi 18, 18, -1                                   # 337
popcntd 19, 18                                    # 338
popcntw 20, 18                                    # 339
popcntb 21, 18                                    # 340
nop
li 31, 1
hang: b hang
