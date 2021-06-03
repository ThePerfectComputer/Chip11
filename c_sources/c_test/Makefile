TOOL=powerpc64-linux-gnu
LE ?= 1

ASFLAGS=-mpower9
LDFLAGS=-T linker.lds
OBFLAGS=
CFLAGS=-Os -mcpu=power9  -c

AS_LE += $(ASFLAGS) -mlittle
LD_LE += $(LDFLAGS) -EL
OB_LE += $(OBFLAGS) -I elf64-little
AS_BE += $(ASFLAGS) -mbig
LD_BE += $(LDFLAGS) -EB
OB_BE += $(OBFLAGS) -I elf64-big
CC_LE += $(CFLAGS) -mlittle

all: test.bin

%.o: %.s
	$(TOOL)-as $(AS_LE) -o $@ $<

%.o: %.c
	$(TOOL)-gcc $(CC_LE) -o $@ $<

test.elf: start.o main.o uart.o
	$(TOOL)-ld $(LD_LE) -o $@ $^

%.bin: %.elf
	$(TOOL)-objcopy $(OB_LE) -O binary $< $@

clean:
	-rm -f *.ol *.ob
	-rm -f *.bin
	-rm -f *.elf

.PHONY: all clean 
.SECONDARY:
