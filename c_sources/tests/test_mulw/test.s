
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

test_mulw:
lis 17, -6745240265@h                             # 22
ori 17, 17, -6745240265@l                         # 23
mullw 18, 4, 17                                   # 24
mulhw 18, 4, 17                                   # 25
mulhwu 18, 4, 17                                  # 26
lis 17, -13486511279@h                            # 27
ori 17, 17, -13486511279@l                        # 28
mullw 18, 6, 17                                   # 29
mulhw 18, 6, 17                                   # 30
mulhwu 18, 6, 17                                  # 31
lis 17, -9796970537@h                             # 32
ori 17, 17, -9796970537@l                         # 33
mullw 18, 1, 17                                   # 34
mulhw 18, 1, 17                                   # 35
mulhwu 18, 1, 17                                  # 36
lis 17, -12897993300@h                            # 37
ori 17, 17, -12897993300@l                        # 38
mullw 18, 2, 17                                   # 39
mulhw 18, 2, 17                                   # 40
mulhwu 18, 2, 17                                  # 41
lis 17, -29613261489@h                            # 42
ori 17, 17, -29613261489@l                        # 43
mullw 18, 3, 17                                   # 44
mulhw 18, 3, 17                                   # 45
mulhwu 18, 3, 17                                  # 46
lis 17, -23693948069@h                            # 47
ori 17, 17, -23693948069@l                        # 48
mullw 18, 8, 17                                   # 49
mulhw 18, 8, 17                                   # 50
mulhwu 18, 8, 17                                  # 51
lis 17, -10922200609@h                            # 52
ori 17, 17, -10922200609@l                        # 53
mullw 18, 8, 17                                   # 54
mulhw 18, 8, 17                                   # 55
mulhwu 18, 8, 17                                  # 56
lis 17, -31942434412@h                            # 57
ori 17, 17, -31942434412@l                        # 58
mullw 18, 3, 17                                   # 59
mulhw 18, 3, 17                                   # 60
mulhwu 18, 3, 17                                  # 61
lis 17, -17349006643@h                            # 62
ori 17, 17, -17349006643@l                        # 63
mullw 18, 5, 17                                   # 64
mulhw 18, 5, 17                                   # 65
mulhwu 18, 5, 17                                  # 66
lis 17, -37085674@h                               # 67
ori 17, 17, -37085674@l                           # 68
mullw 18, 2, 17                                   # 69
mulhw 18, 2, 17                                   # 70
mulhwu 18, 2, 17                                  # 71
lis 17, -20419345966@h                            # 72
ori 17, 17, -20419345966@l                        # 73
mullw 18, 3, 17                                   # 74
mulhw 18, 3, 17                                   # 75
mulhwu 18, 3, 17                                  # 76
lis 17, -26829871101@h                            # 77
ori 17, 17, -26829871101@l                        # 78
mullw 18, 2, 17                                   # 79
mulhw 18, 2, 17                                   # 80
mulhwu 18, 2, 17                                  # 81
lis 17, -28957492431@h                            # 82
ori 17, 17, -28957492431@l                        # 83
mullw 18, 2, 17                                   # 84
mulhw 18, 2, 17                                   # 85
mulhwu 18, 2, 17                                  # 86
lis 17, -4961057486@h                             # 87
ori 17, 17, -4961057486@l                         # 88
mullw 18, 4, 17                                   # 89
mulhw 18, 4, 17                                   # 90
mulhwu 18, 4, 17                                  # 91
lis 17, -11963412304@h                            # 92
ori 17, 17, -11963412304@l                        # 93
mullw 18, 1, 17                                   # 94
mulhw 18, 1, 17                                   # 95
mulhwu 18, 1, 17                                  # 96
lis 17, -34198382802@h                            # 97
ori 17, 17, -34198382802@l                        # 98
mullw 18, 1, 17                                   # 99
mulhw 18, 1, 17                                   # 100
mulhwu 18, 1, 17                                  # 101
nop
li 31, 1
hang: b hang
.section .data
data:
