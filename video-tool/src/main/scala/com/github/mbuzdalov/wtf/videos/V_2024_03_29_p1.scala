package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}
import com.github.mbuzdalov.wtf.util.{PiecewiseLinearFunction => PLW}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}

object V_2024_03_29_p1:
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
      TextMessage("March 29, 2024, test 1 (actually 3)",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(1, 10),
      TextMessage("I am still hunting overshoots via tuning adjustment.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1.5f, HA.Center, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("Since last test I have configured harmonic notch",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2.5f, HA.Center, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("with ESC telemetry as a source, and did two tests behind the scene.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 3.5f, HA.Center, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("For this test, I increased pitch and roll D-terms from 0.002 to 0.01",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 5f, HA.Center, VA.Center)
        .enabledBetween(5, 10),
      TextMessage("in the hope that CX7 will react quicker and avoid overshoots.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 6f, HA.Center, VA.Center)
        .enabledBetween(5, 10),
      TextMessage("I am performing increasingly hard twitches on pitch and roll",
        msgCF, width * 0.5f, height * 0.5f, HA.Center, VA.Center)
        .enabledBetween(14, 18),
      TextMessage("and observing the behavior of the copter",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(14, 18),
      TextMessage("Watch for an example of overshoot on the pitch axis...",
        msgCF, width * 0.5f, height * 0.5f, HA.Center, VA.Center)
        .enabledBetween(22, 27),
      TextMessage("NOW!",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(25.3, 0) to (25.31, 1) to (26.5, 1) to (27, 0)),
      TextMessage("A strong one will happen",
        msgCF, width * 0.5f, height * 0.5f, HA.Center, VA.Center)
        .enabledBetween(35, 41),
      TextMessage("in 3",
        msgCF, width * 0.5f - msgFontSize * 2.5f, height * 0.5f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(36, 0) to (36.01, 1) to (40.5, 1) to (41, 0)),
      TextMessage("2",
        msgCF, width * 0.5f - msgFontSize * 1f, height * 0.5f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(37, 0) to (37.01, 1) to (40.5, 1) to (41, 0)),
      TextMessage("1",
        msgCF, width * 0.5f - msgFontSize * 0f, height * 0.5f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(38, 0) to (38.01, 1) to (40.5, 1) to (41, 0)),
      TextMessage("NOW!",
        msgCF, width * 0.5f + msgFontSize * 2f, height * 0.5f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(39, 0) to (39.01, 1) to (40.5, 1) to (41, 0)),
      TextMessage("Yet another big one",
        msgCF, width * 0.5f, height * 0.2f, HA.Center, VA.Center)
        .enabledBetween(56, 63),
      TextMessage("in 3",
        msgCF, width * 0.5f - msgFontSize * 2.5f, height * 0.2f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(57, 0) to (57.01, 1) to (62.5, 1) to (63, 0)),
      TextMessage("2",
        msgCF, width * 0.5f - msgFontSize * 1f, height * 0.2f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(58, 0) to (58.01, 1) to (62.5, 1) to (63, 0)),
      TextMessage("1",
        msgCF, width * 0.5f - msgFontSize * 0f, height * 0.2f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(59, 0) to (59.01, 1) to (62.5, 1) to (63, 0)),
      TextMessage("NOW!",
        msgCF, width * 0.5f + msgFontSize * 2f, height * 0.2f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(60, 0) to (60.01, 1) to (62.5, 1) to (63, 0)),
      TextMessage("Increasing D-term cannot make it alone.",
        msgCF, width * 0.5f, height * 0.5f, HA.Center, VA.Center)
        .enabledBetween(67, 74),
      TextMessage("When returning to level, required angle rates change too quickly.",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 1.5f, HA.Center, VA.Center)
        .enabledBetween(68.5, 74),
      TextMessage("Maybe the maximum angular accelerations need to be reduced?",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 2.5f, HA.Center, VA.Center)
        .enabledBetween(68.5, 74),
      new Fade(timeOn = 74.3, timeOff = 74.8),
    )

    props.runVideo(allGraphics)
  end apply
  