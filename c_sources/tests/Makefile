TOOL=powerpc64-linux-gnu
LE ?= 1

ASFLAGS=-mpower9
LDFLAGS=-T linker.lds
OBFLAGS=

dirs= test_simple

ifeq ($(LE), 1)
	ASFLAGS += -mlittle
	LDFLAGS += -EL
	OBFLAGS += -I elf64-little
else
	ASFLAGS += -mbig
	LDFLAGS += -EB
	OBFLAGS += -I elf64-big
endif


%.o: %.s
	$(TOOL)-as $(ASFLAGS) -o $@ $<

%.elf: %.o linker.lds
	$(TOOL)-ld $(LDFLAGS) -o $@ $<
%.bin: %.elf
	$(TOOL)-objcopy $(OBFLAGS) -O binary $< $@

all: $(foreach dir, $(dirs), $(dir)/test.bin)

clean:
	-rm -f $(foreach dir, $(dirs), $(wildcard $(dir)/*.o))
	-rm -f $(foreach dir, $(dirs), $(wildcard $(dir)/*.elf))
	-rm -f $(foreach dir, $(dirs), $(wildcard $(dir)/*.bin))
