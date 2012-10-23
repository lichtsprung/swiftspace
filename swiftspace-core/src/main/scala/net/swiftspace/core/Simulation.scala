package net.swiftspace.core

import akka.actor.{Props, ActorSystem, Actor}

/**
 * The Simulation Actor.
 */
class Simulation extends Actor {

  def receive = {
    case Tick() => println("tick")
  }
}

case class Tick()

case class StartClock()

case class StopClock()

/**
 * simulation clock that sends out a simulation tick once a second.
 */
class SimulationClock extends Actor {
  val simulation = context.system.actorFor("user/simulation")
  var running = false
  val clock = new Thread(new Runnable {
    def run() {
      while (running) {
        println("Sending tick to " + simulation)
        simulation ! Tick()
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
