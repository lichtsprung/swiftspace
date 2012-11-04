package net.swiftspace.core.config


import collection.mutable
import net.swiftspace.core.structure.Module.ProcessingModuleDescriptor
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.StructureDescriptor

/**
 * Everything that is needed to configure the simulation.
 */
abstract class Config {


  val processing = mutable.Map[String, ProcessingModuleDescriptor]()
  val resources = mutable.Map[String, Resource]()
  val structures = mutable.Map[String, StructureDescriptor]()

  initResources()
  initProcessingUnits()
  initStartupStructures()

  def initProcessingUnits()

  def initResources()

  def initStartupStructures()
}
