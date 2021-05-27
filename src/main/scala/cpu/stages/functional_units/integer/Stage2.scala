package cpu.stages.functional_units.integer

import cpu.interfaces.{FunctionalUnit, FunctionalUnitExit}
import util.{PipeStage}

import spinal.core._

class Stage2 extends PipeStage(new ExecuteData, new ExecuteData) {
  o := i
}
