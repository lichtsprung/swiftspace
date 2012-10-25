package net.swiftspace.core

import akka.actor.Actor
import collection.mutable
import akka.event.Logging


/**
 * A module is part of a structure in space. It can be installed and fulfill any kind of service.
 *
 * @param requirements the requirements of this module
 * @param input the resource that is being processed by this module unit
 * @param output the product that is being produced by this module unit
 */
class StructureModule(val requirements: ModuleRequirements, val input: ModuleInput, val output: ModuleOutput) {
  def update() = {

  }
}

/**
 * The requirements a Structure has to meet to be able to install the module
 * @param energy
 */
case class ModuleRequirements(energy: Double)

/**
 * What is processed by the module?
 * @param resource the name of the resource
 */
case class ModuleInput(resource: String)

/**
 * What is produced by the module?
 * @param resource the name of the resource
 */
case class ModuleOutput(resource: String)

/**
 * Installs a new module to the structure.
 * @param module the module being installed
 */
case class InstallModule(module: StructureModule)

/**
 * A structure is something that can be build, used or inhabited by the persons inside the simulation.
 * @param name the name of the structure
 * @param coordinate the coordinate in space
 */
class Structure(val name: String, val coordinate: Coordinate) extends Actor {
  val log = Logging(context.system, this)
  var energyAvailable = 0.0
  var energyUsed = 0.0

  var modules = mutable.Buffer[StructureModule]()

  def receive = {
    case InstallModule(module) =>
      if (energyAvailable >= module.requirements.energy) {
        modules += module
        energyAvailable -= module.requirements.energy
        energyUsed += module.requirements.energy
      } else {
        log.error("Unsufficient energy suplly to install new module")
      }
    case Tick() =>
      modules.foreach(m => m.update())
  }
}

case class CreateStructure()

class StructureManager extends Actor {
  def receive = {
    case Tick() =>
    // Update Structures
    case CreateStructure() =>


  }
}
