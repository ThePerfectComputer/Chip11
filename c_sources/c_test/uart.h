#ifndef _UART_H
#define _UART_H

#define UART_BASE 0x20000000UL

void uart_setup(void);
void uart_putc(char);
char uart_getc(void);
void uart_flush(void);
void uart_puts(const char*);

#endif
