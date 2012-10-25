package net.swiftspace.core

import java.util.Date
import structure.{Structure, Module}


sealed case class SimulationMessage()

/**
 * Update Signal of the simulation clock.
 * @param currentTime the current time in the simulation
 */
case class Tick(currentTime: Date) extends SimulationMessage

/**
 * Starts the simulation.
 * @param interval
 */
case class StartClock(interval: Int) extends SimulationMessage

/**
 * Pauses the simulation.
 */
case class StopClock() extends SimulationMessage

/**
 * Changes the update interval of the simulation.
 * @param interval the new interval
 */
case class ChangeTickInterval(interval: Int) extends SimulationMessage

/**
 * Creates a new Structure.
 * @param name the name of the structure
 * @param coordinate the position of the structure
 * @param moduleCount the number of modules that can be installed
 */
case class CreateStructure(name: String, coordinate: Coordinate, moduleCount: Int) extends SimulationMessage

/**
 * Installs a new module to the structure.
 * @param module the module being installed
 */
case class InstallModule(module: StructureModule) extends SimulationMessage

/**
 * Creates a new person in the simulation.
 * @param name the name of the person
 * @param age the age of the person
 */
case class CreatePerson(name: String, age: Int) extends SimulationMessage

/**
 * Moves the person to a new structure immediately.
 * @param structure the structure the person is moved to
 */
case class MovePerson(structure: Structure) extends SimulationMessage
