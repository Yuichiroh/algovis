package yuima.algovis.maze.gen

import yuima.algovis.maze.core.FieldType._
import yuima.algovis.maze.core.MazeOnGeneration

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/24.
  */
object Kruskal extends GenerationAlgorithm {
  /** generates a maze with randomized Kruskal's algorithm. */
  def apply(implicit maze: MazeOnGeneration) = {
    val m = maze.base
    generate()

    def generate(): Unit = {
      val walls = ArrayBuffer[(Int, Int)]()
      val clusters = (0 until m.width).map { i =>
        (0 until m.height).map { j => i * m.height + j }.toArray
      }.toArray

      for {
        x <- 0 until 2 * m.width + 1
        y <- 0 until 2 * m.height + 1
      } {
        if (x % 2 == 1 && y % 2 == 1) maze(x, y) = PASSAGE
        else if ((x % 2 == 1 || y % 2 == 1) &&
          x != 0 && x != 2 * m.width &&
          y != 0 && y != 2 * m.height)
          walls.append((x, y))
      }
      maze(m.start.x, m.start.y) = START
      breakWall(clusters, Random.shuffle(walls.toList))
    }

    @tailrec
    def breakWall(clusters: Array[Array[Int]],
                  walls: List[(Int, Int)]): Unit = {
      walls match {
        case Nil          => createGoal()
        case head :: tail =>
          val wall@(x, y) = head

          val (psg1, psg2) =
            if (x % 2 == 1) (m.Position(x, y - 1), m.Position(x, y + 1))
            else (m.Position(x - 1, y), m.Position(x + 1, y))
          if (clusters(psg1.x / 2)(psg1.y / 2) != clusters(psg2.x / 2)(psg2.y / 2)) {
            maze(x, y) = PASSAGE
            val v1 = clusters(psg1.x / 2)(psg1.y / 2)
            val v2 = clusters(psg2.x / 2)(psg2.y / 2)
            val unified = v1 min v2
            clusters.foreach { cs =>
              cs.indices.foreach { j => if (cs(j) == v1 || cs(j) == v2) cs(j) = unified }
            }
          }
          breakWall(clusters, tail)
      }
    }
  }
}
