package net.swiftspace.core

import akka.util.duration._
import akka.actor.{Props, ActorLogging, Actor}
import akka.util.Duration
import structure.StructureManager


object Simulation {

  case object Tick

  case class TickRate(rate: Duration)

  case class Coordinate(x: Double, y: Double, z: Double)

}

import net.swiftspace.core.Simulation._

class Simulation extends Actor with ActorLogging {
  var ticker = context.system.scheduler.schedule(1.second, 1.second, self, Tick)
  var lastTick = System.currentTimeMillis()
  val structure = context.actorFor("/user/structuremanager")
  log.info("added actor " + structure)

  def receive = {
    case Tick =>
      lastTick = System.currentTimeMillis()
      context.children.foreach(c => c ! Tick)
      structure ! Tick
    case TickRate(rate) =>
      ticker.cancel()
      ticker = context.system.scheduler.schedule(rate, rate, self, Tick)

  }

  override def postStop() = {
    ticker.cancel()
  }
}





