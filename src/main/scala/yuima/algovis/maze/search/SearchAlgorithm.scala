package yuima.algovis.maze.search

import yuima.algovis.maze.core.FieldType._
import yuima.algovis.maze.core.{Maze, MazeOnSearch}
import yuima.algovis.maze.ui.Visualizer

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/21.
  */
trait SearchAlgorithm {
  def apply(implicit maze: MazeOnSearch): Unit

  def nextCandidates(passage: Maze#Passage)(implicit maze: MazeOnSearch): Seq[Maze#Door] = {
    val m = maze.base
    m.Position.adjacent(PASSAGE)(passage.x, passage.y)
    .filter(w => m.Position.opposite(PASSAGE).tupled(w).isDefined ||
                 m.Position.opposite(GOAL).tupled(w).isDefined)
    .map { case (x, y, dir) => m.Door(x, y, passage.distanceTravelled + 1, dir) }
  }

  def nextPassageIsGoal(door: Maze#Door, prev: Maze#Passage, leaves: Seq[Maze#Position], goal: Maze#Position,
                        moved: Int, vis: Option[Visualizer])
                       (implicit maze: MazeOnSearch)
  : Option[Maze#Passage] = {
    val m = maze.base

    val m.Door(x, y, distanceTravelled, dir) = door
    maze(x, y, dir, leaves) = SEARCHED

    m.Position.opposite(PASSAGE)(x, y, dir) match {
      case Some((x2, y2)) =>
        val passage = m.Passage(x2, y2, distanceTravelled)
        maze(x2, y2, dir, distanceTravelled, prev, leaves, goal, moved) = SEARCHED
        Some(passage)
      case None           => None
    }
  }

  override def toString = this.getClass.getSimpleName.replaceAll("([a-z])(_?)([A-Z])", "$1 $3").replace("$", "")
}