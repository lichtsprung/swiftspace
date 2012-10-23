package net.swiftspace.core

import akka.actor.{ActorRef, Props, ActorSystem, Actor}
import collection.mutable

case class RegisterPerson(person: Person)

/**
 * The Simulation Actor.
 */
class Simulation extends Actor {
  val persons = mutable.Buffer[ActorRef]()

  def receive = {
    case Tick() =>
      persons.foreach(p => p ! Tick())
    case RegisterPerson(person) =>
      persons += person.self

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
