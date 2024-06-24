package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_06_09_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 6)
    val logTimeOffset = autoArmedTimes(4) - props.armTime

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
      TextMessage("PIDs were clearly too low.",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(1, 8),
      TextMessage("Pitch and roll rate P&I now increased from 0.1 to 0.2.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(1, 8),

      TextMessage("This was clearly better.",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(21, 30),
      TextMessage("Not quite 'Basically Flyable (c)', but close to that.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(21, 30),
      TextMessage("Yaw drifted because the compass readings were poor.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2.5f, HA.Center, VA.Center)
        .enabledBetween(24, 30),
      TextMessage("Let's try it one more time,",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 4f, HA.Center, VA.Center)
        .enabledBetween(27, 30),
      TextMessage("to get more data.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 5f, HA.Center, VA.Center)
        .enabledBetween(27, 30),

      TextMessage("Definitely needs some tuning",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(48, 57),
      TextMessage("before I let it fall",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(48, 57),
      TextMessage("and land autonomously.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(48, 57),
      TextMessage("But it is nice that it flies.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 3.5f, HA.Center, VA.Center)
        .enabledBetween(53, 57),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 57.8, timeOff = 58.3),
    )

    props.runVideo(allGraphics)
  end apply

