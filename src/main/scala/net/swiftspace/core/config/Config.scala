package net.swiftspace.core.config


import collection.mutable
import net.swiftspace.core.structure.Module.ProcessingModuleDescriptor
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.StructureDescriptor

/**
 * Everything that is needed to configure the simulation.
 */
abstract class Config {


  val processing = mutable.HashMap[String, ProcessingModuleDescriptor]()
  val resources = mutable.HashMap[String, Resource]()
  val structures = mutable.HashMap[String, StructureDescriptor]()

  initResources()
  initProcessingUnits()

  def initProcessingUnits()

  def initResources()

  def initStartupStructures()
}
