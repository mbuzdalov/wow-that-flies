package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.*
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}

object V_2024_03_29_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val hWidth = width / 2
    val stickSize = 71 * width / 1280
    val stickGap = 7 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      width - 2 * stickSize - 2 * stickGap, width - stickSize - stickGap, height - stickSize - stickGap)

    val rpWidth = width / 4
    val rpHeight = height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 20,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(10, 10, 50)
    val msgCF = TextMessage.ColorFont(msgFontSize, msgColor)

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("March 29, 2024, test 2 (actually 4)",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(1, 10),
      TextMessage("To reduce maximum angle accelerations, I changed",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1.5f, HA.Center, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("ATC_ACCEL_{R,P}_MAX from 220000 to 40000.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2.5f, HA.Center, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("The previous values were recommended defaults for a 5-inch quadcopter.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 4.0f, HA.Center, VA.Center)
        .enabledBetween(5, 10),
      TextMessage("40000 = 400 degrees / s^2 were chosen by meditating on the logs.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 5.0f, HA.Center, VA.Center)
        .enabledBetween(5, 10),
      TextMessage("Still, noticeable overshoots happen in pitch and roll",
        msgCF, width * 0.5f, height * 0.6f, HA.Center, VA.Center)
        .enabledBetween(15, 30),
      TextMessage("with flaps forced to move to their maximum angles...",
        msgCF, width * 0.5f, height * 0.6f + msgStep * 1.0f, HA.Center, VA.Center)
        .enabledBetween(15, 30),
      new Fade(timeOn = 0.5, timeOff = 0),
      new Fade(timeOn = 61.7, timeOff = 62.2),
    )

    props.runVideo(allGraphics)
  end apply
  