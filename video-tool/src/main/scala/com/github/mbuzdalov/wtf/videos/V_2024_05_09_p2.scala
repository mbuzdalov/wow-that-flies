package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_05_09_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 3)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

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
      TextMessage("May 09, 2024.",
        msgCF, width * 0.5f, height * 0.15f,
        HA.Center, VA.Center, 1, 8),
      TextMessage("The main objective is VTX commissioning.",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 1f,
        HA.Center, VA.Center, 2, 8),
      TextMessage("If lucky enough, I wanted to fly FPV.",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 2f,
        HA.Center, VA.Center, 2, 8),
      TextMessage("It was difficult to find a place for a test,",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 3.5f,
        HA.Center, VA.Center, 5, 8),
      TextMessage("so some castle ruins were selected.",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 4.5f,
        HA.Center, VA.Center, 5, 8),
      TextMessage("It is actually quite windy,",
        msgCF, width * 0.5f, height * 0.3f,
        HA.Center, VA.Center, 12, 16),
      TextMessage("so it's hard to even hover.",
        msgCF, width * 0.5f, height * 0.3f + msgStep * 1f,
        HA.Center, VA.Center, 12, 16),
      TextMessage("The camera work is not perfect,",
        msgCF, width * 0.5f, height * 0.2f,
        HA.Center, VA.Center, 20, 26),
      TextMessage("including the jelly HDZero feed,",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 1f,
        HA.Center, VA.Center, 20, 26),
      TextMessage("as the camera is mounted nearly on the motor.",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 2f,
        HA.Center, VA.Center, 20, 26),
      TextMessage("Interesting: yaw is quite sharp,",
        msgCF, width * 0.5f, height * 0.2f,
        HA.Center, VA.Center, 29, 34),
      TextMessage("but motors struggle (difference too high)...",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 1f,
        HA.Center, VA.Center, 29, 34),
      TextMessage("I decided not to risk flying FPV,",
        msgCF, width * 0.5f, height * 0.25f,
        HA.Center, VA.Center, 41, 48),
      TextMessage("but instead to perform more flights",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1f,
        HA.Center, VA.Center, 41, 48),
      TextMessage("to get more data about performance in the wind.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2f,
        HA.Center, VA.Center, 41, 48),
      TextMessage("The HDZero system also raises the center of gravity",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 3f,
        HA.Center, VA.Center, 45, 48),
      TextMessage("less than GPS+compass, so CX7 flies worse.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 4f,
        HA.Center, VA.Center, 45, 48),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 48.5, timeOff = 49),
    )

    props.runVideo(allGraphics)
  end apply
  