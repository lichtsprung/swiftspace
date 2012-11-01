package net.swiftspace.core.config


import collection.mutable
import net.swiftspace.core.structure.Module.ProcessingModuleDescriptor

/**
 * Everything that is needed to configure the simulation.
 */
abstract class Config{
  val processing = mutable.Buffer[ProcessingModuleDescriptor]()
}
