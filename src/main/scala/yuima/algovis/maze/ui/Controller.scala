package yuima.algovis.maze.ui

import java.text.{DecimalFormat, NumberFormat}

import yuima.algovis.maze.core._
import yuima.algovis.maze.gen.{DepthFirst, GenerationAlgorithm, Kruskal, Prim}
import yuima.algovis.maze.search._
import yuima.algovis.sound.{Instruments, Notes, Scales}

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, Priority, VBox}
import scalafx.util.converter.FormatStringConverter

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/21.
  */
class Controller(params: Parameters, vis: Visualizer) extends VBox {
  vgrow = Priority.Always
  hgrow = Priority.Always
  spacing = 10
  padding = Insets(20)

  minWidth(Settings.controllerWidth)

  params.searchAlgorithm.onChange(withAction(reset()))
  params.generationAlgorithm.onChange(withAction(generate()))

  val decimal = new DecimalFormat("###")
  val number = NumberFormat.getNumberInstance
  val decimalConverter = new FormatStringConverter[Number](decimal)
  val numConverter = new FormatStringConverter[Number](number)
  val mazeWidth = new HBox {
    val label = new Label {
      text = "width: "
    }
    val size = new TextField {
      textFormatter = new TextFormatter(decimalConverter) {
        value <==> params.width
      }
      onAction = handle { withAction(generate()) }
    }
    children = Seq(label, size)
  }
  val mazeHeight = new HBox {
    val label = new Label {
      text = "height:"
    }

    val size = new TextField {
      textFormatter = new TextFormatter(numConverter) {
        value <==> params.height
      }
      onAction = handle { withAction(generate()) }
    }
    children = Seq(label, size)
  }
  val speed = new HBox {
    val label = new Label {
      text = "speed: × 1,000,000 / 10^k"
    }
    val slider = new Slider {
      min = 0
      max = 5
      blockIncrement = 0.1
      value <==> params.speed
    }
    children = Seq(label, slider)
  }
  val speedText = new TextField {
    editable = false
    textFormatter = new TextFormatter(numConverter) {
      value <==> params.speed
    }
  }
  val generationAlgorithms = new HBox {
    val label = new Label {
      text = "generation:"
    }
    val box = new ComboBox[GenerationAlgorithm] {
      items = ObservableBuffer(DepthFirst,
                               Prim,
                               Kruskal)
      value <==> params.generationAlgorithm
    }
    val check = new CheckBox {
      text = "visualize"
      selected <==> params.visualizeGeneration
    }
    children = Seq(label, box, check)
    alignment = Pos.BaselineCenter
    spacing = 10
  }
  val searchAlgorithms = new HBox {
    val label = new Label {
      text = "search:"
    }
    val box = new ComboBox[SearchAlgorithm] {
      items = ObservableBuffer(DepthFirstSearch,
                               BreadthFirstSearch,
                               IDDFS,
                               A_StarSearch)
      value <==> params.searchAlgorithm
    }
    children = Seq(label, box)
    alignment = Pos.BaselineCenter
    spacing = 10
  }
  val generationButton = new Button {
    text = "GENERATE"
    onAction = handle { withAction(generate()) }
  }
  val resetButton = new Button {
    text = "RESET"
    onAction = handle { withAction(reset()) }
  }
  val searchButton = new Button {
    text = "START"
    onAction = handle {
      if (vis.inAction) {
        text = "START"
        vis.inAction = false
        vis.animation.stop()
      }
      else {
        text = "STOP"
        vis.inAction = true
        vis.prevActionTime = System.nanoTime()
        vis.animation.start()
      }
    }
  }
  val buttons = new HBox {
    children = Seq(generationButton, resetButton, searchButton)
  }
  val instrument = new ComboBox[String] {
    items = ObservableBuffer(Instruments.types: _*)
    value <==> params.sound
  }
  val scale = new ComboBox[Notes] {
    items = ObservableBuffer(Scales.types: _*)
    value <==> params.notes
    onAction = handle { withAction(reset()) }
  }
  var maze: Maze = _

  children = Seq(mazeWidth, mazeHeight,
                 speed, speedText,
                 generationAlgorithms, searchAlgorithms,
                 buttons,
                 instrument, scale)
  generate()

  def reset() = initWith {
    maze.reset()
    vis.init(maze)
  }

  def withAction(op: => Unit) = {
    if (vis.inAction) {
      op
      searchButton.text = "STOP"
      vis.inAction = true
      vis.prevActionTime = System.nanoTime()
      vis.animation.start()
    }
    else op
  }

  def generate() = initWith {
    maze = mazeWithParams
    if (params.visualizeGeneration.value) {
      vis.init(maze)
      System.gc()
      vis.prevSetTime = -1L
      params.generationAlgorithm.value(maze.onGeneration(Some(vis)))
    }
    else {
      System.gc()
      vis.prevSetTime = -1L
      params.generationAlgorithm.value(maze.onGeneration(None))
      vis.init(maze)
    }
  }

  def initWith(op: => Unit) = {
    vis.animation.stop()
    searchButton.text = "START"
    vis.inAction = false
    vis.instrument.allNotesOffSurely()

    vis.uiChangeQueue.clear()
    op
    System.gc()
    vis.prevSetTime = -1L
    params.searchAlgorithm.value(MazeOnSearch(maze, vis))
  }

  def mazeWithParams: Maze = Maze(params.width.value, params.height.value)
}
