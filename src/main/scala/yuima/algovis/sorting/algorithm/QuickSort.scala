package yuima.algovis.sorting.algorithm

import scala.annotation.tailrec

/** Created by yuichiroh on 15/08/19.
  */
object QuickSort extends (collection.mutable.IndexedSeq[Int] => Unit) {
  def apply(seq: collection.mutable.IndexedSeq[Int]) = {
    def swap(i: Int, j: Int) = {
      val tmp = seq(i)
      seq(i) = seq(j)
      seq(j) = tmp
    }

    def partition(start: Int, end: Int) = {
      val pivot = seq(end)
      var i = start
      (start until end).foreach { j =>
        if (seq(j) <= pivot) {
          swap(i, j)
          i = i + 1
        }
      }
      swap(i, end)
      i
    }

    /** Median-of-three pivoting brings this down to Cn, 2 ≈ 1.188 n log n,
      * at the expense of a three-percent increase in the expected number of swaps.
      * https://www.wikiwand.com/en/Quicksort#/Implementation_issues */
    def medianOf3(x: Int, y: Int, z: Int) =
      if (x < y)
        if (y < z) y
        else if (z < x) x
        else z
      else if (z < y) y
      else if (x < z) x
      else z


    def partitionWithMedianOf3Pivoting(start: Int, end: Int) = {
      val pivot = medianOf3(seq(start), seq(start + (end - start) / 2), seq(end))
      var i = start
      var j = end

      @tailrec
      def swapWithPivot: (Int, Int) = {
        while (seq(i) < pivot) { i += 1 }
        while (pivot < seq(j)) { j -= 1 }
        if (i < j) {
          swap(i, j)
          i += 1
          j -= 1
          swapWithPivot
        }
        else (i, j)
      }
      swapWithPivot
    }

    def sort(start: Int, end: Int): Unit = {
      if (start < end) {
        val (i, j) = partitionWithMedianOf3Pivoting(start, end)
        sort(start, i - 1)
        sort(j + 1, end)
      }
    }

    sort(0, seq.length - 1)
  }
}
