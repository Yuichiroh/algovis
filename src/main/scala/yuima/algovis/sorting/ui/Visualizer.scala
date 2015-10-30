package yuima.algovis.sorting.ui

import yuima.algovis.sorting.core.Instrument

import scala.collection.mutable.Queue
import scalafx.animation.AnimationTimer
import scalafx.beans.binding.NumberBinding
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

/** Created by yuichiroh on 15/08/16.
  */
case class Visualizer(var currentSeq: IndexedSeq[Int],
                      params: Parameters,
                      scene0: Scene)
  extends Pane {

  maxWidth(scene0.width.value * 4 / 5)
  styleClass.add("vis")

  lazy val animation = createAnimation

  val instrument = Instrument(params)
  val uiChangeQueue = Queue[UIChange]()
  var w: NumberBinding = _
  var h: NumberBinding = _
  var elements: Array[Rectangle] = _
  var inAction = false
  var startTime = -1L
  var prevSetTime = -1L
  var prevActionTime = System.nanoTime()
  var actionTimeSum = 0L

  init(currentSeq)

  def createAnimation = AnimationTimer((now: Long) => {
    actionTimeSum = 0
    val speed = (1000000 / math.pow(10, params.speed.value)).toInt

    val actions =
      Iterator.continually {
        if (uiChangeQueue.nonEmpty && (now - prevActionTime) / speed > uiChangeQueue.head.time + actionTimeSum) {
          val action = uiChangeQueue.dequeue()
          actionTimeSum += action.time
          Some(action)
        }
        else None
      }.takeWhile(_.isDefined).map(_.get)

    if (actions.hasNext) {
      resetColor(currentSeq)
      instrument.exceededNotesOff()
      actions.foreach(_.action())
      prevActionTime = System.nanoTime()
    }
    else instrument.exceededNotesOff()

    if (uiChangeQueue.isEmpty) {
      resetColor(currentSeq)
      instrument.reduceNotes()
    }
  })

  def resetColor(seq: IndexedSeq[Int]) = elements.indices.foreach { i =>
    elements(i).fill = color(seq, i)
  }

  def init(seq: IndexedSeq[Int]) = {
    w = scene0.width * 4 / 5 / seq.length
    h = scene0.height / seq.length
    elements = seq.indices.map { i =>
      new Rectangle {
        width <== w
        height <== h * (seq(i) + 1)
        layoutX <== w * i
        layoutY <== h * (seq.length - seq(i) - 1)
        fill = color(seq, i)
      }
    }.toArray
    val childrenA = 0
    children = elements
    currentSeq = seq
  }

  def color(seq: IndexedSeq[Int], index: Int) = Color.rgb(0,
                                                          (255 * (2 * seq(index).toDouble / (seq.length - 1))).toInt min 255,
                                                          (255 * (2 * (seq.length - seq(index) - 1).toDouble / (seq.length - 1))).toInt min 255)

  def focus(arr: IndexedSeq[Int], index: Int) = {
    if (prevSetTime < 0) prevSetTime = System.nanoTime()
    uiAction(System.nanoTime() - prevSetTime) {
      elements(index).fill = Color.Red
      instrument.noteOn(arr(index), 100)
      currentSeq = arr
    }
    prevSetTime = System.nanoTime()
  }

  def update(arr: IndexedSeq[Int], index: Int, value: Int) = {
    if (prevSetTime < 0) prevSetTime = System.nanoTime()
    uiAction(System.nanoTime() - prevSetTime) {
      elements(index).height <== h * (value + 1)
      elements(index).layoutX <== w * index
      elements(index).layoutY <== h * (currentSeq.length - value - 1)
      elements(index).fill = Color.Red
      instrument.noteOn(value, 100)
      currentSeq = arr
    }
    prevSetTime = System.nanoTime()
  }

  def uiAction(time: Long)(op: => Unit) = uiChangeQueue.enqueue(new UIChange(() => { op }, time))
}

case class UIChange(action: () => Unit, time: Long)
