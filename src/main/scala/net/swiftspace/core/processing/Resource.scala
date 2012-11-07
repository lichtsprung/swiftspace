package net.swiftspace.core.processing

/**
 * Resources are the basic building blocks for all different kinds of items.
 * @param name the name of the resource
 * @param description a short description
 * @param mass the mass of this resource
 * @param characteristics a list of characteristics that this resource has
 */
case class Resource(
                     name: String,
                     description: String,
                     mass: Double,
                     characteristics: List[String]
                     )

/**
 * Companion object that currently only holds the None-Resource that is used as default resource if an unknown resource
 * name is passed to a function.
 */
object Resource {
  val None = Resource("None", "Unknown resource", 0.0, List())
}
