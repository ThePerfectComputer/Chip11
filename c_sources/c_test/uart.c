#include "uart.h"
#include "stdint.h"

#define GET_32(x) (*((volatile uint32_t*)(x)))

void uart_setup(void){
    GET_32(UART_BASE+UART_CLKDIV) = 1;
    GET_32(UART_BASE+UART_FRAME) = 7;
}

void uart_putc(char c){
    while((GET_32(UART_BASE + UART_TX) & 0x2) == 0){}
    
    GET_32(UART_BASE + UART_TX) = c;

}

void uart_flush(void){
    // wait until the uart queue empties
    while((GET_32(UART_BASE + UART_TX) & 0x1) == 0){}
    // wait some extra cycles so it shows up on the console
    for(int i=0; i<1000; i++){
	__asm__ __volatile__("nop");
    }
}

void uart_puts(const char *str){
    char c;
    while((c = *str++) != 0){
	uart_putc(c);
    }
}
