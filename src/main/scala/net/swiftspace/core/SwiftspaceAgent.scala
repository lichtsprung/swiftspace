package net.swiftspace.core

import akka.actor.{ActorLogging, Actor}
import scala.collection.mutable

/**
 * An agent is an active entity in the simulation.
 * Accepted Messages:
 * - SwiftspaceAgent.AddEffect
 * - SwiftspaceAgent.RemoveEffect
 */
class SwiftspaceAgent(val attributes: List[Attribute]) extends Actor with ActorLogging {
  val activeEffects = mutable.Buffer[Effect]()
  val attributesState = mutable.Map[Attribute, Double]()

  def receive = {
    case SwiftspaceAgent.AddEffect(effect) => ???
    case SwiftspaceAgent.RemoveEffect(effect) => ???
    case _ => log.info("Huh?")
  }
}

object SwiftspaceAgent {

  case class AddEffect(effect: Effect)

  case class RemoveEffect(effect: Effect)

}

case class Attribute(name: String)

case class Effect(name: String, effectFunction: (SwiftspaceAgent) => Unit)

object Effect {

  case class LowerAttribute(attribute: Attribute)

}