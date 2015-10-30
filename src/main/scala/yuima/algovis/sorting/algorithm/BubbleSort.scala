package yuima.algovis.sorting.algorithm

/** Created by yuichiroh on 15/08/19.
  */
object BubbleSort extends (collection.mutable.IndexedSeq[Int] => Unit) {
  def apply(seq: collection.mutable.IndexedSeq[Int]) = {
    def swap(i: Int, j: Int) = {
      val tmp = seq(i)
      seq(i) = seq(j)
      seq(j) = tmp
    }

    var last = seq.length
    for (i <- 0 until seq.length - 1) {
      for (j <- 1 until last) {
        if (seq(j) < seq(j - 1)) {
          swap(j, j - 1)
          last = j
        }
      }
    }
  }
}
