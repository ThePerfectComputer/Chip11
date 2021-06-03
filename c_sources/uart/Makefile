TOOL=powerpc64-linux-gnu
LE ?= 1

ASFLAGS=-mpower9
LDFLAGS=-T linker.lds
OBFLAGS=

AS_LE += $(ASFLAGS) -mlittle
LD_LE += $(LDFLAGS) -EL
OB_LE += $(OBFLAGS) -I elf64-little
AS_BE += $(ASFLAGS) -mbig
LD_BE += $(LDFLAGS) -EB
OB_BE += $(OBFLAGS) -I elf64-big

all: hello.bin echo.bin

%.o: %.s
	$(TOOL)-as $(AS_LE) -o $@ $<

%.elf: %.o linker.lds
	$(TOOL)-ld $(LD_LE) -o $@ $<

%.bin: %.elf
	$(TOOL)-objcopy $(OB_LE) -O binary $< $@

clean:
	-rm -f *.ol *.ob
	-rm -f *.bin
	-rm -f *.elf

.PHONY: all clean 
.SECONDARY:
