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

test_cntlz:
li 17, 0                                          # 20
cntlzw 18, 17                                     # 21
cntlzd 19, 17                                     # 22
li 17, 1                                          # 23
cntlzw 18, 17                                     # 24
cntlzd 19, 17                                     # 25
li 17, 3                                          # 26
cntlzw 18, 17                                     # 27
cntlzd 19, 17                                     # 28
li 17, 7                                          # 29
cntlzw 18, 17                                     # 30
cntlzd 19, 17                                     # 31
li 17, 15                                         # 32
cntlzw 18, 17                                     # 33
cntlzd 19, 17                                     # 34
li 17, 31                                         # 35
cntlzw 18, 17                                     # 36
cntlzd 19, 17                                     # 37
li 17, 63                                         # 38
cntlzw 18, 17                                     # 39
cntlzd 19, 17                                     # 40
li 17, 127                                        # 41
cntlzw 18, 17                                     # 42
cntlzd 19, 17                                     # 43
li 17, 255                                        # 44
cntlzw 18, 17                                     # 45
cntlzd 19, 17                                     # 46
li 17, 511                                        # 47
cntlzw 18, 17                                     # 48
cntlzd 19, 17                                     # 49
li 17, 1023                                       # 50
cntlzw 18, 17                                     # 51
cntlzd 19, 17                                     # 52
li 17, 2047                                       # 53
cntlzw 18, 17                                     # 54
cntlzd 19, 17                                     # 55
li 17, 4095                                       # 56
cntlzw 18, 17                                     # 57
cntlzd 19, 17                                     # 58
li 17, 8191                                       # 59
cntlzw 18, 17                                     # 60
cntlzd 19, 17                                     # 61
li 17, 16383                                      # 62
cntlzw 18, 17                                     # 63
cntlzd 19, 17                                     # 64
li 17, 32767                                      # 65
cntlzw 18, 17                                     # 66
cntlzd 19, 17                                     # 67
nop
li 31, 1
hang: b hang
