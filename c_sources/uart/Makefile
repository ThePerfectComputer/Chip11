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

all: test_le.bin

%.ol: %.s
	$(TOOL)-as $(AS_LE) -o $@ $<
%.ob: %.s
	$(TOOL)-as $(AS_BE) -o $@ $<

%_le.elf: %.ol linker.lds
	$(TOOL)-ld $(LD_LE) -o $@ $<

%_be.elf: %.ob linker.lds
	$(TOOL)-ld $(LD_BE) -o $@ $<

%_le.bin: %_le.elf
	$(TOOL)-objcopy $(OB_LE) -O binary $< $@

%_be.bin: %_be.elf
	$(TOOL)-objcopy $(OB_BE) -O binary $< $@


clean:
	-rm -f *.ol *.ob
	-rm -f *.bin
	-rm -f *.elf

.PHONY: all clean 
.SECONDARY:
