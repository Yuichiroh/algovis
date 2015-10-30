package yuima.algovis.maze.gen

import yuima.algovis.maze.core.FieldType._
import yuima.algovis.maze.core.{Direction, MazeOnGeneration}

import scala.annotation.tailrec
import scala.util.Random

/** A maze generator with depth-first search algorithm.
  *
  * @author Yuichiroh Matsubayashi
  *         Created on 15/08/24.
  */
object DepthFirst extends GenerationAlgorithm {
  /** generates a maze with depth-first search algorithm. */
  def apply(implicit maze: MazeOnGeneration) = {
    val m = maze.base
    generate()

    @tailrec
    def generate(): Unit = {
      val passage = createInitialPassage
      createPassages(passage)
      maze(m.start.x, m.start.y) = START
      val success = createGoal
      if (!success) {
        (0 to 2 * m.width).foreach { i =>
          (0 to 2 * m.height).foreach { j => maze(i, j) = WALL }
        }
        generate()
      }
    }

    def createInitialPassage = {
      val x = Random.nextInt(m.width - 1) * 2 + 1
      val y = Random.nextInt(m.height - 1) * 2 + 1
      maze(x, y) = PASSAGE
      (x, y)
    }

    @tailrec
    def createPassages(passage: (Int, Int), candidates: List[(Int, Int, Direction.Value)] = Nil): Unit = {
      val (x, y) = passage
      val cs = m.Position.adjacent(WALL)(x, y)
        .collect { case w if m.Position.opposite(WALL).tupled(w).isDefined => w }

      Random.shuffle(cs) ::: candidates match {
        case head :: tail =>
          val p = createPassage(head)
          createPassages(p, tail.filter(m.Position.opposite(WALL).tupled(_).isDefined))
        case Nil          =>
      }
    }

    def createPassage(wall: (Int, Int, Direction.Value)) = {
      val (x, y, dir) = wall
      val passage@(x2, y2) = m.Position.opposite(WALL)(x, y, dir).get
      maze(x, y) = PASSAGE
      maze(x2, y2) = PASSAGE
      passage
    }
  }
}
