package net.swiftspace.core

import akka.actor.{Props, Actor}

/**
 * An autonomous agent in the simulation.
 */
class Person(val name: String, var age: Int) extends Actor {

  def receive = {
    case Tick() =>
      println("ticking the person")
  }
}


case class CreatePerson()

class PersonManager extends Actor {
  def receive = {
    case Tick() =>
      context.children.foreach(child => child ! Tick())
    case CreatePerson() =>
      context.actorOf(Props(new Person("unnamed", 20)))
  }
}
