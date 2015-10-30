package yuima.algovis.maze.core

/**
 * @author Yuichiroh Matsubayashi
 *         Created on 15/08/20.
 */
object FieldType extends Enumeration {
  val WALL, PASSAGE, SEARCHED, LEAF, SEARCHING, BEST, START, GOAL = Value
}
