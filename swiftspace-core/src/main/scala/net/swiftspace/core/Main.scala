package net.swiftspace.core

import akka.actor.{Props, ActorSystem}

/**
 * Starting the Simulation...yay!
 */
class Main extends App {
  val actorSystem = ActorSystem("simulationSystem")
  val simulation = actorSystem.actorOf(Props[Simulation], "simulation")
  val clock = actorSystem.actorOf(Props[SimulationClock], "clock")

  clock ! StartClock(1000)
}
