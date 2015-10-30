package yuima.algovis.maze.core

import yuima.algovis.maze.ui.Visualizer

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/25.
  */
case class MazeOnSearch(base: Maze, vis: Option[Visualizer]) {
  val goal = {
    val g = base.fields.flatten.indexWhere(_ == FieldType.GOAL)
    base.Passage(g / base.fields.head.length, g % base.fields.head.length, -1)
  }

  def apply(x: Int, y: Int) = base.fields(x)(y)

  def update(x: Int, y: Int, dir: Direction.Value, leaves: Seq[Maze#Position], ft: FieldType.Value) = {
    visAction { _.searchCell(x, y, dir, leaves) }
    base.fields(x)(y) = ft
  }

  def update(x: Int, y: Int, dir: Direction.Value, distanceTravelled: Int,
             prev: Maze#Passage, leaves: Seq[Maze#Position], goal: Maze#Position, moved: Int,
             ft: FieldType.Value) = {
    val passage = base.Passage(x, y, distanceTravelled)
    visAction {
      _.searchCell(x, y, dir, leaves,
                      passage.euclideanDistanceTo(prev).toInt,
                      passage.euclideanDistanceTo(goal).toInt,
                      distanceTravelled, moved)
    }
    base.fields(x)(y) = ft
  }

  private def visAction(op: Visualizer => Unit) = vis match {
    case Some(v) => op(v)
    case None    =>
  }
}

object MazeOnSearch {
  def apply(base: Maze, vis: Option[Visualizer])(implicit dummyImplicit: DummyImplicit) = new MazeOnSearch(base, vis)

  def apply(base: Maze) = new MazeOnSearch(base, None)

  def apply(base: Maze, vis: Visualizer)(implicit dummyImplicit: DummyImplicit) = new MazeOnSearch(base, Some(vis))
}