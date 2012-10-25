package net.swiftspace.core.structure

import akka.actor.{Props, Actor}
import collection.mutable
import akka.event.Logging


/**
 * A module is part of a structure in space. It can be installed and fulfill any kind of service.
 * @param requirements the requirements of this module
 * @param stats the stats that define this module
 * @param input the resource that is being processed by this module unit
 * @param output the product that is being produced by this module unit
 * @param structure the parent structure it belongs to
 */
class StructureModule(
                       val requirements: ModuleRequirements,
                       val stats: ModuleStats,
                       val input: ModuleInput,
                       val output: ModuleOutput,
                       val structure: Structure) {
  def update() = {

  }
}

/**
 * Defines the stats that define the Module.
 * @param processingRate the rate in which incoming resources can be processed
 */
case class ModuleStats(val processingRate: Double)

/**
 * The requirements a Structure has to meet to be able to install the module
 * @param energy
 */
case class ModuleRequirements(energy: Double)

/**
 * What is processed by the module?
 * @param resource the name of the resource
 * @param maxCapacity the maximum capacity this module is able to hold
 * @param currentCapacity the current capacity of the resource the module is holding
 *
 */
case class ModuleInput(val resource: Resource, val maxCapacity: Double, var currentCapacity: Double)

/**
 * What is produced by the module?
 * @param resource the name of the resource
 * @param maxCapacity the maximum capacity this module is able to hold
 * @param currentCapacity the current capacity of the resource the module is holding
 */
case class ModuleOutput(val resource: Resource, val maxCapacity: Double, var currentCapacity: Double)


/**
 * A structure is something that can be build, used or inhabited by the persons inside the simulation.
 * @param name the name of the structure
 * @param coordinate the coordinate in space
 */
class Structure(val name: String, val coordinate: Coordinate, val moduleSlotCount: Int) extends Actor {
  val log = Logging(context.system, this)


  var energyAvailable = 0.0
  var energyUsed = 0.0

  /**
   * Temperature in Celsius
   */
  var temperature = 18.3
  /**
   * Pressure in bar
   */
  var pressure = 1.0

  val modules = mutable.Buffer[StructureModule]()
  val availableResources = mutable.HashMap[Resource, Double]()
  val neighbours = mutable.HashMap[Direction.type, Structure]()

  def receive = {
    case InstallModule(module) =>
      if (energyAvailable >= module.requirements.energy && modules.size < moduleSlotCount) {
        modules += module
        energyAvailable -= module.requirements.energy
        energyUsed += module.requirements.energy
      } else {
        log.error("module could not be installed")
      }
    case Tick(now) =>
      modules.foreach(m => m.update())
  }
}


class StructureManager extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case Tick(now) =>
      log.info("updating structures...")
      context.children.foreach(child => child ! Tick(now))
    case CreateStructure(name, coordinate, moduleCount) =>
      context.actorOf(Props(new Structure(name, coordinate, moduleCount)))
      log.info("created new structure at " + coordinate + " with name " + name)

  }
}
