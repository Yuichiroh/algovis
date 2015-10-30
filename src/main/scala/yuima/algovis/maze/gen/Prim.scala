package yuima.algovis.maze.gen

import yuima.algovis.maze.core.FieldType._
import yuima.algovis.maze.core.{Direction, MazeOnGeneration}

import scala.annotation.tailrec
import scala.util.Random

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/24.
  */
object Prim extends GenerationAlgorithm {
  def apply(implicit maze: MazeOnGeneration) = {
    val m = maze.base
    generate()

    /** generates a maze with randomized Prim's algorithm. */
    @tailrec
    def generate(): Unit = {
      val adjacentWalls = collection.mutable.Set[(Int, Int, Direction.Value)]()
      m.Position.adjacent(WALL).tupled(createInitialPassage).foreach(adjacentWalls.add)
      createPassagesWithRandomizedPrim(adjacentWalls)
      maze(1, 1) = START
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
    def createPassagesWithRandomizedPrim(adjacentWalls: collection.mutable.Set[(Int, Int, Direction.Value)]): Unit = {
      if (adjacentWalls.nonEmpty) {
        val wall = adjacentWalls.toSeq(Random.nextInt(adjacentWalls.size))
        createPassageWithRandomPrim(wall, adjacentWalls)
        createPassagesWithRandomizedPrim(adjacentWalls)
      }
    }

    def goalCandidates =
      for {
        i <- 0 until m.width
        j <- 0 until m.height
        x = i * 2 + 1
        y = j * 2 + 1
        if (x != m.start.x || y != m.start.y) && m.Position.adjacent(WALL)(x, y).size == 3
      } yield (x, y)

    def createGoal = {
      val candidates = goalCandidates
      candidates.lift(Random.nextInt(candidates.size)) match {
        case Some((x, y)) =>
          maze(x, y) = GOAL
          true
        case None         => false
      }
    }

    def createPassageWithRandomPrim(wall: (Int, Int, Direction.Value),
                                    adjacentWalls: collection.mutable.Set[(Int, Int, Direction.Value)]) = {
      adjacentWalls.remove(wall)
      val (x, y, dir) = wall
      m.Position.opposite(WALL)(x, y, dir) match {
        case Some((x2, y2)) =>
          maze(x, y) = PASSAGE
          maze(x2, y2) = PASSAGE
          m.Position.adjacent(WALL)(x2, y2).foreach(adjacentWalls.add)
        case None           =>
      }
    }

  }
}
