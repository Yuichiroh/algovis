package yuima.algovis.maze.search

import yuima.algovis.maze.core.{Maze, MazeOnSearch}
import yuima.algovis.maze.ui.Visualizer

import scala.annotation.tailrec
import scala.collection.immutable.Queue

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/21.
  */
object BreadthFirstSearch extends SearchAlgorithm {
  def apply(implicit maze: MazeOnSearch) = {
    val m = maze.base
    var moved = 0
    var prev: Maze#Passage = m.start

    search(m.start)

    @tailrec
    def search(passage: Maze#Passage,
               candidates: Queue[Maze#Door] = Queue.empty[Maze#Door]): Unit = {
      prev = passage
      val cs = (candidates /: nextCandidates(passage)) { (q, c) => q.enqueue(c) }
      if (cs.nonEmpty) {
        val (door, doors) = cs.dequeue
        nextPassageIsGoal(door, prev, candidates.toList, maze.goal, moved, maze.vis) match {
          case Some(psg) =>
            moved += 1
            search(psg, doors)
          case None      =>
        }
      }
    }
  }
}
