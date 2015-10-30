package yuima.algovis.sorting.core

import javax.sound.midi.{MidiSystem, ShortMessage}

import yuima.algovis.sorting.ui.Parameters
import yuima.algovis.sound.Instruments

import scala.collection.mutable.Queue

/** Created by yuichiroh on 15/08/19.
  */
case class Instrument(params: Parameters, maxNotes: Int = 64) {
  val devices = MidiSystem.getMidiDeviceInfo
  val midi = MidiSystem.getReceiver
  val activeNotes = Queue[Int]()

  changeSound()

  params.sound.onChange(changeSound())

  params.notes.onChange(changeScale())

  def noteOn(index: Int, velocity: Int) = {
    activeNotes.enqueue(index)
    val msg = new ShortMessage()
    msg.setMessage(ShortMessage.NOTE_ON, params.notes.value(index), velocity)
    midi.send(msg, -1)
  }

  def exceededNotesOff(): Unit =
    if (activeNotes.nonEmpty)
      Iterator.continually(activeNotes.dequeue()).takeWhile(_ => activeNotes.size > maxNotes).toSeq.foreach { index =>
        noteOff(index, 0)
      }

  def reduceNotes(): Unit = if (activeNotes.nonEmpty) noteOff(activeNotes.dequeue(), 0)

  private def noteOff(index: Int, velocity: Int) = {
    val msg = new ShortMessage()
    msg.setMessage(ShortMessage.NOTE_OFF, params.notes.value(index), velocity)
    midi.send(msg, -1)
  }

  private def changeSound() = {
    val msg = new ShortMessage()
    allNotesOff()
    msg.setMessage(ShortMessage.PROGRAM_CHANGE, Instruments.indexOf(params.sound.value), 0)
    midi.send(msg, -1)
  }

  def allNotesOff(): Unit = {
    activeNotes.clear()
    (0 until params.notes.value.size).foreach { i => noteOff(i, 0) }
  }

  private def changeScale() = {
    activeNotes.clear()
    (0 to 127).foreach(rawNoteOff(_, 0))
  }

  private def rawNoteOff(index: Int, velocity: Int) = {
    val msg = new ShortMessage()
    msg.setMessage(ShortMessage.NOTE_OFF, index, velocity)
    midi.send(msg, -1)
  }
}
