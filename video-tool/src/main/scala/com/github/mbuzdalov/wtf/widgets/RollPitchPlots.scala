package com.github.mbuzdalov.wtf.widgets

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.{LogReader, Numbers, Plot}

object RollPitchPlots:
  def create(logReader: LogReader, timeOffset: Double, name: "Roll" | "Pitch", channel: Int, maxAngle: Double,
             x: Int, y: Int, width: Int, height: Int, fontSize: Float, background: Color, timeWidth: Double): Plot =
    val flapMin = logReader.getParameter(s"SERVO${channel}_MIN")
    val flapMax = logReader.getParameter(s"SERVO${channel}_MAX")
    new Plot(
      logReader, timeOffset, x, y,
      width, height, fontSize, background, 2,
      IndexedSeq(
        Plot.Source[Numbers.UInt16]("RCOU", s"C$channel", s"$name Flap", v => v.toDouble, flapMin, flapMax, Color.BLACK),
        Plot.Source[Float]("ATT", s"Des$name", s"Desired $name", v => v, -maxAngle, +maxAngle, Color.BLUE),
        Plot.Source[Float]("ATT", name, s"Actual $name", v => v, -maxAngle, +maxAngle, Color.RED),
      )
    )
end RollPitchPlots
