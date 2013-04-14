package net.swiftspace.core.config


import collection.mutable
import net.swiftspace.core.structure.Module.ProcessingModuleDescriptor
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.StructureDescriptor
import net.swiftspace.core.{Effect, Attribute}

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

  // All attributes that are used in the simulation.
  val attributes = mutable.Buffer[Attribute]()

  val effects = mutable.Map[String, Effect]()

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
   * Add new attributes here.
   * Every agent that is spawned will have this set of attributes.
   */
  def initAttributes()

  /**
   * The structures that will be available at startup.
   */
  def initStartupStructures()
}
