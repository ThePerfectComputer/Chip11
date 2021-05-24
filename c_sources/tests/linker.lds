MEMORY
{
    ram : ORIGIN = 0x00000000, LENGTH = 128M
}

SECTIONS
{
    .text : { *(.text*) } > ram
    .bss : { *(.bss*) } > ram
}

