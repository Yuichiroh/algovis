package yuima.algovis.sound

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/19.
  */
abstract class Notes {
  val scaleSize = 12

  /** A starting point of notes.
    * Sound that is perceptible by humans has frequencies from about 20 Hz to 20,000 Hz, and 24 is C1 = 32.7 Hz. */
  val start = 24

  /* keys */
  val C = 0
  val C_S = 1
  val D_F = 1
  val D = 2
  val D_S = 3
  val E_F = 3
  val E = 4
  val F = 5
  val F_S = 6
  val G_F = 6
  val G = 7
  val G_S = 8
  val A_F = 8
  val A = 9
  val A_S = 10
  val B_F = 10
  val B = 11

  val keys: IndexedSeq[Int]

  def size = length

  def length = ((128.0 - start) / scaleSize * keys.length).toInt

  def apply(index: Int) = index / keys.length * scaleSize + keys(index % keys.length) + start

  override def toString = this.getClass.getSimpleName.replaceAll("([a-z])([A-Z])", "$1 $2").replace("$", "")
}

object ChromaticScale extends Notes {
  val keys = IndexedSeq(C, C_S, D, D_S, E, F, F_S, G, G_S, A, A_S, B)
}

object DiatonicScale extends Notes {
  val keys = IndexedSeq(C, D, E, F, G, A, B)
}

object AscendingMelodicMinorScale extends Notes {
  val keys = IndexedSeq(C, D, E_F, F, G, A, B) //1 2 ♭3 4 5 6 7 8
}

object DescendingMelodicMinorScale extends Notes {
  val keys = IndexedSeq(C, D, E_F, F, G, A_F, B_F) // 1 2 ♭3 4 5 ♭6 ♭7 8
}

object HarmonicMinorScale extends Notes {
  val keys = IndexedSeq(C, D, E_F, F, G, A_F, B) // 2 1 2 2 1 3 1
}

object PentatonicScale extends Notes {
  val keys = IndexedSeq(C, D, E, G, A) // 2-2-3-2-3
  //  val keys = IndexedSeq(C, D, F, G, A) // 2-3-2-2-3: Ritsu
  //  val keys = IndexedSeq(C, E_F, F, G, B_F) // 3-2-2-3-2: Minyou
}

object InnAscendingScale extends Notes {
  val keys = IndexedSeq(C, D_F, F, G, B_F) // 1-4-2-3-3
}

object MiyakoBushiScale extends Notes {
  val keys = IndexedSeq(C, D_F, F, G, A_F) // 1-4-2-1-4
  //  val keys = IndexedSeq(C, E, F, A, B) // 4-1-4-2-1 MinorYonaNuki
}

object RyukyuScale extends Notes {
  val keys = IndexedSeq(C, E, F, G, B) // 4-1-2-3-1
}

object BluesScale extends Notes {
  val keys = IndexedSeq(C, E_F, F, G_F, G, B_F) // 3-2-1-1-3-2
}

// TODO: Whole tone scale
// TODO: Augmented scale
// TODO: Prometheus scale
// TODO: Tritone scale
// TODO: Octatonic scales