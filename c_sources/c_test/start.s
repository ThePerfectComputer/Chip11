	.org 0
	b _start

	.org 16
	.globl _start
_start:	
	lis 1, _stack_start@h
	ori 1, 1, _stack_start@l

	lis 3, main@h
	ori 3, 3, main@l
	ld 2, 8(3)   # load main's TOC
	ld 3, 0(3)   # load main's address

	mtctr 3
	bctrl

hang:	b hang
