package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_11_27:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val stickW = width / 2
    val stickSize = 201 * stickW / 1920
    val stickGap = 21 * stickW / 1920
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickW - stickSize - stickGap, stickW + stickGap, stickGap)

    val rpGap = 11 * width / 1280
    val rpWidth = (width - 5 * rpGap) / 4
    val rpHeight = (height * (1 - 1280.0 / 1920) - 2 * rpGap).toInt
    val fontSize = 16f * width / 1280
    val rpBackground = Color.BLACK

    val bottomPlotH = height - rpHeight - rpGap

    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", -1, 50,
      rpGap, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2, 4)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", -1, 50,
      rpGap * 2 + rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2, 4)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 3, 4,
      rpGap * 3 + 2 * rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val flapPlot = Plot.createXFlapPlot(reader, logTimeOffset,
      rpGap * 4 + 3 * rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      sticks.whenTopLeftBlack,
      rollPlot.whenTopLeftBlack,
      pitchPlot.whenTopLeftBlack,
      yawPlot.whenTopLeftBlack,
      flapPlot.whenTopLeftBlack,

      LeftBlanket(0.38f, new Color(240, 240, 240, 200), 1.5, 1.9, 12.1, 12.5),
      TextMessage("November 27th.",
        msgCF, width * 0.02, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(2, 12),
      TextMessage("This was another data flight",
        msgCF, width * 0.02, height * 0.06 + msgStep * 1.5, HA.Left, VA.Center)
        .enabledBetween(3, 12),
      TextMessage("where I would try falling from",
        msgCF, width * 0.02, height * 0.06 + msgStep * 2.5, HA.Left, VA.Center)
        .enabledBetween(3, 12),
      TextMessage("the sky at different pitches and",
        msgCF, width * 0.02, height * 0.06 + msgStep * 3.5, HA.Left, VA.Center)
        .enabledBetween(3, 12),
      TextMessage("throttles to get some ideas.",
        msgCF, width * 0.02, height * 0.06 + msgStep * 4.5, HA.Left, VA.Center)
        .enabledBetween(3, 12),
      TextMessage("I could not represent this",
        msgCF, width * 0.02, height * 0.06 + msgStep * 6, HA.Left, VA.Center)
        .enabledBetween(7, 12),
      TextMessage("nicely in a video, so simply",
        msgCF, width * 0.02, height * 0.06 + msgStep * 7, HA.Left, VA.Center)
        .enabledBetween(7, 12),
      TextMessage("enjoy the footage :)",
        msgCF, width * 0.02, height * 0.06 + msgStep * 8, HA.Left, VA.Center)
        .enabledBetween(7, 12),


      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 214.5, timeOff = 215.0),
    )

    props.runVideo(allGraphics)
  end apply
