package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_01_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 2)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val stickW = width / 2
    val stickSize = 201 * stickW / 1920
    val stickGap = 21 * stickW / 1920
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickW - stickSize - stickGap, stickW + stickGap, stickGap)

    val rpGap = 11 * width / 1280
    val rpWidth = width / 6
    val rpHeight = height / 7
    val fontSize = 16f * width / 1280
    val rpBackground = new Color(255, 255, 255, 120)

    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", -1, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 3)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", -1, 20,
      rpGap * 2 + rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 3)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 3, 4,
      width - 2 * rpGap - 2 * rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 3)
    val flapPlot = Plot.createXFlapPlot(reader, logTimeOffset,
      width - rpGap - rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 3)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      sticks, rollPlot, pitchPlot, yawPlot, flapPlot,
      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 46.5, timeOff = 47.0),
    )

    props.runVideo(allGraphics)
  end apply
