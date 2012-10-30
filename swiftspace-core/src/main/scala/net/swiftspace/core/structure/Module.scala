package net.swiftspace.core.structure

import akka.actor.{ActorLogging, Actor, ActorRef}

import collection.mutable
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.Structure.{DemandResource, ReceiveResource}


/**
 * Part of a structure.
 */
abstract class Module extends Actor with ActorLogging

/**
 * Can process resources.
 * @param input the input resources and how much is needed to produce one unit of the resulting resource
 * @param output the resource that is being produced
 * @param processingTime how long it takes to produce one unit
 * @param capacity how much the processing unit can store
 */
class ProcessingModule(
                        input: Seq[(Resource, Double)],
                        output: Seq[(Resource, Double)],
                        processingTime: Double,
                        capacity: Double)
  extends Module {

  import net.swiftspace.core.Simulation.Tick

  val resources = mutable.HashMap[Resource, Double]().withDefaultValue(0.0)
  input.foreach(r => resources += r._1 -> 0.0)

  val produces = mutable.HashMap[Resource, Double]().withDefaultValue(0.0)
  output.foreach(r => produces += r._1 -> 0.0)
  log.info("produces: " + produces)

  var counter = 0.0
  var storage = 0.0

  def processResources(): Unit = {
    log.debug("processing " + resources + " to " + produces)
    if (input.foldLeft(true)((a, b) => a && resources.get(b._1).get >= b._2)) {
      storage += output.foldLeft(0.0)((a, b) => a + b._2)
      log.debug("capacity is: " + capacity)
      log.debug(capacity - storage + " storage capacity left!")
      input.foreach(r => resources.update(r._1, resources.get(r._1).get - r._2))
      output.foreach(r => produces.update(r._1, produces.get(r._1).get + r._2))
    } else {
      input.foreach(r => {
        if (resources.get(r._1).get < r._2) {
          log.debug("Demanding resource from main structure: " + r._1.name)
          context.parent ! DemandResource(r._1, r._2 * 10)
        }
      })
    }
  }

  def receive = {
    case Tick =>
      counter += 0.1
      if (counter >= processingTime) {
        processResources()
        counter = 0
      }
      if (storage > 0.8 * capacity) {
        produces.foreach(r => {
          context.parent ! ReceiveResource(r._1, r._2)
          produces.update(r._1, 0)
        })
        storage = 0.0
      }
    case ReceiveResource(resource, amount) =>
      if (resources.contains(resource)) {
        resources.update(resource, resources.get(resource).get + amount)
      } else {
        log.error("Module cannot process " + resource)
      }

      log.debug("Amount of " + resource + " available: " + resources.get(resource))
  }
}

object Module {

  case class ProcessingModuleDescriptor(
                                         input: Seq[(Resource, Double)],
                                         output: Seq[(Resource, Double)],
                                         processingTime: Double,
                                         capacity: Double)

  class WaterGenerator(level: Int = 1) extends ProcessingModuleDescriptor(
    Seq[(Resource, Double)]((
      Resource("Oxygen"), 1),
      (Resource("Hydrogen"), 2),
      (Resource("Energy"), 0.5)),
    Seq[(Resource, Double)](
      (Resource("Water"), 1)),
    1.0 / level, 50 * level)

  class Electrolyser(level: Int = 1) extends ProcessingModuleDescriptor(
    Seq[(Resource, Double)]((
      Resource("Water"), 1),
      (Resource("Energy"), 2)),
    Seq[(Resource, Double)](
      (Resource("Oxygen"), 1),
      (Resource("Hydrogen"), 2)),
    2.0 / level, 50 * level)

  class PowerGenerator(level: Int = 1) extends ProcessingModuleDescriptor(
    Seq[(Resource, Double)]((
      Resource("Hydrogen"), 1)),
    Seq[(Resource, Double)](
      (Resource("Energy"), 3),
      (Resource("Helium"), 0.1)),
    1.0 / level, 20 * level)


}
