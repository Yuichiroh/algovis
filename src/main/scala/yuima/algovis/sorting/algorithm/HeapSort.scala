package yuima.algovis.sorting.algorithm

/** Created by yuichiroh on 15/08/20.
  */
object HeapSort extends (collection.mutable.IndexedSeq[Int] => Unit) {

  def apply(seq: collection.mutable.IndexedSeq[Int]) = {

    var heapSize = seq.length
    def sort() = {
      build()
      (seq.length - 1 to 1 by -1).foreach { index =>
        swap(0, index)
        heapSize -= 1
        maxHeapify(0)
      }
    }

    def build() = (seq.length / 2 - 1 to 0 by -1).foreach(maxHeapify)

    def maxHeapify(index: Int): Unit = {
      val l = left(index)
      val r = right(index)
      var largest = index

      if (l < heapSize && seq(l) > seq(largest))
        largest = l
      if (r < heapSize && seq(r) > seq(largest))
        largest = r
      if (largest != index) {
        swap(index, largest)
        maxHeapify(largest)
      }
    }

    def swap(i: Int, j: Int) = {
      val tmp = seq(i)
      seq(i) = seq(j)
      seq(j) = tmp
    }

    def left(index: Int) = (index << 1) + 1

    def right(index: Int) = (index + 1) << 1

    sort()
  }
}
