package yuima.algovis.sorting.ui

import yuima.algovis.sound.{Instruments, Notes, ScaleType, Scales}

import scalafx.beans.property.{DoubleProperty, ObjectProperty, StringProperty}

/** Created by yuichiroh on 15/08/19.
  */
case class Parameters(var speed: DoubleProperty = DoubleProperty(2),
                      var notes: ObjectProperty[Notes] = ObjectProperty(Scales.types(ScaleType.Diatonic.id)),
                      var sound: StringProperty = StringProperty(Instruments(0)))
