package net.swiftspace.core.config


import collection.mutable
import net.swiftspace.core.structure.Module.ProcessingModuleDescriptor
import net.swiftspace.core.processing.Resource

/**
 * Everything that is needed to configure the simulation.
 */
abstract class Config{
  val processing = mutable.Buffer[ProcessingModuleDescriptor]()
  val resources = mutable.HashMap[String, Resource]()

  initResources()
  initProcessingUnits()

  def initProcessingUnits()
  def initResources()
}
