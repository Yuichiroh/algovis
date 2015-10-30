package yuima.algovis.sorting.algorithm

/** Created by yuichiroh on 15/08/19.
  */
object GnomeSort extends (collection.mutable.IndexedSeq[Int] => Unit) {
  def apply(seq: collection.mutable.IndexedSeq[Int]) = {
    def swap(i: Int, j: Int) = {
      val tmp = seq(i)
      seq(i) = seq(j)
      seq(j) = tmp
    }

    var i = 1
    var last = 0
    while (i < seq.size) {
      if (seq(i - 1) <= seq(i)) {
        if (last > 0) {
          i = last
          last = 0
        }
        i += 1
      }
      else {
        swap(i - 1, i)
        if (i > 1) {
          if (last == 0) last = i
          i -= 1
        }
        else i += 1
      }
    }
  }
}
