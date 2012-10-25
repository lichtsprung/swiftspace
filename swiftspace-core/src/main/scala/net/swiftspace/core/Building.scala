package net.swiftspace.core

import akka.actor.Actor

/**
 * Every larger structure that can be inhabitable.
 */
class Building(override val name: String, override val coordinate: Coordinate) extends Structure(name, coordinate) {
  def receive = {
    case Tick() =>
    // Update modules
  }
}

/**
 * Is part of a structure and can fulfil a service or function.
 */
class StructureModule extends Actor {
  def receive = {
    case Tick =>
      // Update module
  }
}

abstract class Structure(val name: String, val coordinate: Coordinate) extends Actor{
  var energyAvailable = 0.0
  var energyUsed = 0.0
  var energyNeeded = 0.0
}

import StructureType._

case class GetNearestStructure(structureType: StructureType)

class StructureManager extends Actor {
  def receive = {
    case Tick() =>
    // Update Structures

  }
}


object StructureType extends Enumeration {
  type StructureType = Value
  val STATION = Value
}