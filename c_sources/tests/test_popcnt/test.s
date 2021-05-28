
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

test_popcnt:
li 17, 1                                          # 22
sldi 18, 17, 0                                    # 23
addi 18, 18, -1                                   # 24
popcntd 19, 18                                    # 25
popcntw 20, 18                                    # 26
popcntb 21, 18                                    # 27
sldi 18, 17, 1                                    # 28
addi 18, 18, -1                                   # 29
popcntd 19, 18                                    # 30
popcntw 20, 18                                    # 31
popcntb 21, 18                                    # 32
sldi 18, 17, 2                                    # 33
addi 18, 18, -1                                   # 34
popcntd 19, 18                                    # 35
popcntw 20, 18                                    # 36
popcntb 21, 18                                    # 37
sldi 18, 17, 3                                    # 38
addi 18, 18, -1                                   # 39
popcntd 19, 18                                    # 40
popcntw 20, 18                                    # 41
popcntb 21, 18                                    # 42
sldi 18, 17, 4                                    # 43
addi 18, 18, -1                                   # 44
popcntd 19, 18                                    # 45
popcntw 20, 18                                    # 46
popcntb 21, 18                                    # 47
sldi 18, 17, 5                                    # 48
addi 18, 18, -1                                   # 49
popcntd 19, 18                                    # 50
popcntw 20, 18                                    # 51
popcntb 21, 18                                    # 52
sldi 18, 17, 6                                    # 53
addi 18, 18, -1                                   # 54
popcntd 19, 18                                    # 55
popcntw 20, 18                                    # 56
popcntb 21, 18                                    # 57
sldi 18, 17, 7                                    # 58
addi 18, 18, -1                                   # 59
popcntd 19, 18                                    # 60
popcntw 20, 18                                    # 61
popcntb 21, 18                                    # 62
sldi 18, 17, 8                                    # 63
addi 18, 18, -1                                   # 64
popcntd 19, 18                                    # 65
popcntw 20, 18                                    # 66
popcntb 21, 18                                    # 67
sldi 18, 17, 9                                    # 68
addi 18, 18, -1                                   # 69
popcntd 19, 18                                    # 70
popcntw 20, 18                                    # 71
popcntb 21, 18                                    # 72
sldi 18, 17, 10                                   # 73
addi 18, 18, -1                                   # 74
popcntd 19, 18                                    # 75
popcntw 20, 18                                    # 76
popcntb 21, 18                                    # 77
sldi 18, 17, 11                                   # 78
addi 18, 18, -1                                   # 79
popcntd 19, 18                                    # 80
popcntw 20, 18                                    # 81
popcntb 21, 18                                    # 82
sldi 18, 17, 12                                   # 83
addi 18, 18, -1                                   # 84
popcntd 19, 18                                    # 85
popcntw 20, 18                                    # 86
popcntb 21, 18                                    # 87
sldi 18, 17, 13                                   # 88
addi 18, 18, -1                                   # 89
popcntd 19, 18                                    # 90
popcntw 20, 18                                    # 91
popcntb 21, 18                                    # 92
sldi 18, 17, 14                                   # 93
addi 18, 18, -1                                   # 94
popcntd 19, 18                                    # 95
popcntw 20, 18                                    # 96
popcntb 21, 18                                    # 97
sldi 18, 17, 15                                   # 98
addi 18, 18, -1                                   # 99
popcntd 19, 18                                    # 100
popcntw 20, 18                                    # 101
popcntb 21, 18                                    # 102
sldi 18, 17, 16                                   # 103
addi 18, 18, -1                                   # 104
popcntd 19, 18                                    # 105
popcntw 20, 18                                    # 106
popcntb 21, 18                                    # 107
sldi 18, 17, 17                                   # 108
addi 18, 18, -1                                   # 109
popcntd 19, 18                                    # 110
popcntw 20, 18                                    # 111
popcntb 21, 18                                    # 112
sldi 18, 17, 18                                   # 113
addi 18, 18, -1                                   # 114
popcntd 19, 18                                    # 115
popcntw 20, 18                                    # 116
popcntb 21, 18                                    # 117
sldi 18, 17, 19                                   # 118
addi 18, 18, -1                                   # 119
popcntd 19, 18                                    # 120
popcntw 20, 18                                    # 121
popcntb 21, 18                                    # 122
sldi 18, 17, 20                                   # 123
addi 18, 18, -1                                   # 124
popcntd 19, 18                                    # 125
popcntw 20, 18                                    # 126
popcntb 21, 18                                    # 127
sldi 18, 17, 21                                   # 128
addi 18, 18, -1                                   # 129
popcntd 19, 18                                    # 130
popcntw 20, 18                                    # 131
popcntb 21, 18                                    # 132
sldi 18, 17, 22                                   # 133
addi 18, 18, -1                                   # 134
popcntd 19, 18                                    # 135
popcntw 20, 18                                    # 136
popcntb 21, 18                                    # 137
sldi 18, 17, 23                                   # 138
addi 18, 18, -1                                   # 139
popcntd 19, 18                                    # 140
popcntw 20, 18                                    # 141
popcntb 21, 18                                    # 142
sldi 18, 17, 24                                   # 143
addi 18, 18, -1                                   # 144
popcntd 19, 18                                    # 145
popcntw 20, 18                                    # 146
popcntb 21, 18                                    # 147
sldi 18, 17, 25                                   # 148
addi 18, 18, -1                                   # 149
popcntd 19, 18                                    # 150
popcntw 20, 18                                    # 151
popcntb 21, 18                                    # 152
sldi 18, 17, 26                                   # 153
addi 18, 18, -1                                   # 154
popcntd 19, 18                                    # 155
popcntw 20, 18                                    # 156
popcntb 21, 18                                    # 157
sldi 18, 17, 27                                   # 158
addi 18, 18, -1                                   # 159
popcntd 19, 18                                    # 160
popcntw 20, 18                                    # 161
popcntb 21, 18                                    # 162
sldi 18, 17, 28                                   # 163
addi 18, 18, -1                                   # 164
popcntd 19, 18                                    # 165
popcntw 20, 18                                    # 166
popcntb 21, 18                                    # 167
sldi 18, 17, 29                                   # 168
addi 18, 18, -1                                   # 169
popcntd 19, 18                                    # 170
popcntw 20, 18                                    # 171
popcntb 21, 18                                    # 172
sldi 18, 17, 30                                   # 173
addi 18, 18, -1                                   # 174
popcntd 19, 18                                    # 175
popcntw 20, 18                                    # 176
popcntb 21, 18                                    # 177
sldi 18, 17, 31                                   # 178
addi 18, 18, -1                                   # 179
popcntd 19, 18                                    # 180
popcntw 20, 18                                    # 181
popcntb 21, 18                                    # 182
sldi 18, 17, 32                                   # 183
addi 18, 18, -1                                   # 184
popcntd 19, 18                                    # 185
popcntw 20, 18                                    # 186
popcntb 21, 18                                    # 187
sldi 18, 17, 33                                   # 188
addi 18, 18, -1                                   # 189
popcntd 19, 18                                    # 190
popcntw 20, 18                                    # 191
popcntb 21, 18                                    # 192
sldi 18, 17, 34                                   # 193
addi 18, 18, -1                                   # 194
popcntd 19, 18                                    # 195
popcntw 20, 18                                    # 196
popcntb 21, 18                                    # 197
sldi 18, 17, 35                                   # 198
addi 18, 18, -1                                   # 199
popcntd 19, 18                                    # 200
popcntw 20, 18                                    # 201
popcntb 21, 18                                    # 202
sldi 18, 17, 36                                   # 203
addi 18, 18, -1                                   # 204
popcntd 19, 18                                    # 205
popcntw 20, 18                                    # 206
popcntb 21, 18                                    # 207
sldi 18, 17, 37                                   # 208
addi 18, 18, -1                                   # 209
popcntd 19, 18                                    # 210
popcntw 20, 18                                    # 211
popcntb 21, 18                                    # 212
sldi 18, 17, 38                                   # 213
addi 18, 18, -1                                   # 214
popcntd 19, 18                                    # 215
popcntw 20, 18                                    # 216
popcntb 21, 18                                    # 217
sldi 18, 17, 39                                   # 218
addi 18, 18, -1                                   # 219
popcntd 19, 18                                    # 220
popcntw 20, 18                                    # 221
popcntb 21, 18                                    # 222
sldi 18, 17, 40                                   # 223
addi 18, 18, -1                                   # 224
popcntd 19, 18                                    # 225
popcntw 20, 18                                    # 226
popcntb 21, 18                                    # 227
sldi 18, 17, 41                                   # 228
addi 18, 18, -1                                   # 229
popcntd 19, 18                                    # 230
popcntw 20, 18                                    # 231
popcntb 21, 18                                    # 232
sldi 18, 17, 42                                   # 233
addi 18, 18, -1                                   # 234
popcntd 19, 18                                    # 235
popcntw 20, 18                                    # 236
popcntb 21, 18                                    # 237
sldi 18, 17, 43                                   # 238
addi 18, 18, -1                                   # 239
popcntd 19, 18                                    # 240
popcntw 20, 18                                    # 241
popcntb 21, 18                                    # 242
sldi 18, 17, 44                                   # 243
addi 18, 18, -1                                   # 244
popcntd 19, 18                                    # 245
popcntw 20, 18                                    # 246
popcntb 21, 18                                    # 247
sldi 18, 17, 45                                   # 248
addi 18, 18, -1                                   # 249
popcntd 19, 18                                    # 250
popcntw 20, 18                                    # 251
popcntb 21, 18                                    # 252
sldi 18, 17, 46                                   # 253
addi 18, 18, -1                                   # 254
popcntd 19, 18                                    # 255
popcntw 20, 18                                    # 256
popcntb 21, 18                                    # 257
sldi 18, 17, 47                                   # 258
addi 18, 18, -1                                   # 259
popcntd 19, 18                                    # 260
popcntw 20, 18                                    # 261
popcntb 21, 18                                    # 262
sldi 18, 17, 48                                   # 263
addi 18, 18, -1                                   # 264
popcntd 19, 18                                    # 265
popcntw 20, 18                                    # 266
popcntb 21, 18                                    # 267
sldi 18, 17, 49                                   # 268
addi 18, 18, -1                                   # 269
popcntd 19, 18                                    # 270
popcntw 20, 18                                    # 271
popcntb 21, 18                                    # 272
sldi 18, 17, 50                                   # 273
addi 18, 18, -1                                   # 274
popcntd 19, 18                                    # 275
popcntw 20, 18                                    # 276
popcntb 21, 18                                    # 277
sldi 18, 17, 51                                   # 278
addi 18, 18, -1                                   # 279
popcntd 19, 18                                    # 280
popcntw 20, 18                                    # 281
popcntb 21, 18                                    # 282
sldi 18, 17, 52                                   # 283
addi 18, 18, -1                                   # 284
popcntd 19, 18                                    # 285
popcntw 20, 18                                    # 286
popcntb 21, 18                                    # 287
sldi 18, 17, 53                                   # 288
addi 18, 18, -1                                   # 289
popcntd 19, 18                                    # 290
popcntw 20, 18                                    # 291
popcntb 21, 18                                    # 292
sldi 18, 17, 54                                   # 293
addi 18, 18, -1                                   # 294
popcntd 19, 18                                    # 295
popcntw 20, 18                                    # 296
popcntb 21, 18                                    # 297
sldi 18, 17, 55                                   # 298
addi 18, 18, -1                                   # 299
popcntd 19, 18                                    # 300
popcntw 20, 18                                    # 301
popcntb 21, 18                                    # 302
sldi 18, 17, 56                                   # 303
addi 18, 18, -1                                   # 304
popcntd 19, 18                                    # 305
popcntw 20, 18                                    # 306
popcntb 21, 18                                    # 307
sldi 18, 17, 57                                   # 308
addi 18, 18, -1                                   # 309
popcntd 19, 18                                    # 310
popcntw 20, 18                                    # 311
popcntb 21, 18                                    # 312
sldi 18, 17, 58                                   # 313
addi 18, 18, -1                                   # 314
popcntd 19, 18                                    # 315
popcntw 20, 18                                    # 316
popcntb 21, 18                                    # 317
sldi 18, 17, 59                                   # 318
addi 18, 18, -1                                   # 319
popcntd 19, 18                                    # 320
popcntw 20, 18                                    # 321
popcntb 21, 18                                    # 322
sldi 18, 17, 60                                   # 323
addi 18, 18, -1                                   # 324
popcntd 19, 18                                    # 325
popcntw 20, 18                                    # 326
popcntb 21, 18                                    # 327
sldi 18, 17, 61                                   # 328
addi 18, 18, -1                                   # 329
popcntd 19, 18                                    # 330
popcntw 20, 18                                    # 331
popcntb 21, 18                                    # 332
sldi 18, 17, 62                                   # 333
addi 18, 18, -1                                   # 334
popcntd 19, 18                                    # 335
popcntw 20, 18                                    # 336
popcntb 21, 18                                    # 337
sldi 18, 17, 63                                   # 338
addi 18, 18, -1                                   # 339
popcntd 19, 18                                    # 340
popcntw 20, 18                                    # 341
popcntb 21, 18                                    # 342
nop
li 31, 1
hang: b hang
.section .data
data:
