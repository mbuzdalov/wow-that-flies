package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_05_10_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 2)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val hWidth = width / 2
    val stickSize = 61 * width / 1280
    val stickGap = 5 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      hWidth - stickSize - stickGap / 2, hWidth + stickGap / 2, stickGap)

    val rpWidth = width / 7
    val rpHeight = height / 4
    val rpGap = 5 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 15,
      rpGap, height - 3 * (rpHeight + rpGap), rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 15,
      rpGap, height - 2 * (rpHeight + rpGap), rpWidth, rpHeight, fontSize, rpBackground, 2)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 1, 2,
      rpGap, height - (rpHeight + rpGap), rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(240, 240, 0), new Color(0, 0, 0, 120))

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot, yawPlot,
      TextMessage("May 10, 2024. Pre-checks for flying FPV.",
        msgCF, width * 0.5f, height * 0.15f, HA.Center, VA.Center)
        .enabledBetween(1, 4),
      TextMessage("No changes to the vehicle were made since the last test.",
        msgCF, width * 0.5f, height * 0.15f, HA.Center, VA.Center)
        .enabledBetween(15, 21),
      TextMessage("Not even battery charging.",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(18, 21),
      TextMessage("Well, it flies nicely enough",
        msgCF, width * 0.5f, height * 0.15f, HA.Center, VA.Center)
        .enabledBetween(46, 49),
      TextMessage("in current conditions.",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(46, 49),

      Fade(timeOn = 49, timeOff = 49.5),
    )

    props.runVideo(allGraphics)
  end apply
  