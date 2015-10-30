package yuima.algovis.sorting.ui

import java.text.NumberFormat

import com.typesafe.scalalogging.LazyLogging
import yuima.algovis.sorting.core.ArrayWithVis
import yuima.algovis.sorting.algorithm._
import yuima.algovis.sound.{Scales, Notes, Instruments}

import scala.util.Random
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, ComboBox, RadioButton, Slider, TextField, TextFormatter, ToggleGroup}
import scalafx.scene.layout.{HBox, Priority, VBox}
import scalafx.util.converter.FormatStringConverter

/** Created by yuichiroh on 15/08/17.
  */
class Controller(params: Parameters, vis: Visualizer) extends VBox {
  styleClass.add("ctrl")

  vgrow = Priority.Always
  hgrow = Priority.Always
  spacing = 10
  padding = Insets(20)
  val numberFormat = NumberFormat.getNumberInstance
  val converter = new FormatStringConverter[Number](numberFormat)

  val algorithmTog = new ToggleGroup
  val algorithms = new VBox {
    vgrow = Priority.Always
    hgrow = Priority.Always
    spacing = 10
    padding = Insets(20)

    children = Seq(
      createButton("selection", SelectionSort, select = true),
      createButton("insertion", InsertionSort),
      createButton("gnome", GnomeSort),
      createButton("shell", ShellSort),
      createButton("bubble", BubbleSort),
      createButton("cocktail", CocktailSort),
      createButton("comb", CombSort),
      createButton("merge", MergeSort),
      createButton("quick", QuickSort),
      createButton("heap", HeapSort)
    )
    // TODO: Introsort, Timsort >> Smoothsort, Block sort
  }

  val instrument = new ComboBox[String] {
    items = ObservableBuffer(Instruments.types: _*)
    value <==> params.sound
  }

  val scale = new ComboBox[Notes] {
    items = ObservableBuffer(Scales.types: _*)
    value <==> params.notes
    onAction = handle { reset() }
  }

  val n = new TextField {
    editable = false
    text = params.notes.value.size.toString
  }

  val speed = new Slider {
    min = 0
    max = 6
    blockIncrement = 0.1
    value <==> params.speed
  }

  val speedText = new TextField {
    editable = false
    textFormatter = new TextFormatter(converter) {
      value <==> params.speed
    }
  }

  val playButton = new Button {
    text = "▶"
    onAction = handle {
      if (vis.inAction) {
        vis.animation.stop()
        vis.inAction = false
        text = "▶"
      }
      else {
        System.gc()
        vis.inAction = true
        text = "||"
        vis.prevActionTime = System.nanoTime()
        vis.animation.start()
      }
    }
  }

  val resetButton = new Button {
    text = "RESET"
    onAction = handle { reset() }
  }

  val buttons = new HBox {
    children = Seq(resetButton, playButton)
  }

  children = Seq(n, speed, speedText, buttons, instrument, scale, algorithms)

  def createButton(name: String, op: collection.mutable.IndexedSeq[Int] => Unit, select: Boolean = false) =
    new RadioButton {
      toggleGroup = algorithmTog
      text = name
      selected = select
      userData = op
      onAction = handle { changeAlgorithm() }
    }

  def changeAlgorithm() = {
    if (vis.inAction) {
      reset()
      System.gc()
      vis.inAction = true
      playButton.text = "||"
      vis.prevActionTime = System.nanoTime()
      vis.animation.start()
    }
    else reset()
  }

  def reset() = {
    (0 to 5).foreach { _ =>
      vis.animation.stop()
      vis.inAction = false
      vis.instrument.allNotesOff()
      n.text = params.notes.value.size.toString
      playButton.text = "▶"
      System.gc()
      vis.uiChangeQueue.clear()
      vis.prevSetTime = -1L
      val arr = Random.shuffle(0 to params.notes.value.length - 1).toArray
      val arrVis = ArrayWithVis(arr, vis)
      vis.init(arr.toIndexedSeq)
      algorithmTog.selectedToggle.value.getUserData.asInstanceOf[IndexedSeq[Int] => Unit](arrVis)
    }
  }
}
