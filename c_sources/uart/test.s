	.set UART_BASE, 0x20000000
	.set UART_CLKDIV, 0x0
	.set UART_FRAME, 0x10
	.set UART_TX, 0x20
	.set UART_RX, 0x30

	.org 0
	b start
	.org 16
start:	
	# Load the uart address into r8
	lis %r8, UART_BASE@h
	ori %r8, %r8, UART_BASE@l

	# set the baud rate to something?
	## lwz %r1, UART_CLKDIV(%r8)
	li %r1, 0x40
	stw %r1, UART_CLKDIV(%r8)

	# set the frame info
	li %r1, 7
	stw %r1, UART_FRAME(%r8)

	# transmit 0x55
	li %r1, 0x55
	stw %r1, UART_TX(%r8)
	nop
	nop
	nop

hang:	b hang
	
	

	
