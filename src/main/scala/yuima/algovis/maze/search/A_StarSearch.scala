package yuima.algovis.maze.search

import yuima.algovis.maze.core.{Maze, MazeOnSearch}
import yuima.algovis.maze.ui.Visualizer

import scala.annotation.tailrec
import scala.collection.mutable

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/21.
  */
object A_StarSearch extends SearchAlgorithm {
  def apply(implicit maze: MazeOnSearch) = {
    val m = maze.base
    var moved = 0
    val candidates = mutable.PriorityQueue.empty[(Double, Maze#Door)]

    var prev: Maze#Passage = m.start
    search(m.start)

    @tailrec
    def search(passage: Maze#Passage): Unit = {
      prev = passage
      nextCandidates(passage).foreach { door =>
        val score = door.distanceTravelled + door.euclideanDistanceTo(maze.goal)
        candidates.enqueue((-score, door))
      }

      if (candidates.nonEmpty) {
        val (_, door) = candidates.dequeue()
        nextPassageIsGoal(door, prev, candidates.toList.map(_._2), maze.goal, moved, maze.vis) match {
          case Some(psg) =>
            moved += 1
            search(psg)
          case None      =>
        }
      }
    }
  }
}
