package yuima.algovis.sorting.algorithm

/** Created by yuichiroh on 15/08/16.
  */
object ShellSort extends (collection.mutable.IndexedSeq[Int] => Unit) {
  def apply(seq: collection.mutable.IndexedSeq[Int]) = {
    def insertionSortWithInterval(h: Int) = {
      for (i <- h until seq.size by h) {
        val value = seq(i)
        if (seq(i) < seq(i - h)) {
          var j = i
          do {
            seq(j) = seq(j - h)
            j -= h
          } while (j > 0 && value < seq(j - h))
          seq(j) = value
        }
      }
    }

    val hs = Stream.iterate(1) { h => 3 * h + 1 }.takeWhile(_ < seq.length / 3).reverseIterator
    while (hs.hasNext) {
      val h = hs.next()
      insertionSortWithInterval(h)
    }
  }
}




