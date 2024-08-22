package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}
import com.github.mbuzdalov.wtf.util.{PiecewiseLinearFunction => PLW}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}

object V_2024_05_09_p3:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 3)
    val logTimeOffset = autoArmedTimes(1) - props.armTime

    val hWidth = width / 2
    val stickSize = 61 * width / 1280
    val stickGap = 5 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      hWidth - stickSize - stickGap / 2, hWidth + stickGap / 2, stickGap)

    val rpWidth = width / 7
    val rpHeight = height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 30,
      rpGap, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 30,
      width - rpWidth - rpGap, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 1, 2,
      hWidth - rpWidth / 2, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 120))

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot, yawPlot,
      TextMessage("May 09, 2024, test #2.",
        msgCF, width * 0.5f, height * 0.15f, HA.Center, VA.Center)
        .enabledBetween(1, 6),
      TextMessage("I wanted to get higher and move around more,",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(2, 6),
      TextMessage("but the wind decided otherwise.",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(2, 6),
      TextMessage("Watch for the yaw incident",
        msgCF, width * 0.5f, height * 0.15f, HA.Center, VA.Center)
        .enabledBetween(17, 23.5),
      TextMessage("in 3",
        msgCF, width * 0.5f - msgFontSize * 2.5f, height * 0.15f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(18.5, 0) to (18.51, 1) to (23, 1) to (23.5, 0)),
      TextMessage("2",
        msgCF, width * 0.5f - msgFontSize * 1f, height * 0.15f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(19.5, 0) to (19.51, 1) to (23, 1) to (23.5, 0)),
      TextMessage("1",
        msgCF, width * 0.5f - msgFontSize * 0f, height * 0.15f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(20.5, 0) to (20.51, 1) to (23, 1) to (23.5, 0)),
      TextMessage("NOW!",
        msgCF, width * 0.5f + msgFontSize * 2f, height * 0.15f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(21.5, 0) to (21.51, 1) to (23, 1) to (23.5, 0)),
      TextMessage("Things are about to go nuts...",
        msgCF, width * 0.5f, height * 0.45f, HA.Center, VA.Center)
        .enabledBetween(26, 28.6),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 33.5, timeOff = 34),
    )

    props.runVideo(allGraphics)
  end apply
  