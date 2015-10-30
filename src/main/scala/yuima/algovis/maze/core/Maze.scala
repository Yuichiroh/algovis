package yuima.algovis.maze.core

import yuima.algovis.maze.core.Direction._
import yuima.algovis.maze.core.FieldType._
import yuima.algovis.maze.ui.Visualizer

/** A maze using automatic generation methods.
  *
  * @author Yuichiroh Matsubayashi
  *         Created on 15/08/20.
  */
case class Maze(width: Int, height: Int) {
  val start = Passage(1, 1, 0)
  val maxMove = width * height

  private[core] val fields = Array.fill(width * 2 + 1) {
    Array.fill(height * 2 + 1)(WALL)
  }

  def onSearch(vis: Option[Visualizer]) = new MazeOnSearch(this, vis)

  def onGeneration(vis: Option[Visualizer]) = new MazeOnGeneration(this, vis)

  def cloneFields = fields.map(_.clone())

  override def toString = fields.transpose.map {
    _.map {
      case PASSAGE   => "□"
      case WALL      => "■"
      case SEARCHED  => "×"
      case LEAF      => "*"
      case SEARCHING => "@"
      case BEST      => "+"
      case START     => "☆"
      case GOAL      => "◎"
    }.mkString(" ")
  }.mkString("\n")

  def reset() = {
    for {
      x <- 0 until width * 2 + 1
      y <- 0 until height * 2 + 1
      if fields(x)(y) != WALL && fields(x)(y) != START && fields(x)(y) != GOAL
    } fields(x)(y) = PASSAGE
  }

  class Position(val x: Int, val y: Int) {
    def euclideanDistanceTo[P <: Maze#Position](p: P) = math.sqrt(math.pow(x.toDouble - p.x, 2) + math.pow(y.toDouble - p.y, 2))

    override def equals(o: Any) = o match {
      case that: Position => (this.x, this.y) equals (that.x, that.y)
      case _              => false
    }
  }

  case class Passage(override val x: Int, override val y: Int, distanceTravelled: Int) extends Position(x, y)

  case class Door(override val x: Int, override val y: Int, distanceTravelled: Int, dir: Direction.Value) extends Position(x, y) with Ordered[Maze#Door] {

    import scala.math.Ordered.orderingToOrdered

    def compare(that: Maze#Door): Int = (this.distanceTravelled, this.x, this.y) compare (that.distanceTravelled, that.x, that.y)
  }

  case object Position {
    val adjacent = (ft: FieldType.Value) => (x: Int, y: Int) =>
      List(left(x, y), right(x, y), up(x, y), down(x, y))
        .collect { case Some(triple@(x2, y2, dir)) if fields(x2)(y2) == ft => triple }
    val opposite = (ft: FieldType.Value) => (x: Int, y: Int, dir: Direction.Value) => {
      val opposite = dir match {
        case LEFT  => left(x, y)
        case RIGHT => right(x, y)
        case UP    => up(x, y)
        case DOWN  => down(x, y)
      }
      opposite match {
        case Some((x2, y2, _)) if fields(x2)(y2) == ft => Some(x2, y2)
        case _                                         => None
      }
    }

    def apply(x: Int, y: Int) = new Position(x, y)

    def left(x: Int, y: Int) = if (x - 1 > 0) Some(x - 1, y, LEFT) else None

    def right(x: Int, y: Int) = if (x < width * 2) Some(x + 1, y, RIGHT) else None

    def up(x: Int, y: Int) = if (y - 1 > 0) Some(x, y - 1, UP) else None

    def down(x: Int, y: Int) = if (y < height * 2) Some(x, y + 1, DOWN) else None
  }

}