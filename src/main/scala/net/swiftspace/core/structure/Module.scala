package net.swiftspace.core.structure

import akka.actor.{ActorLogging, Actor}

import collection.mutable
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.Structure.{DemandResource, ReceiveResource}
import net.swiftspace.core.Simulation


/**
 * Part of a structure.
 */
abstract class Module extends Actor with ActorLogging

/**
 * Can process resources.
 * @param name the name of this unit
 * @param input the input resources and how much is needed to produce one unit of the resulting resource
 * @param output the resource that is being produced
 * @param processingTime how long it takes to produce one unit
 * @param capacity how much the processing unit can store
 */
class ProcessingModule(name: String,
                       input: List[(String, Double)],
                       output: List[(String, Double)],
                       processingTime: Double,
                       capacity: Double)
  extends Module {

  import net.swiftspace.core.Simulation.Tick

  val resources = mutable.HashMap[Resource, Double]().withDefaultValue(0.0)
  input.foreach(r => resources += getResource(r._1) -> 0.0)

  val produces = mutable.HashMap[Resource, Double]().withDefaultValue(0.0)
  output.foreach(r => produces += getResource(r._1) -> 0.0)
  log.info("produces: " + produces)

  var counter = 0.0
  var storage = 0.0

  def processResources(): Unit = {
    log.debug("processing " + resources + " to " + produces)
    if (input.foldLeft(true)((a, b) => a && resources.get(getResource(b._1)).get >= b._2)) {
      storage += output.foldLeft(0.0)((a, b) => a + b._2)
      log.debug("capacity is: " + capacity)
      log.debug(capacity - storage + " storage capacity left!")
      input.foreach(r => resources.update(getResource(r._1), resources.get(getResource(r._1)).get - r._2))
      output.foreach(r => produces.update(getResource(r._1), produces.get(getResource(r._1)).get + r._2))
    } else {
      input.foreach(r => {
        if (resources.get(getResource(r._1)).get < r._2) {
          log.debug("Demanding resource from main structure: " + getResource(r._1).name)
          context.parent ! DemandResource(getResource(r._1), r._2 * 10)
        }
      })
    }
  }

  private def getResource(name: String): Resource = {
    Simulation.configuration.resources.get(name).getOrElse(Resource.None)
  }

  def receive = {
    case Tick =>
      counter += Simulation.tickRate.toSeconds
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

  /**
   * This class is used as a descriptor for a processing unit in the simulation. It can be sent between actors and used
   * as a reference to create a new processing module.
   *
   * @param input the input resources
   * @param output the resources that are being produced
   * @param processingTime the time it takes to create 1 unit of the output
   * @param capacity the maximum storage capacity before it is delivered to the main structure
   */
  case class ProcessingModuleDescriptor(name: String,
                                        input: List[(String, Double)],
                                        output: List[(String, Double)],
                                        processingTime: Double,
                                        capacity: Double)

}
