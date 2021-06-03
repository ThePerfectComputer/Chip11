#include "uart.h"
#include "stdint.h"

#define GET_32(x) (*((volatile uint32_t*)(x)))

struct uart_control_t {
    volatile uint32_t clkdiv;
    uint32_t padding[3];
    volatile uint32_t frame;
    uint32_t padding2[3];
    volatile uint32_t tx;
    uint32_t padding3[3];
    volatile uint32_t rx;
    uint32_t padding4[3];
} __attribute__((aligned(16)));
struct uart_control_t * const uart_ptr = (struct uart_control_t*) UART_BASE;

void uart_setup(void){
    uart_ptr->clkdiv = 1;
    uart_ptr->frame = 7;
}

void uart_putc(char c){
    while((uart_ptr->tx & 0x2) == 0){}
    
    uart_ptr->tx = c;
}

void uart_flush(void){
    // wait until the uart queue empties
    while((uart_ptr->tx & 0x1) == 0){}
    // wait some extra cycles so it shows up on the console
    for(int i=0; i<200; i++){
	__asm__ __volatile__("nop");
    }
}

void uart_puts(const char *str){
    char c;
    while((c = *str++) != 0){
	uart_putc(c);
    }
}

char uart_getc(void){
    while((uart_ptr->tx & 0x4) == 0){}
    char c;
    
    c = uart_ptr->rx & 0xff;

    return c;
}
