
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

test_store4_update_reversed:
lis 17, ld_data_end@h                             # 22
ori 17, 17, ld_data_end@l                         # 23
mr 19, 17                                         # 24
stwu 6, -4(17)                                    # 25
addi 29, 29, 0                                    # 26
add 28, 28, 29                                    # 27
stwu 2, -4(17)                                    # 28
addi 29, 29, 1                                    # 29
add 28, 28, 29                                    # 30
stwu 3, -4(17)                                    # 31
addi 29, 29, 2                                    # 32
add 28, 28, 29                                    # 33
stwu 7, -4(17)                                    # 34
addi 29, 29, 3                                    # 35
add 28, 28, 29                                    # 36
stwu 2, -4(17)                                    # 37
addi 29, 29, 4                                    # 38
add 28, 28, 29                                    # 39
stwu 1, -4(17)                                    # 40
addi 29, 29, 5                                    # 41
add 28, 28, 29                                    # 42
stwu 3, -4(17)                                    # 43
addi 29, 29, 6                                    # 44
add 28, 28, 29                                    # 45
stwu 6, -4(17)                                    # 46
addi 29, 29, 7                                    # 47
add 28, 28, 29                                    # 48
lwzu 18, -4(19)                                   # 49
addi 29, 29, 0                                    # 50
add 28, 28, 29                                    # 51
lwzu 18, -4(19)                                   # 52
addi 29, 29, 1                                    # 53
add 28, 28, 29                                    # 54
lwzu 18, -4(19)                                   # 55
addi 29, 29, 2                                    # 56
add 28, 28, 29                                    # 57
lwzu 18, -4(19)                                   # 58
addi 29, 29, 3                                    # 59
add 28, 28, 29                                    # 60
lwzu 18, -4(19)                                   # 61
addi 29, 29, 4                                    # 62
add 28, 28, 29                                    # 63
lwzu 18, -4(19)                                   # 64
addi 29, 29, 5                                    # 65
add 28, 28, 29                                    # 66
lwzu 18, -4(19)                                   # 67
addi 29, 29, 6                                    # 68
add 28, 28, 29                                    # 69
lwzu 18, -4(19)                                   # 70
addi 29, 29, 7                                    # 71
add 28, 28, 29                                    # 72
nop
li 31, 1
hang: b hang
.section .data
data:
ld_data:
.long 0
.long 0
.long 0
.long 0
.long 0
.long 0
.long 0
.long 0
ld_data_end:
