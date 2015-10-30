package yuima.algovis.sorting.algorithm

/** Created by yuichiroh on 15/08/19.
  */
object SelectionSort extends (collection.mutable.IndexedSeq[Int] => Unit) {
  def apply(seq: collection.mutable.IndexedSeq[Int]) = {
    def swap(i: Int, j: Int) = {
      val tmp = seq(i)
      seq(i) = seq(j)
      seq(j) = tmp
    }

    for (i <- seq.indices) {
      var minIndex = i
      for (j <- i until seq.size)
        if (seq(j) < seq(minIndex)) minIndex = j
      swap(i, minIndex)
    }
  }
}
