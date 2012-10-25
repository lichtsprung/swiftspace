package net.swiftspace.core

import akka.actor.Actor
import collection.mutable
import akka.event.Logging


/**
 * Is part of a structure and can fulfil a service or function.
 */
class StructureModule(val requirements: ModuleRequirements) {
  def update() = {

  }
}

case class ModuleRequirements(energy: Double)

case class RegisterModule(module: StructureModule)

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
    case RegisterModule(module) =>
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
