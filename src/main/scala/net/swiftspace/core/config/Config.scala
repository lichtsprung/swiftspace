package net.swiftspace.core.config


import collection.mutable
import net.swiftspace.core.structure.Module.ProcessingModuleDescriptor
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.StructureDescriptor

/**
 * Everything that is needed to configure the simulation.
 */
abstract class Config {


  // All processing modules known to the simulation.
  val processing = mutable.Map[String, ProcessingModuleDescriptor]()

  // All resources in the simulation.
  val resources = mutable.Map[String, Resource]()

  // All pre-defined structures in the simulation.
  val structures = mutable.Map[String, StructureDescriptor]()

  initResources()
  initProcessingUnits()
  initStartupStructures()

  /**
   * This function defines and initialises the map with the processing modules.
   */
  def initProcessingUnits()

  /**
   * In this method all resources should be described and added to the map.
   */
  def initResources()

  /**
   * The structures that will be available at startup.
   */
  def initStartupStructures()
}
