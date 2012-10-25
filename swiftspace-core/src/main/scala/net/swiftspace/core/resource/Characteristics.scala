package net.swiftspace.core.resource

import collection.mutable


/**
 * The characteristics of a resource.
 */
case class Characteristics() {
  val characteristics = mutable.HashMap[Characteristic.type, Double]()

  def addCharacteristic(characteristic: Characteristic.type, degree: Double) = characteristics += characteristic -> degree

  def removeCharacteristic(characteristic: Characteristic.type) = characteristics.remove(characteristic)
}

/**
 * Characteristics of a Resource
 */
object Characteristic extends Enumeration {
  type Characteristic = Value
  val Toxic, Acid = Value
}

object Phase extends Enumeration {
  type Phase = Value
  val Liquid, Solid, Gaseous = Value
}