package net.swiftspace.core

import akka.actor.{ActorLogging, Actor}

/**
 * An agent is an active entity in the simulation.
 * Accepted Messages:
 * - SwiftspaceAgent.AddAttribute
 * - SwiftspaceAgent.AddEffect
 * - SwiftspaceAgent.RemoveAttribute
 * - SwiftspaceAgent.RemoveEffect
 */
class SwiftspaceAgent extends Actor with ActorLogging {

  def receive = {
    case SwiftspaceAgent.AddAttribute => ???
    case SwiftspaceAgent.AddEffect => ???
    case SwiftspaceAgent.RemoveAttribute => ???
    case SwiftspaceAgent.RemoveEffect => ???
    case _ => log.info("Huh?")
  }
}

object SwiftspaceAgent {

  case class AddAttribute(attribute: Attribute)

  case class RemoveAttribute(attribute: Attribute)

  case class AddEffect(effect: Effect)

  case class RemoveEffect(effect: Effect)

}

case class Attribute(name: String)

case class Effect(name: String)