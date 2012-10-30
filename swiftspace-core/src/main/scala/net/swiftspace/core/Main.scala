package net.swiftspace.core

import akka.actor.{Props, ActorSystem}
import structure.Structure.NewStructure
import akka.util.duration._
import net.swiftspace.core.Simulation.{TickRate, Coordinate}

object Main extends App {
  val system = ActorSystem("swiftspace-core")
  val simulation = system.actorOf(Props[Simulation], "simulation")
  val structure = system.actorFor("/user/simulation/structuremanager")

  simulation ! TickRate(100.milliseconds)
  structure ! NewStructure(Coordinate(0, 0, 0))

}
