	.org 0
	b _start
.org 0x10
_start:
	li 1, 0x1234
	nop
hang:	b hang
