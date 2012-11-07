package net.swiftspace.core

import akka.actor.{Props, ActorLogging, Actor}
import akka.pattern.ask
import com.twitter.util.Eval
import config.Config
import java.io.File
import net.swiftspace.core.Simulation._
import structure.Structure.NewStructure
import structure.StructureManager
import structure.StructureManager.StructureInVicinty
import akka.util.Timeout
import concurrent.duration._
import concurrent.ExecutionContext.Implicits.global


object Simulation {
  val url = getClass.getClassLoader.getResource("Configuration.scala").getFile
  val configuration = Eval[Config](new File(url))

  var tickRate = 1.second

  case object Tick

  case class TickRate(rate: FiniteDuration)

  case class Coordinate(x: Double, y: Double, z: Double) {
    def distance(c: Coordinate): Double = math.sqrt(math.pow(x - c.x, 2) + math.pow(y - c.y, 2) + math.pow(z - c.z, 2))
  }

}


class Simulation extends Actor with ActorLogging {
  implicit val timeout = Timeout(1.seconds)
  val structure = context.actorOf(Props[StructureManager], "structuremanager")
  log.info("Loading configuration...")

  /*
   * Loading configuration file
   */
  Simulation.configuration.structures.foreach(s => {
    log.info("Spawning new structure: " + s._2)
    structure ! NewStructure(s._2)
  })

  val list = structure ? StructureInVicinty(Coordinate(1, 1, 1), 5)
  println(list.isCompleted)

  var ticker = context.system.scheduler.schedule(1 second, 1 second, self, Tick)


  def receive = {
    case Tick =>
      structure ! Tick
    case TickRate(rate) =>
      log.info("Changing tick rate to " + rate)
      ticker.cancel()
      Simulation.tickRate = rate
      ticker = context.system.scheduler.schedule(rate, rate, self, Tick)
  }

  override def postStop() {
    ticker.cancel()
  }
}





