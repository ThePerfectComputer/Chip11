import unittest
import subprocess
import csv
from random import Random
import os
import pathlib

os.chdir(os.path.dirname(os.path.realpath(__file__)))

cur_insn = 0
insn_dict = {}

# Translate between number of bytes and the assembly directive to store a constant
data_directives = {
    1: ".byte",
    2: ".short",
    4: ".long",
    8: ".quad"
}

class ASMTestCase(unittest.TestCase):

    def setUp(self):
        self.instructions = []
        self.data = []
        self.data.extend([".section .data", "data:"])
        self.generate_data()
        global cur_insn
        cur_insn = len(self.instructions[0].splitlines())
        self.rand = Random(self.id())

    def generate_data(self):
        self.instructions.extend(["""
.section .text
.org 0
b _start
.org 0x10
_start:
generate_data:
lis 1, 0xdead,
ori 1, 1, 0xbeef
lis 2, 0x1234
ori 2, 2, 0x5678
li 3, 0x1000
li 4, -0x1000
li 5, -0x5a5a
li 6, 0x5a5a
li 7, -1
lis 8, 0x1234
ori 8, 8, 0x5678
sldi 8, 8, 32
oris 8, 8, 0x9abc
ori 8, 8, 0xdef0
mtcr 1
"""])

    
    def tearDown(self):
        # print(cls.instructions)
        self.instructions.append('nop')
        self.instructions.append('li 31, 1')
        self.instructions.append('hang: b hang')

        test_name = self.id().split('.')[-1]
        pathlib.Path(test_name).mkdir(parents=True, exist_ok=True)
        test_file = os.path.join(test_name, 'test.s')
        #Write all instructions to test.s
        with open(test_file, 'w') as asmfile:
            asmfile.write('\n'.join(self.instructions))
            asmfile.write('\n')
            asmfile.write('\n'.join(self.data))
            asmfile.write('\n')


    # def compare_csvs(self, expectedcsv, actualcsv):
    #     with open(expectedcsv, 'r') as exp:
    #         # the chiselwatt one has an extra line that simplepower does not
    #         expected = list(csv.DictReader(exp))[:-1]
    #     with open(actualcsv, 'r') as act:
    #         actual = list(csv.DictReader(act))
            
    #     try:
    #         len_found = len(actual)
    #         len_expected = len(insn_dict)
    #         assert len_found >= len_expected
    #     except AssertionError:
    #         raise Exception("Could not find all expected instructions (found "
    #                         f"{len_found}, expected {len_expected}). Was "
    #                         "SimplePower run with enough ticks?")
    #     rows = min(len(actual), len(expected))
    #     for i in range(rows):
    #         row_e = expected[i]
    #         row_a = actual[i]

    #         if row_e != row_a:
    #             print(f"difference at address 0x{row_e['cia']}: (instruction #{i} - {insn_dict[i]})")
    #             for key in row_e.keys():
    #                 e = row_e[key]
    #                 a = row_a[key]
    #                 if e != a:
    #                     print(f'    {key}: expected 0x{e}, got 0x{a}')
    #                     # print(f'           expected 0b{int(e, 16):064b}, got 0b{int(a, 16):064b}')



    def add_code(self, test_id, instructions, data=[]):
        global cur_insn, insn_list
        test_name = test_id.split('.')[2]
        self.instructions.append(f'{test_name}:')
        for i in instructions:
            self.instructions.append(i.ljust(50) + f"# {cur_insn}")
            insn_dict[cur_insn] = i
            cur_insn += 1
        self.data.extend(data)

    # r1-8 have data preloaded:
    # r1: ffffffffdeadbeef
    # r2: 12345678
    # r3: 1000
    # r4: fffffffffffff000
    # r5: ffffffffffffa5a6
    # r6: 5a5a
    # r7: ffffffffffffffff
    # r8: 123456789abcdef0

    def test_add(self):
        insns = []
        for i in range(20):
            src1 = self.rand.randrange(1,8+1)
            src2 = self.rand.randrange(1,8+1)
            imm = self.rand.randrange(-0x8000, 0x7fff)
            dest = 17
            insns.extend([
                f"addi {dest}, {src1}, {imm}",
                f"addis {dest}, {src1}, {imm}",
                f"addic {dest}, {src1}, {imm}",
                f"addic. {dest}, {src1}, {imm}",
                f"subfic {dest}, {src1}, {imm}",
                f"add {dest}, {src1}, {src2}",
                f"subf {dest}, {src1}, {src2}",
                f"addc {dest}, {src1}, {src2}",
                f"subfc {dest}, {src1}, {src2}",
                f"adde {dest}, {src1}, {src2}",
                f"subfe {dest}, {src1}, {src2}",
                f"addme {dest}, {src1}",
                f"subfme {dest}, {src1}",
                f"subfze {dest}, {src1}",
                f"addze {dest}, {src1}",
                ])
        self.add_code(self.id(), insns)

    def test_rldicl(self):
        insns = []
        for i in range(5):
            dest = 18
            src = self.rand.randrange(1,8+1)
            sh = self.rand.randrange(0, 32)
            me = self.rand.randrange(0, 32)
            insns.append(f"rldicl. {dest}, {src}, {sh}, {me}")
        self.add_code(self.id(), insns)

    def test_rldicr(self):
        insns = []
        for i in range(5):
            dest = 18
            src = self.rand.randrange(1,8+1)
            sh = self.rand.randrange(0, 32)
            me = self.rand.randrange(0, 32)
            insns.append(f"rldicr. {dest}, {src}, {sh}, {me}")
        self.add_code(self.id(), insns)

    def test_rldcr(self):
        insns = []
        for i in range(5):
            dest = 18
            src = self.rand.randrange(1,8+1)
            rb = self.rand.randrange(1, 8+1)
            me = self.rand.randrange(0, 32)
            insns.append(f"rldcr. {dest}, {src}, {rb}, {me}")
        self.add_code(self.id(), insns)

    def test_rldimi(self):
        insns = []
        insns.append('mr 18, 8')
        for i in range(5):
            dest = 18
            src = self.rand.randrange(1,8+1)
            rb = self.rand.randrange(1, 8+1)
            me = self.rand.randrange(0, 32)
            insns.append(f"rldimi {dest}, {src}, {rb}, {me}")
        self.add_code(self.id(), insns)

    def test_rlwimi(self):
        insns = []
        insns.append('mr 18, 8')
        for i in range(5):
            dest = 18
            src = self.rand.randrange(1,8+1)
            sh = self.rand.randrange(0, 32)
            mb = self.rand.randrange(0, 32)
            me = self.rand.randrange(0, 32)
            insns.append(f"rlwimi {dest}, {src}, {sh}, {mb}, {me}")
        self.add_code(self.id(), insns)

    def test_rlwinm(self):
        insns = []
        for i in range(5):
            dest = 18
            src = self.rand.randrange(1,8+1)
            sh = self.rand.randrange(0, 32)
            mb = self.rand.randrange(0, 32)
            me = self.rand.randrange(0, 32)
            insns.append(f"rlwinm. {dest}, {src}, {sh}, {mb}, {me}")
        self.add_code(self.id(), insns)

    def test_xer(self):
        insns = []
        for i in range(20):
            src1 = self.rand.randrange(1,8+1)
            src2 = self.rand.randrange(1,8+1)
            src3 = self.rand.randrange(1,8+1)
            imm = self.rand.randrange(-0x8000, 0x7fff)
            dest = 17
            insns.extend([
                f"addic. {dest}, {src1}, {imm}",
                "mfxer 20",
                f"addeo 18, {src2}, {src3}",
                "mfxer 21",
                ])
        self.add_code(self.id(), insns)

    def test_overflow(self):
        insns = ["lis 17, -0x8000",
                 "sldi 19, 17, 32",
                 "li 0, 0"]

        for insn in ["add", "addc", "adde", "subf", "subfc", "subfe"]:
            insns.extend([
                 f"{insn}o 18, 17, 17",
                 "mfxer 21",
                "mtxer 0"])
            insns.extend([
                 f"{insn}o 18, 19, 19",
                 "mfxer 21",
                "mtxer 0"])
        insns.extend([
            "addo 18, 19, 19",
            "addo 18, 0, 0",
            "mfxer 21"])
        self.add_code(self.id(), insns)


            

    def test_dot(self):
        insns = []
        def generate_data():
            for dot in ['', '.']:
                insns.extend([
                    f'addc{dot} 17, 1, 1',
                    f'adde{dot} 17, 1, 1',
                    f'addic{dot} 17, 1, 1',
                    f'add{dot} 17, 1, 1',
                    f'addze{dot} 17, 2',
                    f'and{dot} 17, 3, 2',
                    f'cntlzd{dot} 17, 2',
                    f'cntlzw{dot} 17, 3',
                    f'extsb{dot} 17, 4',
                    f'extsw{dot} 17, 5',
                    f'neg{dot} 17, 2',
                    f'nor{dot} 17, 1, 3',
                    f'orc{dot} 17, 8, 7',
                    f'or{dot} 17, 6, 5',
                    f'rldcr{dot} 17, 8, 2, 15',
                    f'rldicl{dot} 17, 8, 18, 20',
                    f'rldicr{dot} 17, 8, 18, 20',
                    f'rldimi{dot} 17, 8, 18, 20',
                    f'rlwimi{dot} 17, 8, 14, 28, 20',
                    f'rlwinm{dot} 17, 8, 14, 28, 20',
                    f'sld{dot} 17, 8, 5',
                    f'slw{dot} 17, 8, 5',
                    f'srad{dot} 17, 8, 5',
                    f'sradi{dot} 17, 8, 0x10',
                    f'sraw{dot} 17, 8, 5',
                    f'srawi{dot} 17, 8, 0x10',
                    f'srd{dot} 17, 8, 5',
                    f'srw{dot} 17, 8, 5',
                    f'subfc{dot} 17, 1, 2',
                    f'subfe{dot} 17, 2, 3',
                    f'subf{dot} 17, 1, 2',
                    f'xor{dot} 17, 4, 7'
                ])
        generate_data()
        # set SO bit
        insns.extend(["lis 17, -0x8000",
                      "sldi 17, 17, 32",
                      "addo 19, 17, 17"])
        generate_data()
        self.add_code(self.id(), insns)

    def test_cntlz(self):
        insns = []
        for i in range(16):
            insns.extend([
                f'li 17, {(1<<i)-1}',
                'cntlzw 18, 17',
                'cntlzd 19, 17'
                ])
        self.add_code(self.id(), insns)

    def test_popcnt(self):
        insns = []
        insns.append('li 17, 1')
        for i in range(64):
            insns.extend([
                f'sldi 18, 17, {i}',
                'addi 18, 18, -1',
                'popcntd 19, 18',
                'popcntw 20, 18',
                'popcntb 21, 18'
                ])
        self.add_code(self.id(), insns)


    def test_and(self):
        self.add_code(self.id(), [
            'andi.  17, 7, 0x20',
            'and. 17, 2, 1',
            'andc. 17, 5, 1'
        ])

    
    def test_cmp(self):
        insns = []
        def do_compare():
            insns.extend([
                'li 10, -5',
                'cmpi 0, 1, 10, -6',
                'cmpi 1, 1, 10, -4',
                'cmpi 2, 1, 10, -5',
                'li 10, -5',
                'li 11, -6',
                'cmp 0, 1, 10, 11',
                'li 11, -6',
                'cmp 1, 1, 10, 11',
                'li 11, -6',
                'cmp 2, 1, 10, 11',
                'li 10, -5',
                'cmpli 0, 1, 10, -6',
                'cmpli 1, 1, 10, -4',
                'cmpli 2, 1, 10, -5',
                'li 10, -5',
                'li 11, -6',
                'cmpl 0, 1, 10, 11',
                'li 11, -6',
                'cmpl 1, 1, 10, 11',
                'li 11, -6',
                'cmpl 2, 1, 10, 11',
            ])
        do_compare()
        # set SO bit
        insns.extend(["lis 17, -0x8000",
                      "sldi 17, 17, 32",
                      "addo 19, 17, 17"])
        do_compare()
        self.add_code(self.id(), insns)


    def test_exts(self):
        self.add_code(self.id(), [
            'li 10, 0',
            'extsb. 10, 10',
            'li 10, -1',
            'extsb. 10, 10',
            'li 10, 0',
            'extsw. 10, 10',
            'li 10, -1',
            'extsw. 10, 10'
        ])


    def test_neg(self):
        self.add_code(self.id(), [
            'li 10, 0',
            'neg. 10, 10',
            'li 10, -1',
            'neg. 10, 10'
        ])


    def test_or(self):
        insns = []
        for i in range(-2, 3):
            for j in range(-2, 3):
                insns.extend([
                    f'li 10, {i}',
                    f'li 11, {j}',
                    'nor. 12, 10, 11',
                    'or. 12, 10, 11',
                    'orc. 12, 10, 11',
                    'xor. 12, 10, 11',
                    f'ori 12, 10, {j+2}',
                    f'oris 12, 10, {j+2}',
                    f'xori 12, 10, {j+2}',
                    f'xoris 12, 10, {j+2}'
                ])
        self.add_code(self.id(), insns)

    def test_shift(self):
        insns = []
        for s in range(64):
            insns.extend([
                'li 10, 0b10101010',
                'ori 10, 10, 0b10101010',
                f'li 11, {s}',
                'slw. 12, 10, 11',
                'li 10, -1',
                f'li 11, {s}',
                'srad. 12, 10, 11',
                'mfxer 21',
                'sraw. 12, 10, 11',
                'mfxer 21',
                f'srawi. 12, 10, {s & 31}',
                'mfxer 21',
                f'sradi. 12, 10, {s & 63}',
                'mfxer 21',
            ])
        for s in range(128):
            insns.extend([
                'lis 10, 0b10101010',
                'ori 10, 10, 0b10101010',
                f'li 11, {s}',
                'sld. 12, 10, 11',
            ])
        self.add_code(self.id(), insns)

    def generate_test_load(self, nbytes, load_insn):
        insns = []
        data = []
        data_items = 8
        insns.extend(["lis 17, ld_data@h",
                      "ori 17, 17, ld_data@l"])
        for i in range(data_items):
            insns.append(f"{load_insn} 18, {i*nbytes}(17)")
        data.append("ld_data:")
        directive = data_directives[nbytes]
        for i in range(data_items):
            rand = self.rand.randint(0, 1<<(8*nbytes)-1)
            data.append(f"{directive} 0x{rand:x}")
        self.add_code(self.id(), insns, data)

    def test_load8(self):
        self.generate_test_load(8, "ld")

    def test_load4(self):
        self.generate_test_load(4, "lwz")

    def test_load2(self):
        self.generate_test_load(2, "lhz")

    def test_load1(self):
        self.generate_test_load(1, "lbz")
        

    def generate_test_store(self, nbytes, load_insn, store_insn):
        insns = []
        data = []
        data_items = 8
        insns.extend(["lis 17, ld_data@h",
                      "ori 17, 17, ld_data@l"])
        for i in range(data_items):
            source = self.rand.randint(1, 8)
            insns.append(f"{store_insn} {source}, {i*nbytes}(17)")
        for i in range(data_items):
            insns.append(f"{load_insn} 18, {i*nbytes}(17)")

        data.append("ld_data:")
        directive = data_directives[nbytes]
        for i in range(data_items):
            data.append(f"{directive} 0")
        self.add_code(self.id(), insns, data)

    def test_store1(self):
        self.generate_test_store(1, "lbz", "stb")

    def test_store8(self):
        self.generate_test_store(8, "ld", "std")

    def test_store4(self):
        self.generate_test_store(4, "lwz", "stw")

    def test_store2(self):
        self.generate_test_store(2, "lhz", "sth")

    def generate_test_load_update(self, nbytes, load_insn):
        insns = []
        data = []
        data_items = 8
        insns.extend([f"lis 17, (ld_data-{nbytes})@h",
                      f"ori 17, 17, (ld_data-{nbytes})@l"])
        for i in range(data_items):
            insns.append(f"{load_insn} 18, {nbytes}(17)")
        data.append("ld_data:")
        directive = data_directives[nbytes]
        for i in range(data_items):
            rand = self.rand.randint(0, 1<<(8*nbytes)-1)
            data.append(f"{directive} 0x{rand:x}")
        self.add_code(self.id(), insns, data)

    def test_load1_update(self):
        self.generate_test_load_update(1, "lbzu")
    def test_load2_update(self):
        self.generate_test_load_update(2, "lhzu")
    def test_load4_update(self):
        self.generate_test_load_update(4, "lwzu")
    def test_load8_update(self):
        self.generate_test_load_update(8, "ldu")

    def generate_test_store_update(self, nbytes, load_insn, store_insn):
        insns = []
        data = []
        data_items = 8
        insns.extend([f"lis 17, (ld_data-{nbytes})@h",
                      f"ori 17, 17, (ld_data-{nbytes})@l",
                      "mr 19, 17"])
        for i in range(data_items):
            source = self.rand.randint(1, 8)
            insns.append(f"{store_insn} {source}, {nbytes}(17)")
        for i in range(data_items):
            insns.append(f"{load_insn} 18, {nbytes}(19)")

        data.append("ld_data:")
        directive = data_directives[nbytes]
        for i in range(data_items):
            data.append(f"{directive} 0")
        self.add_code(self.id(), insns, data)

    def test_store1_update(self):
        self.generate_test_store_update(1, "lbzu", "stbu")

    def test_store2_update(self):
        self.generate_test_store_update(2, "lhzu", "sthu")

    def test_store4_update(self):
        self.generate_test_store_update(4, "lwzu", "stwu")

    def test_store8_update(self):
        self.generate_test_store_update(8, "ldu", "stdu")

    def generate_test_load_indexed(self, nbytes, load_insn):
        insns = []
        data = []
        data_items = 8
        insns.extend(["lis 17, ld_data@h",
                      "ori 17, 17, ld_data@l",
                      "li 18, 0"])
        for i in range(data_items):
            insns.append(f"{load_insn} 19, 17, 18")
            insns.append(f"addi 18, 18, {nbytes}")
        data.append("ld_data:")
        directive = data_directives[nbytes]
        for i in range(data_items):
            rand = self.rand.randint(0, 1<<(8*nbytes)-1)
            data.append(f"{directive} 0x{rand:x}")
        self.add_code(self.id(), insns, data)

    def test_load1_indexed(self):
        self.generate_test_load_indexed(1, "lbzx")
    def test_load2_indexed(self):
        self.generate_test_load_indexed(2, "lhzx")
    def test_load4_indexed(self):
        self.generate_test_load_indexed(4, "lwzx")
    def test_load8_indexed(self):
        self.generate_test_load_indexed(8, "ldx")
if __name__ == '__main__':
    unittest.main()
