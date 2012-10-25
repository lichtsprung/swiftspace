package net.swiftspace.core.person

import akka.actor.{Props, Actor}
import akka.event.Logging
import net.swiftspace.core.{CreatePerson, Tick}

/**
 * An autonomous agent in the simulation.
 */
class Person(val name: String, var age: Int) extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case Tick(now) =>
      log.info("Updating person " + name)
  }
}




class PersonManager extends Actor {
  def receive = {
    case Tick(now) =>
      context.children.foreach(child => child ! Tick(now))
    case CreatePerson(name, age) =>
      context.actorOf(Props(new Person(name, age)))
  }
}
