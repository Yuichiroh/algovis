package yuima.algovis.sorting.algorithm

/** Created by yuichiroh on 15/08/19.
  */
object CombSort extends (collection.mutable.IndexedSeq[Int] => Unit) {
  def apply(seq: collection.mutable.IndexedSeq[Int]) = {
    def swap(i: Int, j: Int) = {
      val tmp = seq(i)
      seq(i) = seq(j)
      seq(j) = tmp
    }

    var h = seq.length
    var numSwap = 1
    while (h > 1 || numSwap > 0) {
      numSwap = 0
      if (h > 1) h = h * 10 / 13
      if (8 < h && h < 11) h = 11
      for (i <- 0 until seq.length - h) {
        if (seq(i) > seq(i + h)) {
          swap(i, i + h)
          numSwap += 1
        }
      }
    }
  }
}
