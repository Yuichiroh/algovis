package yuima.algovis.maze.search

import yuima.algovis.maze.core.{Maze, MazeOnSearch}
import yuima.algovis.maze.ui.Visualizer

import scala.annotation.tailrec
import scala.util.Random

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/21.
  */
object DepthFirstSearch extends SearchAlgorithm {
  def apply(implicit maze: MazeOnSearch) = {
    val m = maze.base
    var moved = 0
    var prev: Maze#Passage = m.start

    search(m.start)

    @tailrec
    def search(passage: Maze#Passage,
               candidates: List[Maze#Door] = Nil): Unit = {
      prev = passage
      Random.shuffle(nextCandidates(passage)).toList ::: candidates match {
        case door :: doors =>
          maybeNextPassage(door, prev, candidates, maze.goal, moved, maze.vis) match {
            case Some(psg) =>
              moved += 1
              search(psg, doors)
            case None      =>
          }
        case Nil           =>
      }
    }
  }
}
