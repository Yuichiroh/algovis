package yuima.algovis.maze.search

import yuima.algovis.maze.core.Direction

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/21.
  */
//class Position(val x: Int, val y: Int) {
//  def euclideanDistanceTo[P <: Position](p: P) = math.sqrt(math.pow(x.toDouble - p.x, 2) + math.pow(y.toDouble - p.y, 2))
//
//  override def equals(o: Any) = o match {
//    case that: Position => (this.x, this.y) equals (that.x, that.y)
//    case _              => false
//  }
//}
//
//object Position {
//  def apply(x: Int, y: Int) = new Position(x, y)
//}
//
//case class Passage(override val x: Int, override val y: Int, manhattanFromStart: Int) extends Position(x, y)
//
//case class Door(override val x: Int, override val y: Int, distanceTravelled: Int, dir: Direction.Value) extends Position(x, y) with Ordered[Door] {
//
//  import scala.math.Ordered.orderingToOrdered
//
//  def compare(that: Door): Int = (this.distanceTravelled, this.x, this.y) compare (that.distanceTravelled, that.x, that.y)
//}