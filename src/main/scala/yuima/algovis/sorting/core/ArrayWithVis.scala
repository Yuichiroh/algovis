package yuima.algovis.sorting.core

import yuima.algovis.sorting.ui.Visualizer

/** Created by yuichiroh on 15/08/16.
  */
case class ArrayWithVis(arr: Array[Int], vis: Visualizer) extends collection.mutable.IndexedSeq[Int] {
  val length = arr.length

  def apply(index: Int) = {
    vis.focus(arr.toIndexedSeq, index)
    arr(index)
  }

  def update(index: Int, value: Int) = {
    vis.update(arr.toIndexedSeq, index, value)
    arr(index) = value
  }
}