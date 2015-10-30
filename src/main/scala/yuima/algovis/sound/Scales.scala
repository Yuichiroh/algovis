package yuima.algovis.sound

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/18.
  */
object ScaleType extends Enumeration {
  val
  Chromatic,
  Diatonic,
  AscendingMelodicMinor,
  DescendingMelodicMinor,
  HarmonicMinor,
  Blues,
  Pentatonic,
  InnAscending,
  MiyakoBushi,
  Ryukyu
  = Value
}

object Scales {
  val types = Seq(
    ChromaticScale,
    DiatonicScale,
    AscendingMelodicMinorScale,
    DescendingMelodicMinorScale,
    HarmonicMinorScale,
    BluesScale,
    PentatonicScale,
    InnAscendingScale,
    MiyakoBushiScale,
    RyukyuScale
  )
}
