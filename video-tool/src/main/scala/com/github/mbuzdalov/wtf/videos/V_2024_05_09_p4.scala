package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_05_09_p4:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 3)
    val logTimeOffset = autoArmedTimes(2) - props.armTime

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
      TextMessage("May 09, 2024, test #3.",
        msgCF, width * 0.5f, height * 0.15f,
        HA.Center, VA.Center, 1, 6),
      TextMessage("CX7 was quite fine, and it is disappointing",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 1f,
        HA.Center, VA.Center, 2, 6),
      TextMessage("to end a test session with a crash.",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 2f,
        HA.Center, VA.Center, 2, 6),
      TextMessage("So I performed another test.",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 3f,
        HA.Center, VA.Center, 4, 6),
      TextMessage("Nothing new in this test:",
        msgCF, width * 0.5f, height * 0.15f,
        HA.Center, VA.Center, 18, 24.5),
      TextMessage("occasional flap saturation in roll",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 1f,
        HA.Center, VA.Center, 18, 24.5),
      TextMessage("due to the wind.",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 2f,
        HA.Center, VA.Center, 18, 24.5),
      TextMessage("But maybe in less windy conditions",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 3.5f,
        HA.Center, VA.Center, 21, 24.5),
      TextMessage("it would fly just OK?...",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 4.5f,
        HA.Center, VA.Center, 21, 24.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 24.5, timeOff = 25),
    )

    props.runVideo(allGraphics)
  end apply
  