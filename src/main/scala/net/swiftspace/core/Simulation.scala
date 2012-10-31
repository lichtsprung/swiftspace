package net.swiftspace.core

import akka.util.duration._
import akka.actor.{Props, ActorLogging, Actor}
import structure.StructureManager
import akka.util.Duration


object Simulation {

  case object Tick

  case class TickRate(rate: Duration)

  case class Coordinate(x: Double, y: Double, z: Double)

}

import net.swiftspace.core.Simulation._

class Simulation extends Actor with ActorLogging {

  var lastTick = System.currentTimeMillis()
  val structure = context.actorOf(Props[StructureManager], "structuremanager")
  log.info("added actor " + structure)
  var ticker = context.system.scheduler.schedule(1.second, 1.second, self, Tick)



  def receive = {
    case Tick =>
      lastTick = System.currentTimeMillis()
      structure ! Tick
    case TickRate(rate) =>
      ticker.cancel()
      ticker = context.system.scheduler.schedule(rate, rate, self, Tick)
  }

  override def postStop() = {
    ticker.cancel()
  }
}





