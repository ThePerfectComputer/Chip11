//generated 2021-05-31 15:21:58.108904

package isa
import spinal.core._
import spinal.lib._

object ReadSlotPacking {
    // Slot 1
    val VRPort1 = 0
    val FPRPort1 = 0
    val VSRPort1 = 0
    val COMBINEDPort1 = 0
    val GPRPort1 = 0

    // Slot 2
    val VRPort2 = 1
    val FPRPort2 = 1
    val VSRPort2 = 1
    val COMBINEDPort2 = 1
    val CRAPort1 = 1
    val GPRPort2 = 1

    // Slot 3
    val VRPort3 = 2
    val VSRPort3 = 2
    val FPRPort3 = 2
    val CRBPort1 = 2
    val GPRPort3 = 2

    // Slot 4
    val SPRPort1 = 3
    val FPRPort4 = 3
    val FPSCRPort1 = 3
    val GPRPort4 = 3

    // Slot 5
    val XERPort1 = 4
    val BHRBPort1 = 4
    val SPRPort2 = 4
    val COMBINEDPort3 = 4
    val FPSCRPort2 = 4

    val VRSlots = Set(0,1,2)
    val FPRSlots = Set(0,1,2,3)
    val VSRSlots = Set(0,1,2)
    val COMBINEDSlots = Set(0,1,4)
    val GPRSlots = Set(0,1,2,3)
    val CRASlots = Set(1)
    val CRBSlots = Set(2)
    val SPRSlots = Set(3,4)
    val FPSCRSlots = Set(3,4)
    val XERSlots = Set(4)
    val BHRBSlots = Set(4)

    var slotsMap = Map[SourceSelect.E, Set[Int]]()
    slotsMap += (SourceSelect.VR -> VRSlots)
    slotsMap += (SourceSelect.FPR -> FPRSlots)
    slotsMap += (SourceSelect.VSR -> VSRSlots)
    slotsMap += (SourceSelect.COMBINED -> COMBINEDSlots)
    slotsMap += (SourceSelect.GPR -> GPRSlots)
    slotsMap += (SourceSelect.CRA -> CRASlots)
    slotsMap += (SourceSelect.CRB -> CRBSlots)
    slotsMap += (SourceSelect.SPR -> SPRSlots)
    slotsMap += (SourceSelect.FPSCR -> FPSCRSlots)
    slotsMap += (SourceSelect.XER -> XERSlots)
    slotsMap += (SourceSelect.BHRB -> BHRBSlots)
    def apply(sel: SourceSelect.E) = slotsMap(sel)
}

object WriteSlotPacking {
    // Slot 1
    val VRPort1 = 0
    val FPRPort1 = 0
    val VSRPort1 = 0
    val COMBINEDPort1 = 0
    val GPRPort1 = 0

    // Slot 2
    val VSRPort2 = 1
    val VRPort2 = 1
    val FPRPort2 = 1
    val GPRPort2 = 1

    // Slot 3
    val CRAPort1 = 2

    // Slot 4
    val SPRPort1 = 3
    val FPSCRPort1 = 3
    val CRBPort1 = 3

    // Slot 5
    val XERPort1 = 4
    val BHRBPort1 = 4
    val SPRPort2 = 4
    val COMBINEDPort2 = 4
    val FPSCRPort2 = 4

    val VRSlots = Set(0,1)
    val FPRSlots = Set(0,1)
    val VSRSlots = Set(0,1)
    val COMBINEDSlots = Set(0,4)
    val GPRSlots = Set(0,1)
    val CRASlots = Set(2)
    val SPRSlots = Set(3,4)
    val FPSCRSlots = Set(3,4)
    val CRBSlots = Set(3)
    val XERSlots = Set(4)
    val BHRBSlots = Set(4)

    var slotsMap = Map[SourceSelect.E, Set[Int]]()
    slotsMap += (SourceSelect.VR -> VRSlots)
    slotsMap += (SourceSelect.FPR -> FPRSlots)
    slotsMap += (SourceSelect.VSR -> VSRSlots)
    slotsMap += (SourceSelect.COMBINED -> COMBINEDSlots)
    slotsMap += (SourceSelect.GPR -> GPRSlots)
    slotsMap += (SourceSelect.CRA -> CRASlots)
    slotsMap += (SourceSelect.SPR -> SPRSlots)
    slotsMap += (SourceSelect.FPSCR -> FPSCRSlots)
    slotsMap += (SourceSelect.CRB -> CRBSlots)
    slotsMap += (SourceSelect.XER -> XERSlots)
    slotsMap += (SourceSelect.BHRB -> BHRBSlots)
    def apply(sel: SourceSelect.E) = slotsMap(sel)
}
