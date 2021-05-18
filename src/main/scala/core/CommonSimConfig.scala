package core

import spinal.core.SpinalConfig
import spinal.core.sim.SimConfig

object CommonSimConfig {
  def apply() = SimConfig.withWave.withConfig(SpinalConfig(targetDirectory = "rtl")).workspacePath("waves")
}
