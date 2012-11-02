package net.swiftspace.core

import akka.util.duration._
import akka.actor.{Props, ActorLogging, Actor}
import config.Config
import structure.StructureManager
import akka.util.FiniteDuration
import com.twitter.util.Eval
import java.io.File


object Simulation {
  val url = getClass.getClassLoader.getResource("Configuration.scala").getFile
  val configuration = Eval[Config](new File(url))
  var tickRate = 0.1 seconds

  case object Tick

  case class TickRate(rate: FiniteDuration)

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
      Simulation.tickRate = rate
      ticker = context.system.scheduler.schedule(rate, rate, self, Tick)
  }

  override def postStop() = {
    ticker.cancel()
  }
}





