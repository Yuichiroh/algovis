package yuima.algovis.maze.gen

import yuima.algovis.maze.core.FieldType._
import yuima.algovis.maze.core.{Maze, MazeOnGeneration}

import scala.util.Random

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/24.
  */
trait GenerationAlgorithm {
  def apply(implicit maze: MazeOnGeneration): Unit

  override def toString = this.getClass.getSimpleName.replaceAll("([a-z])(_?)([A-Z])", "$1 $3").replace("$", "")

  protected def createGoal()(implicit maze: MazeOnGeneration) = {
    val candidates = goalCandidates(maze.base)
    candidates.lift(Random.nextInt(candidates.size)) match {
      case Some((x, y)) =>
        maze(x, y) = GOAL
        true
      case None         => false
    }
  }

  private def goalCandidates(maze: Maze) = for {
    i <- 0 until maze.width
    j <- 0 until maze.height
    x = i * 2 + 1
    y = j * 2 + 1
    if (x != maze.start.x || y != maze.start.y) && maze.Position.adjacent(WALL)(x, y).size == 3
  } yield (x, y)
}
