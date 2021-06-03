MEMORY
{
   ram : ORIGIN = 0x0, LENGTH = 0x80000
}

SECTIONS{
. = 0x0000;
.text : { *(.text) } > ram
.rodata : { *(.rodata) } > ram
.data : { *(.data) } > ram
_toc_start = .;
.toc : { *(.toc) } > ram
_toc_end = .;

. = . + 0x100;
_stack_start = .;

}
