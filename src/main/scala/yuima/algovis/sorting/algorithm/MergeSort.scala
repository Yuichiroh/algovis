package yuima.algovis.sorting.algorithm

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

/** Created by yuichiroh on 15/08/19.
  */
object MergeSort extends (collection.mutable.IndexedSeq[Int] => Unit) {
  def apply(seq: collection.mutable.IndexedSeq[Int]) = {
    def sort(start: Int, end: Int): Unit = {
      if (end - start > 1) {
        val mid = divide(start, end)
        sort(start, mid)
        sort(mid, end)
        val result = merge(start, mid, mid, end)

        (start until end).foreach(i => seq(i) = result(i - start))
      }
    }

    def divide(start: Int, end: Int) = (start + end) >>> 1

    @tailrec
    def merge(s1: Int, e1: Int, s2: Int, e2: Int, sorted: ArrayBuffer[Int] = ArrayBuffer[Int]()): ArrayBuffer[Int] = {
      if (e1 - s1 < 1) {
        if (e2 - s2 < 1) sorted
        else {
          sorted.append(seq(s2))
          merge(s1, e1, s2 + 1, e2, sorted)
        }
      }
      else if (e2 - s2 < 1) {
        sorted.append(seq(s1))
        merge(s1 + 1, e1, s2, e2, sorted)
      }
      else if (seq(s1) < seq(s2)) {
        sorted.append(seq(s1))
        merge(s1 + 1, e1, s2, e2, sorted)
      }
      else {
        sorted.append(seq(s2))
        merge(s1, e1, s2 + 1, e2, sorted)
      }
    }

    sort(0, seq.length)
  }
}