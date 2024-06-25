package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_06_09_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 6)
    val logTimeOffset = autoArmedTimes(3) - props.armTime

    val hWidth = width / 2
    val stickSize = 101 * width / 1280
    val stickGap = 11 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      hWidth - stickSize - stickGap, hWidth + stickGap, stickGap)

    val rpWidth = (width / 6.5).toInt
    val rpHeight = height / 6
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)

    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", -1, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", -1, 20,
      rpGap * 2 + rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val servoPlot = Plot.createXFlapPlot(reader, logTimeOffset,
      width - 2 * rpGap - 2 * rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 3, 4,
      width - rpGap - rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 50, 10), new Color(255, 255, 255, 120))

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot, servoPlot, yawPlot,
      TextMessage("June 09, 2024.",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(1, 8),
      TextMessage("Remember the April 12th video?",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(2, 8),
      TextMessage("SN1 is now alive.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2.5f, HA.Center, VA.Center)
        .enabledBetween(4, 8),
      TextMessage("This is its first test session.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 3.5f, HA.Center, VA.Center)
        .enabledBetween(5, 8),

      Fade(timeOn = 18, timeOff = 18.5),
    )

    props.runVideo(allGraphics)
  end apply
