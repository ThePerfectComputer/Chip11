#include "uart.h"

int main(void){

    uart_setup();
    uart_puts("Hello, this is a very long string");
    uart_flush();
    return 5;
}
