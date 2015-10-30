package yuima.algovis.maze.ui

import yuima.algovis.maze.core.{Maze, Parameters}
import yuima.algovis.maze.gen.{DepthFirst, Kruskal, Prim}
import yuima.algovis.maze.search.{A_StarSearch, BreadthFirstSearch, DepthFirstSearch, IDDFS}

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color
import scalafx.stage.Screen

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/21.
  */
object MazeSearchVis extends JFXApp {
  val params = Parameters()
  warmUp()

  val bounds = Screen.primary.visualBounds
  val scene0 = new Scene(bounds.width, bounds.height - 25) {
    fill = Color.Transparent
  }
  val vis = Visualizer(params, scene0)
  val controller = new Controller(params, vis)

  val layout = new BorderPane {
    center = vis
    right = controller
  }
  scene0.content = layout

  stage = new PrimaryStage {
    title = "Maze Search Visualizer"
    scene = scene0
  }


  def warmUp() = {
    val gen = Seq(DepthFirst, Prim, Kruskal)
    val search = Seq(DepthFirstSearch, BreadthFirstSearch, IDDFS, A_StarSearch)

    for {
      _ <- 0 to 5
      g <- gen
      s <- search
    } {
      val maze = Maze(params.width.value, params.height.value)
      g(maze.onGeneration(None))
      s(maze.onSearch(None))
      System.gc()
    }
  }
}
