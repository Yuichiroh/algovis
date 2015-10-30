package yuima.algovis.maze.ui

import yuima.algovis.maze.core.FieldType._
import yuima.algovis.maze.core._

import scala.annotation.tailrec
import scala.collection.mutable
import scalafx.animation.AnimationTimer
import scalafx.beans.binding.NumberBinding
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/21.
  */
case class Visualizer(params: Parameters, scene0: Scene) extends Pane {

  maxWidth(scene0.width.value - Settings.controllerWidth)

  lazy val animation = createAnimation

  val instrument = Instrument(params, 64)
  val uiChangeQueue = mutable.Queue[UIChange]()

  var m: Maze = _
  var start: Maze#Position = _
  var fields: Array[Array[FieldType.Value]] = _
  var trace: Array[Array[Maze#Position]] = _
  var maxMove: Int = _
  var passageWidth: NumberBinding = _
  var elements: Array[Rectangle] = _
  var inAction = false
  var startTime = -1L
  var prevSetTime = -1L
  var prevActionTime = System.nanoTime()
  var actionTimeSum = 0L

  def init(maze: Maze) = {
    m = maze
    start = maze.start
    fields = maze.cloneFields
    trace = Array.fill(maze.width * 2 + 1) { Array.fill(maze.height * 2 + 1)(null) }
    maxMove = maze.maxMove

    passageWidth =
      if ((scene0.height / (maze.height * 2 + 1)).intValue() < ((scene0.width - Settings.controllerWidth) / (maze.width * 2 + 1)).intValue())
        scene0.height / (maze.height * 2 + 1)
      else
        (scene0.width - Settings.controllerWidth) / (maze.width * 2 + 1)


    elements = Array.ofDim[Rectangle]((maze.width * 2 + 1) * (maze.height * 2 + 1))

    for {
      i <- 0 until maze.width * 2 + 1
      j <- 0 until maze.height * 2 + 1
    } {
      elements(i * (maze.height * 2 + 1) + j) = new Rectangle {
        width <== passageWidth
        height <== passageWidth
        layoutX <== passageWidth * i
        layoutY <== passageWidth * j
        fill = color(fields(i)(j))
      }
    }
    children = elements
  }

  def generateCell(i: Int, j: Int, ft: FieldType.Value, euclideanToPrev: Double, moved: Int): Unit = {
    if (prevSetTime < 0) prevSetTime = System.nanoTime()
    uiAction((System.nanoTime() - prevSetTime) / 10) {
      fields(i)(j) = ft
      instrument.noteOn(rescaleNoteOfEuclideanDistance(euclideanToPrev), 100)
      instrument.noteOn(normalizeNoteOfArea(moved), 100)
    }
    prevSetTime = System.nanoTime()
  }

  def normalizeNoteOfArea(area: Int) = (area.toDouble / maxMove * params.notes.value.length).toInt

  def rescaleNoteOfEuclideanDistance(distance: Double) = {
    (distance / math.sqrt(math.pow(params.width.value * 2 + 1, 2) + math.pow(params.height.value * 2 + 1, 2)) * params.notes.value.length).toInt
  }

  def uiAction(time: Long)(op: => Unit) = uiChangeQueue.enqueue(new UIChange(() => {
    op
  }, time))

  def searchCell(i: Int, j: Int, dir: Direction.Value, leaves: Seq[Maze#Position],
                 euclideanToPrev: Double, euclideanToGoal: Double, distanceTravelled: Int, moved: Int): Unit = {
    if (prevSetTime < 0) prevSetTime = System.nanoTime()
    uiAction(System.nanoTime() - prevSetTime) {
      resetSearchingNode()
      resetLeaves(leaves)
      trace(i)(j) = prevPos(i, j, dir)
      if (m.Position(i, j) != start) fields(i)(j) = SEARCHING
      resetBestRoute(i, j)
      instrument.noteOn(rescaleNoteOfTravelledDistance(distanceTravelled), 100)
      instrument.noteOn(rescaleNoteOfEuclideanDistance(euclideanToGoal), 100)
      instrument.noteOn(rescaleNoteOfEuclideanDistance(euclideanToPrev), 100)
      instrument.noteOn(normalizeNoteOfArea(moved), 100)
    }
    prevSetTime = System.nanoTime()
  }

  def rescaleNoteOfTravelledDistance(distance: Double) =
    (distance / (params.width.value + params.height.value) * 15).toInt % (4 * params.notes.value.keys.length) + 2 * params.notes.value.keys.length

  def resetSearchingNode() = {
    for {
      i <- fields.indices
      j <- fields.head.indices
      if fields(i)(j) == SEARCHING
    } fields(i)(j) = LEAF
  }

  def resetLeaves(positions: Seq[Maze#Position]) = {
    for {
      i <- fields.indices
      j <- fields.head.indices
      if fields(i)(j) == LEAF
    } fields(i)(j) = SEARCHED
    positions.foreach(p => fields(p.x)(p.y) = LEAF)
  }

  def prevPos(x: Int, y: Int, dir: Direction.Value) = {
    import Direction._
    dir match {
      case LEFT => m.Position(x + 1, y)
      case RIGHT => m.Position(x - 1, y)
      case UP => m.Position(x, y + 1)
      case DOWN => m.Position(x, y - 1)
    }
  }

  def searchCell(i: Int, j: Int, dir: Direction.Value, leaves: Seq[Maze#Position]): Unit = {
    if (prevSetTime < 0) prevSetTime = System.nanoTime()
    uiAction(System.nanoTime() - prevSetTime) {
      resetSearchingNode()
      resetLeaves(leaves)
      trace(i)(j) = prevPos(i, j, dir)
      if (m.Position(i, j) != start) fields(i)(j) = SEARCHING
      resetBestRoute(i, j)
    }
    prevSetTime = System.nanoTime()
  }

  def resetBestRoute(x: Int, y: Int) = {
    for {
      i <- fields.indices
      j <- fields.head.indices
      if fields(i)(j) == BEST
    } fields(i)(j) = SEARCHED
    setBestRoute(x, y)
  }

  @tailrec
  private def setBestRoute(x: Int, y: Int): Unit = {
    val prev = trace(x)(y)
    if (prev != start) {
      fields(prev.x)(prev.y) = BEST
      setBestRoute(prev.x, prev.y)
    }
  }

  private def createAnimation = AnimationTimer((now: Long) => {
    actionTimeSum = 0
    val speed = (100000 / math.pow(10, params.speed.value)).toInt

    val actions = Iterator.continually {
      if (uiChangeQueue.nonEmpty && (now - prevActionTime) / speed > uiChangeQueue.head.time + actionTimeSum) {
        val action = uiChangeQueue.dequeue()
        actionTimeSum += action.time
        Some(action)
      }
      else None
    }.takeWhile(_.isDefined).map(_.get)

    if (actions.hasNext) {
      instrument.exceededNotesOff()
      actions.foreach(_.action())
      resetColors()
      prevActionTime = System.nanoTime()
    }
    else instrument.exceededNotesOff()

    if (uiChangeQueue.isEmpty) {
      resetColors()
      instrument.reduceNotes()
    }
  })

  def resetColors() = {
    for {
      i <- fields.indices
      j <- fields.head.indices
    } elements(i * fields.head.length + j).fill = color(fields(i)(j))
  }

  def color(fieldType: FieldType.Value) = fieldType match {
    case WALL => Color.Black
    case PASSAGE => Color.White
    case SEARCHED => Color.Gray
    case BEST => Color.Yellow
    case LEAF => Color.Orange
    case SEARCHING => Color.Pink
    case START => Color.Lime
    case GOAL => Color.Red
  }
}

case class UIChange(action: () => Unit, time: Long)

