#ifndef _UART_H
#define _UART_H

#define UART_BASE 0x20000000UL
#define UART_CLKDIV 0x0UL
#define UART_FRAME 0x10UL
#define UART_TX 0x20UL
#define UART_RX 0x30UL

void uart_setup(void);
void uart_putc(char);
char uart_getc(void);
void uart_flush(void);
void uart_puts(const char*);

#endif
