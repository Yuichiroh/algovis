package yuima.algovis.maze.core

import yuima.algovis.maze.gen.{GenerationAlgorithm, Prim}
import yuima.algovis.maze.search.{DepthFirstSearch, SearchAlgorithm}
import yuima.algovis.sound.{Instruments, Notes, ScaleType, Scales}

import scalafx.beans.property.{BooleanProperty, DoubleProperty, IntegerProperty, ObjectProperty, StringProperty}

/** @author Yuichiroh Matsubayashi
  *         Created on 15/08/21.
  */
case class Parameters(width: IntegerProperty = IntegerProperty(60),
                      height: IntegerProperty = IntegerProperty(40),
                      speed: DoubleProperty = DoubleProperty(1.5),
                      visualizeGeneration: BooleanProperty = BooleanProperty(true),
                      generationAlgorithm: ObjectProperty[GenerationAlgorithm] = ObjectProperty(Prim),
                      searchAlgorithm: ObjectProperty[SearchAlgorithm] = ObjectProperty(DepthFirstSearch),
                      notes: ObjectProperty[Notes] = ObjectProperty(Scales.types(ScaleType.Diatonic.id)),
                      sound: StringProperty = StringProperty(Instruments(0)))

