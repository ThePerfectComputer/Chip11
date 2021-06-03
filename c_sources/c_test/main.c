#include "uart.h"

int main(void){

    uart_setup();
    while(1){
	char c = uart_getc();
	uart_putc(c);
    }
    uart_flush();
    return 5;
}
