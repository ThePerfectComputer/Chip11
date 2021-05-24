	.org 0
	b _start
.org 0x10
_start:
	li 1, 0x1234
	li 2, 0x5678
	add 3, 1, 2
hang:	b hang
