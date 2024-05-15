package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.*
import com.github.mbuzdalov.wtf.widgets.{Fade, RollPitchPlots, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment => HA, VerticalAlignment => VA}

object V_2024_03_29_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes.head - props.armTime

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
    val rollPlot = RollPitchPlots.create(reader, logTimeOffset, "Roll", 3, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = RollPitchPlots.create(reader, logTimeOffset, "Pitch", 4, 20,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(10, 10, 50)
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("March 29, 2024, test 2 (actually 4)",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f,
        HA.Center, VA.Center, 1, 10),
      TextMessage("To reduce maximum angle accelerations, I changed",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 1.5f,
        HA.Center, VA.Center, 3, 10),
      TextMessage("ATC_ACCEL_{R,P}_MAX from 220000 to 40000.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 2.5f,
        HA.Center, VA.Center, 3, 10),
      TextMessage("The previous values were recommended defaults for a 5-inch quadcopter.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 4.0f,
        HA.Center, VA.Center, 5, 10),
      TextMessage("40000 = 400 degrees / s^2 were chosen by meditating on the logs.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 5.0f,
        HA.Center, VA.Center, 5, 10),
      TextMessage("Still, noticeable overshoots happen in pitch and roll",
        msgFontSize, msgColor, width * 0.5f, height * 0.6f,
        HA.Center, VA.Center, 15, 30),
      TextMessage("with flaps forced to move to their maximum angles...",
        msgFontSize, msgColor, width * 0.5f, height * 0.6f + msgStep * 1.0f,
        HA.Center, VA.Center, 15, 30),
      new Fade(timeOn = 0.5, timeOff = 0),
      new Fade(timeOn = 61.7, timeOff = 62.2),
    )

    props.run(allGraphics)
  end apply
  