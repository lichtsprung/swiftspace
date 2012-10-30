package net.swiftspace.core

import akka.actor.{Props, ActorSystem}
import structure.Structure.NewStructure
import structure.StructureManager
import net.swiftspace.core.Simulation.Coordinate

object Main extends App {
  val system = ActorSystem("swiftspace-core")
  val simulation = system.actorOf(Props[Simulation], "simulation")
  val structure = system.actorOf(Props[StructureManager], "structuremanager")

  structure ! NewStructure(Coordinate(0, 0, 0))

}
