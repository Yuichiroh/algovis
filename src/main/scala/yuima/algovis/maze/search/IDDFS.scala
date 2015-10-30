package yuima.algovis.maze.search

import yuima.algovis.maze.core.{Maze, MazeOnSearch}

import scala.annotation.tailrec
import scala.util.Random

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/21.
  */
object IDDFS extends SearchAlgorithm {
  def apply(implicit maze: MazeOnSearch) = {
    val m = maze.base
    val step = (m.width + m.height) / 10
    var moved = 0
    var prev: Maze#Passage = m.start

    search(m.start, 1)

    @tailrec
    def search(passage: Maze#Passage,
               limit: Int,
               candidates: List[Maze#Door] = Nil): Unit = {
      val cs = if (passage.distanceTravelled < limit) {
        prev = passage
        Random.shuffle(nextCandidates(passage)).toList ::: candidates
      } else candidates

      cs match {
        case door :: doors =>
          nextPassageIsGoal(door, prev, candidates, maze.goal, moved, maze.vis) match {
            case Some(psg) =>
              moved += 1
              search(psg, limit, doors)
            case None      =>
          }
        case Nil           =>
          moved = 0
          maze.base.reset()
          prev = m.Passage(1, 1, 0)
          search(m.Passage(1, 1, 0), limit + step)
      }
    }
  }
}
