package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{withSpeed, HorizontalAlignment as HA, VerticalAlignment as VA}

object V_2024_03_29_p1:
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
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 20,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(10, 10, 50)
    val msgCF = TextMessage.ColorFont(msgFontSize, msgColor)

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("March 29, 2024, test 1 (actually 3)",
        msgCF, width * 0.5f, height * 0.25f,
        HA.Center, VA.Center, 1, 10),
      TextMessage("I am still hunting overshoots via tuning adjustment.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1.5f,
        HA.Center, VA.Center, 3, 10),
      TextMessage("Since last test I have configured harmonic notch",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2.5f,
        HA.Center, VA.Center, 3, 10),
      TextMessage("with ESC telemetry as a source, and did two tests behind the scene.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 3.5f,
        HA.Center, VA.Center, 3, 10),
      TextMessage("For this test, I increased pitch and roll D-terms from 0.002 to 0.01",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 5f,
        HA.Center, VA.Center, 5, 10),
      TextMessage("in the hope that CX7 will react quicker and avoid overshoots.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 6f,
        HA.Center, VA.Center, 5, 10),
      TextMessage("I am performing increasingly hard twitches on pitch and roll",
        msgCF, width * 0.5f, height * 0.5f,
        HA.Center, VA.Center, 14, 18),
      TextMessage("and observing the behavior of the copter",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 1f,
        HA.Center, VA.Center, 14, 18),
      TextMessage("Watch for an example of overshoot on the pitch axis...",
        msgCF, width * 0.5f, height * 0.5f,
        HA.Center, VA.Center, 22, 27),
      TextMessage("NOW!",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 1f,
        HA.Center, VA.Center, 25.3 withSpeed 100, 27),
      TextMessage("A strong one will happen",
        msgCF, width * 0.5f, height * 0.5f,
        HA.Center, VA.Center, 35, 41),
      TextMessage("in 3",
        msgCF, width * 0.5f - msgFontSize * 2.5f, height * 0.5f + msgStep * 1f,
        HA.Center, VA.Center, 36 withSpeed 100, 41),
      TextMessage("2",
        msgCF, width * 0.5f - msgFontSize * 1f, height * 0.5f + msgStep * 1f,
        HA.Center, VA.Center, 37 withSpeed 100, 41),
      TextMessage("1",
        msgCF, width * 0.5f - msgFontSize * 0f, height * 0.5f + msgStep * 1f,
        HA.Center, VA.Center, 38 withSpeed 100, 41),
      TextMessage("NOW!",
        msgCF, width * 0.5f + msgFontSize * 2f, height * 0.5f + msgStep * 1f,
        HA.Center, VA.Center, 39 withSpeed 100, 41),
      TextMessage("Yet another big one",
        msgCF, width * 0.5f, height * 0.2f,
        HA.Center, VA.Center, 56, 63),
      TextMessage("in 3",
        msgCF, width * 0.5f - msgFontSize * 2.5f, height * 0.2f + msgStep * 1f,
        HA.Center, VA.Center, 57 withSpeed 100, 63),
      TextMessage("2",
        msgCF, width * 0.5f - msgFontSize * 1f, height * 0.2f + msgStep * 1f,
        HA.Center, VA.Center, 58 withSpeed 100, 63),
      TextMessage("1",
        msgCF, width * 0.5f - msgFontSize * 0f, height * 0.2f + msgStep * 1f,
        HA.Center, VA.Center, 59 withSpeed 100, 63),
      TextMessage("NOW!",
        msgCF, width * 0.5f + msgFontSize * 2f, height * 0.2f + msgStep * 1f,
        HA.Center, VA.Center, 60 withSpeed 100, 63),
      TextMessage("Increasing D-term cannot make it alone.",
        msgCF, width * 0.5f, height * 0.5f,
        HA.Center, VA.Center, 67, 74),
      TextMessage("When returning to level, required angle rates change too quickly.",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 1.5f,
        HA.Center, VA.Center, 68.5, 74),
      TextMessage("Maybe the maximum angular accelerations need to be reduced?",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 2.5f,
        HA.Center, VA.Center, 68.5, 74),
      new Fade(timeOn = 74.3, timeOff = 74.8),
    )

    props.run(allGraphics)
  end apply
  