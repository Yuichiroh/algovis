package yuima.algovis.sorting.algorithm

import scala.annotation.tailrec

/** Created by yuichiroh on 15/08/19.
  */
object CocktailSort extends (collection.mutable.IndexedSeq[Int] => Unit) {
  def apply(seq: collection.mutable.IndexedSeq[Int]) = {
    def swap(i: Int, j: Int) = {
      val tmp = seq(i)
      seq(i) = seq(j)
      seq(j) = tmp
    }

    @tailrec
    def shake(start: Int, end: Int, step: Int = 1): Unit = {
      var swaped = false
      var last = end
      (start to end by step).foreach { i =>
        if (seq(i) > seq(i + 1)) {
          swap(i, i + 1)
          swaped = true
          last = i
        }
      }
      if (swaped) shake(last, start, -step)
    }

    shake(0, seq.length - 2)
  }
}
