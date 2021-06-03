#include "uart.h"

int main(void){

    uart_setup();
    uart_putc('a');
    uart_flush();
    return 5;
}
