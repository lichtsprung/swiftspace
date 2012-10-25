package net.swiftspace.core

import akka.actor.{ActorRef, Props, ActorSystem, Actor}
import collection.mutable
import akka.routing.Broadcast

case class Coordinate(x: Double, y: Double, z: Double)

/**
 * The Simulation Actor.
 */
class Simulation extends Actor {
  val personManager = context.actorOf(Props[PersonManager], "PersonManager")
  val structureManager = context.actorOf(Props[StructureManager], "StructureManager")

  def receive = {
    case Tick() =>
      context.children.foreach(c => c ! Tick())
  }
}

/**
 * Update Signal of the simulation clock.
 */
case class Tick()

case class StartClock()

case class StopClock()

/**
 * simulation clock that sends out a simulation tick once a second.
 */
class SimulationClock extends Actor {
  val simulation = context.actorFor("../simulation")
  var running = false
  var counter = 5
  val clock = new Thread(new Runnable {
    def run() {
      while (running) {
        println("Sending tick to " + simulation)
        simulation ! Tick()
        counter -= 1
        if (counter < 0) {
          running = false
          context.system.shutdown()
        }
        Thread.sleep(1000)
      }
    }
  })


  def receive = {
    case StartClock() =>
      println("starting clock")
      running = true
      clock.start()
    case StopClock() =>
      running = false
      context.system.shutdown()
  }
}


/**
 * Main class that starts the simulation
 */
object Simulation extends App {
  val actorSystem = ActorSystem("simulationSystem")
  val simulation = actorSystem.actorOf(Props[Simulation], "simulation")
  val clock = actorSystem.actorOf(Props[SimulationClock], "clock")

  clock ! StartClock()
}
