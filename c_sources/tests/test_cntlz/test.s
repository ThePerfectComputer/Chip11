
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

test_cntlz:
li 17, 0                                          # 22
cntlzw 18, 17                                     # 23
cntlzd 19, 17                                     # 24
li 17, 1                                          # 25
cntlzw 18, 17                                     # 26
cntlzd 19, 17                                     # 27
li 17, 3                                          # 28
cntlzw 18, 17                                     # 29
cntlzd 19, 17                                     # 30
li 17, 7                                          # 31
cntlzw 18, 17                                     # 32
cntlzd 19, 17                                     # 33
li 17, 15                                         # 34
cntlzw 18, 17                                     # 35
cntlzd 19, 17                                     # 36
li 17, 31                                         # 37
cntlzw 18, 17                                     # 38
cntlzd 19, 17                                     # 39
li 17, 63                                         # 40
cntlzw 18, 17                                     # 41
cntlzd 19, 17                                     # 42
li 17, 127                                        # 43
cntlzw 18, 17                                     # 44
cntlzd 19, 17                                     # 45
li 17, 255                                        # 46
cntlzw 18, 17                                     # 47
cntlzd 19, 17                                     # 48
li 17, 511                                        # 49
cntlzw 18, 17                                     # 50
cntlzd 19, 17                                     # 51
li 17, 1023                                       # 52
cntlzw 18, 17                                     # 53
cntlzd 19, 17                                     # 54
li 17, 2047                                       # 55
cntlzw 18, 17                                     # 56
cntlzd 19, 17                                     # 57
li 17, 4095                                       # 58
cntlzw 18, 17                                     # 59
cntlzd 19, 17                                     # 60
li 17, 8191                                       # 61
cntlzw 18, 17                                     # 62
cntlzd 19, 17                                     # 63
li 17, 16383                                      # 64
cntlzw 18, 17                                     # 65
cntlzd 19, 17                                     # 66
li 17, 32767                                      # 67
cntlzw 18, 17                                     # 68
cntlzd 19, 17                                     # 69
nop
li 31, 1
hang: b hang
.section .data
data:
