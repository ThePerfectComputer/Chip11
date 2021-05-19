//generated 2021-05-19 16:44:02.442152

package isa
import spinal.core._
import spinal.lib._

object Forms {
    def selectBits(instruction: UInt, bita: Int, bitb: Int) =
        Reverse(Reverse(instruction)(bita downto bitb))
    def selectBits(instruction: UInt, bit: Int) =
        Reverse(instruction)(bit)

  object A1 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object A2 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRC(instruction: UInt) = selectBits(instruction, 25, 21)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object A3 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object A4 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def FRC(instruction: UInt) = selectBits(instruction, 25, 21)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object A5 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def BC(instruction: UInt) = selectBits(instruction, 25, 21)
  }
  object B1 {
    def BO(instruction: UInt) = selectBits(instruction, 10, 6)
    def BI(instruction: UInt) = selectBits(instruction, 15, 11)
    def BD(instruction: UInt) = selectBits(instruction, 29, 16).asSInt
    def AA(instruction: UInt) = selectBits(instruction, 30)
    def LK(instruction: UInt) = selectBits(instruction, 31)
  }
  object D1 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def L(instruction: UInt) = selectBits(instruction, 10)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def SI(instruction: UInt) = selectBits(instruction, 31, 16).asSInt
  }
  object D2 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def L(instruction: UInt) = selectBits(instruction, 10)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def UI(instruction: UInt) = selectBits(instruction, 31, 16)
  }
  object D3 {
    def FRS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def D(instruction: UInt) = selectBits(instruction, 31, 16).asSInt
  }
  object D4 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def D(instruction: UInt) = selectBits(instruction, 31, 16).asSInt
  }
  object D5 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def D(instruction: UInt) = selectBits(instruction, 31, 16).asSInt
  }
  object D6 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def UI(instruction: UInt) = selectBits(instruction, 31, 16)
  }
  object D7 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def D(instruction: UInt) = selectBits(instruction, 31, 16).asSInt
  }
  object D8 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def SI(instruction: UInt) = selectBits(instruction, 31, 16).asSInt
  }
  object D9 {
    def TO(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def SI(instruction: UInt) = selectBits(instruction, 31, 16).asSInt
  }
  object DQ1 {
    def RTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DQ(instruction: UInt) = selectBits(instruction, 27, 16).asSInt
    def PT(instruction: UInt) = selectBits(instruction, 31, 28)
  }
  object DQ2 {
    def S(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DQ(instruction: UInt) = selectBits(instruction, 27, 16).asSInt
    def SX(instruction: UInt) = selectBits(instruction, 28)
  }
  object DQ3 {
    def TO(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DQ(instruction: UInt) = selectBits(instruction, 27, 16).asSInt
    def TX(instruction: UInt) = selectBits(instruction, 28)
  }
  object DS1 {
    def FRSp(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DS(instruction: UInt) = selectBits(instruction, 29, 16).asSInt
  }
  object DS2 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DS(instruction: UInt) = selectBits(instruction, 29, 16).asSInt
  }
  object DS3 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DS(instruction: UInt) = selectBits(instruction, 29, 16).asSInt
  }
  object DS4 {
    def RSp(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DS(instruction: UInt) = selectBits(instruction, 29, 16).asSInt
  }
  object DS5 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DS(instruction: UInt) = selectBits(instruction, 29, 16).asSInt
  }
  object DS6 {
    def VRS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DS(instruction: UInt) = selectBits(instruction, 29, 16).asSInt
  }
  object DS7 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DS(instruction: UInt) = selectBits(instruction, 29, 16).asSInt
  }
  object DX1 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def d1(instruction: UInt) = selectBits(instruction, 15, 11)
    def d0(instruction: UInt) = selectBits(instruction, 25, 16)
    def d2(instruction: UInt) = selectBits(instruction, 31)
  }
  object I1 {
    def LI(instruction: UInt) = selectBits(instruction, 29, 6).asSInt
    def AA(instruction: UInt) = selectBits(instruction, 30)
    def LK(instruction: UInt) = selectBits(instruction, 31)
  }
  object M1 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def MB(instruction: UInt) = selectBits(instruction, 25, 21)
    def ME(instruction: UInt) = selectBits(instruction, 30, 26)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object M2 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def SH(instruction: UInt) = selectBits(instruction, 20, 16)
    def MB(instruction: UInt) = selectBits(instruction, 25, 21)
    def ME(instruction: UInt) = selectBits(instruction, 30, 26)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object MD1 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def sh1(instruction: UInt) = selectBits(instruction, 20, 16)
    def mb(instruction: UInt) = selectBits(instruction, 26, 21)
    def sh2(instruction: UInt) = selectBits(instruction, 30)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object MD2 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def sh1(instruction: UInt) = selectBits(instruction, 20, 16)
    def me(instruction: UInt) = selectBits(instruction, 26, 21)
    def sh2(instruction: UInt) = selectBits(instruction, 30)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object MDS1 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def mb(instruction: UInt) = selectBits(instruction, 26, 21)
    def sh(instruction: UInt) = selectBits(instruction, 30)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object MDS2 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def me(instruction: UInt) = selectBits(instruction, 26, 21)
    def sh(instruction: UInt) = selectBits(instruction, 30)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object SC1 {
    def LEV(instruction: UInt) = selectBits(instruction, 26, 20)
  }
  object VA1 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def RC(instruction: UInt) = selectBits(instruction, 25, 21)
  }
  object VA2 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def VRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def SHB(instruction: UInt) = selectBits(instruction, 25, 22)
  }
  object VA3 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def VRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def VRC(instruction: UInt) = selectBits(instruction, 25, 21)
  }
  object VC1 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def VRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 21)
  }
  object VX1 {
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object VX2 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object VX3 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
  }
  object VX4 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object VX5 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def UIM(instruction: UInt) = selectBits(instruction, 15, 14)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object VX6 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def UIM(instruction: UInt) = selectBits(instruction, 15, 13)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object VX7 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def UIM(instruction: UInt) = selectBits(instruction, 15, 12)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object VX8 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object VX9 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def PS(instruction: UInt) = selectBits(instruction, 22)
  }
  object VX10 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object VX11 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object VX12 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def SIM(instruction: UInt) = selectBits(instruction, 15, 11).asSInt
  }
  object VX13 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def UIM(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object VX14 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def VRA(instruction: UInt) = selectBits(instruction, 15, 11)
  }
  object VX15 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def VRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object VX16 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def VRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def PS(instruction: UInt) = selectBits(instruction, 22)
  }
  object VX17 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def VRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X1 {
  }
  object X2 {
  }
  object X3 {
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X4 {
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
  }
  object X5 {
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
  }
  object X6 {
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X7 {
    def L(instruction: UInt) = selectBits(instruction, 10)
  }
  object X8 {
    def L(instruction: UInt) = selectBits(instruction, 10)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X9 {
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X10 {
    def L(instruction: UInt) = selectBits(instruction, 10)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X11 {
    def L(instruction: UInt) = selectBits(instruction, 10, 9)
  }
  object X12 {
    def L(instruction: UInt) = selectBits(instruction, 10, 9)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X13 {
    def WC(instruction: UInt) = selectBits(instruction, 10, 9)
  }
  object X14 {
    def IH(instruction: UInt) = selectBits(instruction, 10, 8)
  }
  object X15 {
    def CT(instruction: UInt) = selectBits(instruction, 10, 7)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X16 {
    def A(instruction: UInt) = selectBits(instruction, 6)
  }
  object X17 {
    def A(instruction: UInt) = selectBits(instruction, 6)
    def R(instruction: UInt) = selectBits(instruction, 10)
  }
  object X18 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
  }
  object X19 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X20 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def W(instruction: UInt) = selectBits(instruction, 15)
    def U(instruction: UInt) = selectBits(instruction, 19, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X21 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def BFA(instruction: UInt) = selectBits(instruction, 13, 11)
  }
  object X22 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def FRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X23 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def FRAp(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X24 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def FRAp(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X25 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X26 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def UIM(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X27 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def UIM(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X28 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def VRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X29 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X30 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def L(instruction: UInt) = selectBits(instruction, 10)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X31 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def DCMX(instruction: UInt) = selectBits(instruction, 15, 9)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X32 {
    def BT(instruction: UInt) = selectBits(instruction, 10, 6)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X33 {
    def FRS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X34 {
    def FRSp(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X35 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X36 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X37 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X38 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X39 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
  }
  object X40 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def RM(instruction: UInt) = selectBits(instruction, 20, 19)
  }
  object X41 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def DRM(instruction: UInt) = selectBits(instruction, 20, 18)
  }
  object X42 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X43 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X44 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X45 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X46 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def S(instruction: UInt) = selectBits(instruction, 11)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X47 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def SP(instruction: UInt) = selectBits(instruction, 12, 11)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X48 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X49 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X50 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X51 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRAp(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X52 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X53 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def S(instruction: UInt) = selectBits(instruction, 11)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X54 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def SP(instruction: UInt) = selectBits(instruction, 12, 11)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X55 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X56 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def L(instruction: UInt) = selectBits(instruction, 15)
  }
  object X57 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RIC(instruction: UInt) = selectBits(instruction, 13, 12)
    def PR(instruction: UInt) = selectBits(instruction, 14)
    def R(instruction: UInt) = selectBits(instruction, 15)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X58 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def SR(instruction: UInt) = selectBits(instruction, 15, 12)
  }
  object X59 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def BFA(instruction: UInt) = selectBits(instruction, 13, 11)
  }
  object X60 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
  }
  object X61 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
  }
  object X62 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X63 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def FC(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X64 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def NB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X65 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def SH(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X66 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X67 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X68 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object X69 {
    def RSp(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X70 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
  }
  object X71 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X72 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X73 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def L(instruction: UInt) = selectBits(instruction, 15, 14)
  }
  object X74 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def SR(instruction: UInt) = selectBits(instruction, 15, 12)
  }
  object X75 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def FC(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X76 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def NB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X77 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X78 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def EH(instruction: UInt) = selectBits(instruction, 31)
  }
  object X79 {
    def RTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def EH(instruction: UInt) = selectBits(instruction, 31)
  }
  object X80 {
    def S(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def SX(instruction: UInt) = selectBits(instruction, 31)
  }
  object X81 {
    def S(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def SX(instruction: UInt) = selectBits(instruction, 31)
  }
  object X82 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 12, 11)
    def IMM8(instruction: UInt) = selectBits(instruction, 20, 13)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object X83 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object X84 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object X85 {
    def TH(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X86 {
    def TO(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def SI(instruction: UInt) = selectBits(instruction, 20, 16).asSInt
  }
  object X87 {
    def TO(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X88 {
    def TO(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X89 {
    def VRS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X90 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X91 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def RO(instruction: UInt) = selectBits(instruction, 31)
  }
  object X92 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X93 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def VRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object X94 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def VRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def RO(instruction: UInt) = selectBits(instruction, 31)
  }
  object XFL1 {
    def L(instruction: UInt) = selectBits(instruction, 6)
    def FLM(instruction: UInt) = selectBits(instruction, 14, 7)
    def W(instruction: UInt) = selectBits(instruction, 15)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object XFX1 {
  }
  object XFX2 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def FXM(instruction: UInt) = selectBits(instruction, 19, 12)
  }
  object XFX3 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def FXM(instruction: UInt) = selectBits(instruction, 19, 12)
  }
  object XFX4 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def spr(instruction: UInt) = selectBits(instruction, 20, 11)
  }
  object XFX5 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
  }
  object XFX6 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def FXM(instruction: UInt) = selectBits(instruction, 19, 12)
  }
  object XFX7 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def BHRBE(instruction: UInt) = selectBits(instruction, 20, 11)
  }
  object XFX8 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def spr(instruction: UInt) = selectBits(instruction, 20, 11)
  }
  object XFX9 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def tbr(instruction: UInt) = selectBits(instruction, 20, 11)
  }
  object XL1 {
  }
  object XL2 {
    def S(instruction: UInt) = selectBits(instruction, 20)
  }
  object XL3 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def BFA(instruction: UInt) = selectBits(instruction, 13, 11)
  }
  object XL4 {
    def BO(instruction: UInt) = selectBits(instruction, 10, 6)
    def BI(instruction: UInt) = selectBits(instruction, 15, 11)
    def BH(instruction: UInt) = selectBits(instruction, 20, 19)
    def LK(instruction: UInt) = selectBits(instruction, 31)
  }
  object XL5 {
    def BT(instruction: UInt) = selectBits(instruction, 10, 6)
    def BA(instruction: UInt) = selectBits(instruction, 15, 11)
    def BB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object XO1 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def OE(instruction: UInt) = selectBits(instruction, 21)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object XO2 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
  }
  object XO3 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object XO4 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def RB(instruction: UInt) = selectBits(instruction, 20, 16)
    def OE(instruction: UInt) = selectBits(instruction, 21)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object XS1 {
    def RS(instruction: UInt) = selectBits(instruction, 10, 6)
    def RA(instruction: UInt) = selectBits(instruction, 15, 11)
    def sh1(instruction: UInt) = selectBits(instruction, 20, 16)
    def sh2(instruction: UInt) = selectBits(instruction, 30)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object XX2_1 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def BX(instruction: UInt) = selectBits(instruction, 30)
  }
  object XX2_2 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def DCMX(instruction: UInt) = selectBits(instruction, 15, 9)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def BX(instruction: UInt) = selectBits(instruction, 30)
  }
  object XX2_3 {
    def RT(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def BX(instruction: UInt) = selectBits(instruction, 30)
  }
  object XX2_4 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def BX(instruction: UInt) = selectBits(instruction, 30)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object XX2_5 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def UIM(instruction: UInt) = selectBits(instruction, 15, 14)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def BX(instruction: UInt) = selectBits(instruction, 30)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object XX2_6 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def UIM(instruction: UInt) = selectBits(instruction, 15, 12)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def BX(instruction: UInt) = selectBits(instruction, 30)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object XX2_7 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def dx(instruction: UInt) = selectBits(instruction, 15, 11)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def dc(instruction: UInt) = selectBits(instruction, 25)
    def dm(instruction: UInt) = selectBits(instruction, 29)
    def BX(instruction: UInt) = selectBits(instruction, 30)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object XX2_8 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def EO(instruction: UInt) = selectBits(instruction, 15, 11)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def BX(instruction: UInt) = selectBits(instruction, 30)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object XX3_1 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def A(instruction: UInt) = selectBits(instruction, 15, 11)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def AX(instruction: UInt) = selectBits(instruction, 29)
    def BX(instruction: UInt) = selectBits(instruction, 30)
  }
  object XX3_2 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def A(instruction: UInt) = selectBits(instruction, 15, 11)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def DM(instruction: UInt) = selectBits(instruction, 23, 22)
    def AX(instruction: UInt) = selectBits(instruction, 29)
    def BX(instruction: UInt) = selectBits(instruction, 30)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object XX3_3 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def A(instruction: UInt) = selectBits(instruction, 15, 11)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def SHW(instruction: UInt) = selectBits(instruction, 23, 22)
    def AX(instruction: UInt) = selectBits(instruction, 29)
    def BX(instruction: UInt) = selectBits(instruction, 30)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object XX3_4 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def A(instruction: UInt) = selectBits(instruction, 15, 11)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 21)
    def AX(instruction: UInt) = selectBits(instruction, 29)
    def BX(instruction: UInt) = selectBits(instruction, 30)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object XX3_5 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def A(instruction: UInt) = selectBits(instruction, 15, 11)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def AX(instruction: UInt) = selectBits(instruction, 29)
    def BX(instruction: UInt) = selectBits(instruction, 30)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object XX4_1 {
    def T(instruction: UInt) = selectBits(instruction, 10, 6)
    def A(instruction: UInt) = selectBits(instruction, 15, 11)
    def B(instruction: UInt) = selectBits(instruction, 20, 16)
    def C(instruction: UInt) = selectBits(instruction, 25, 21)
    def CX(instruction: UInt) = selectBits(instruction, 28)
    def AX(instruction: UInt) = selectBits(instruction, 29)
    def BX(instruction: UInt) = selectBits(instruction, 30)
    def TX(instruction: UInt) = selectBits(instruction, 31)
  }
  object Z22_1 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def FRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DCM(instruction: UInt) = selectBits(instruction, 21, 16)
  }
  object Z22_2 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def FRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def DGM(instruction: UInt) = selectBits(instruction, 21, 16)
  }
  object Z22_3 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def FRAp(instruction: UInt) = selectBits(instruction, 15, 11)
    def DCM(instruction: UInt) = selectBits(instruction, 21, 16)
  }
  object Z22_4 {
    def BF(instruction: UInt) = selectBits(instruction, 8, 6)
    def FRAp(instruction: UInt) = selectBits(instruction, 15, 11)
    def DGM(instruction: UInt) = selectBits(instruction, 21, 16)
  }
  object Z22_5 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def SH(instruction: UInt) = selectBits(instruction, 21, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object Z22_6 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRAp(instruction: UInt) = selectBits(instruction, 15, 11)
    def SH(instruction: UInt) = selectBits(instruction, 21, 16)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object Z23_1 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def R(instruction: UInt) = selectBits(instruction, 15)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def RMC(instruction: UInt) = selectBits(instruction, 21)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object Z23_2 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRA(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def RMC(instruction: UInt) = selectBits(instruction, 21)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object Z23_3 {
    def FRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def TE(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def RMC(instruction: UInt) = selectBits(instruction, 21)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object Z23_4 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def R(instruction: UInt) = selectBits(instruction, 15)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
    def RMC(instruction: UInt) = selectBits(instruction, 21)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object Z23_5 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRAp(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
    def RMC(instruction: UInt) = selectBits(instruction, 21)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object Z23_6 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def FRAp(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
    def RMC(instruction: UInt) = selectBits(instruction, 21)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object Z23_7 {
    def FRTp(instruction: UInt) = selectBits(instruction, 10, 6)
    def TE(instruction: UInt) = selectBits(instruction, 15, 11)
    def FRBp(instruction: UInt) = selectBits(instruction, 20, 16)
    def RMC(instruction: UInt) = selectBits(instruction, 21)
    def Rc(instruction: UInt) = selectBits(instruction, 31)
  }
  object Z23_8 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def R(instruction: UInt) = selectBits(instruction, 15)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def RMC(instruction: UInt) = selectBits(instruction, 21)
  }
  object Z23_9 {
    def VRT(instruction: UInt) = selectBits(instruction, 10, 6)
    def R(instruction: UInt) = selectBits(instruction, 15)
    def VRB(instruction: UInt) = selectBits(instruction, 20, 16)
    def RMC(instruction: UInt) = selectBits(instruction, 21)
    def EX(instruction: UInt) = selectBits(instruction, 31)
  }
}