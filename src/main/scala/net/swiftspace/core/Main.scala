package net.swiftspace.core

import akka.actor.{Props, ActorSystem}
import net.swiftspace.core.Simulation.Coordinate
import structure.StructureManager.StructureInVicinity


object Main extends App {
  val system = ActorSystem("swiftspace-core")
  val simulation = system.actorOf(Props[Simulation], "simulation")
}
