package yuima.algovis.sorting.ui

import scala.util.Random
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color
import scalafx.stage.Screen

/**
 * @author Yuichiroh Matsubayashi
 *         Created by yuichiroh on 15/08/16.
 */
object SortVis extends JFXApp {
  val params = Parameters()
  val arr = Random.shuffle(0 to params.notes.value.length - 1).toArray

  val bounds = Screen.primary.visualBounds
  val scene0 = new Scene(bounds.width, bounds.height - 25) {
    fill = Color.Transparent
    stylesheets.add(this.getClass.getClassLoader.getResource("style.css").toExternalForm)
  }

  val vis = Visualizer(arr, params, scene0)
  val controller = new Controller(params, vis)

  val layout = new BorderPane {
    center = vis
    right = controller
  }
  scene0.content = layout

  stage = new PrimaryStage {
    title = "Sorting Visualizer"
    scene = scene0
  }

  controller.reset()
}

