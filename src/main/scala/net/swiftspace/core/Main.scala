package net.swiftspace.core

import akka.actor.{Props, ActorSystem}
import akka.util.duration._
import net.swiftspace.core.Simulation.TickRate

object Main extends App {
  val system = ActorSystem("swiftspace-core")
  val simulation = system.actorOf(Props[Simulation], "simulation")
  val structure = system.actorFor("/user/simulation/structuremanager")

  simulation ! TickRate(100.milliseconds)

}
