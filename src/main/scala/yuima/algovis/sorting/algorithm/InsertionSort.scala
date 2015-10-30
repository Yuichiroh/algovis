package yuima.algovis.sorting.algorithm

/** Created by yuichiroh on 15/08/19.
  */
object InsertionSort extends (collection.mutable.IndexedSeq[Int] => Unit) {
  def apply(seq: collection.mutable.IndexedSeq[Int]) = {
    for (i <- 1 until seq.size) {
      val value = seq(i)
      if (seq(i) < seq(i - 1)) {
        var j = i
        do {
          seq(j) = seq(j - 1)
          j -= 1
        } while (j > 0 && value < seq(j - 1))
        seq(j) = value
      }
    }
  }
}
