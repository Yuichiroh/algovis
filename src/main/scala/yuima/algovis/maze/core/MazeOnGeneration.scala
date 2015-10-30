package yuima.algovis.maze.core

import yuima.algovis.maze.ui.Visualizer

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/25.
  */
case class MazeOnGeneration(base: Maze, vis: Option[Visualizer]) {
  var prev: Maze#Position = base.Position(1, 1)
  var moved = 0

  def apply(x: Int, y: Int): FieldType.Value = base.fields(x)(y)

  def update(x: Int, y: Int, ft: FieldType.Value) = {
    visAction {
      val pos = base.Position(x, y)
      _.generateCell(x, y, ft, pos.euclideanDistanceTo(prev), moved / 2)
    }
    base.fields(x)(y) = ft
    moved += 1
  }

  private def visAction(op: Visualizer => Unit) = vis match {
    case Some(v) => op(v)
    case None    =>
  }
}

object MazeOnGeneration {
  def apply(base: Maze, vis: Option[Visualizer])(implicit dummyImplicit: DummyImplicit) = new MazeOnGeneration(base, vis)

  def apply(base: Maze) = new MazeOnGeneration(base, None)

  def apply(base: Maze, vis: Visualizer)(implicit dummyImplicit: DummyImplicit) = new MazeOnGeneration(base, Some(vis))
}
