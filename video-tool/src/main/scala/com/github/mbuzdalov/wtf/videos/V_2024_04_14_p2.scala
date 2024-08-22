package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}
import com.github.mbuzdalov.wtf.util.{PiecewiseLinearFunction => PLW}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}

object V_2024_04_14_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val hWidth = width / 2
    val stickSize = 101 * width / 1280
    val stickGap = 11 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      hWidth - stickSize - stickGap, hWidth + stickGap, stickGap)

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
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("April 14, 2024, test #2.",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(1, 7),
      TextMessage("This time, ATC_ANG_{PIT,RLL}_P are increased to 16.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1.0f, HA.Center, VA.Center)
        .enabledBetween(3, 7),
      TextMessage("Pitch is still not very good, as you can see",
        msgCF, width * 0.5f, height * 0.7f, HA.Center, VA.Center)
        .enabledBetween(11, 19),
      TextMessage("in 3",
        msgCF, width * 0.5f - msgFontSize * 2.5f, height * 0.7f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(13.5, 0) to (13.51, 1) to (18.5, 1) to (19, 0)),
      TextMessage("2",
        msgCF, width * 0.5f - msgFontSize * 1f, height * 0.7f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(14.5, 0) to (14.51, 1) to (18.5, 1) to (19, 0)),
      TextMessage("1",
        msgCF, width * 0.5f - msgFontSize * 0f, height * 0.7f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(15.5, 0) to (15.51, 1) to (18.5, 1) to (19, 0)),
      TextMessage("NOW!",
        msgCF, width * 0.5f + msgFontSize * 2f, height * 0.7f + msgStep * 1f, HA.Center, VA.Center)
        .withAlpha(PLW(16.5, 0) to (16.51, 1) to (18.5, 1) to (19, 0)),
      TextMessage("But roll is really nice now...",
        msgCF, width * 0.5f, height * 0.7f, HA.Center, VA.Center)
        .enabledBetween(27, 32),
      TextMessage("Let's keep these angle P-terms at 16 for now.",
        msgCF, width * 0.5f, height * 0.4f, HA.Center, VA.Center)
        .enabledBetween(71, 76),
      TextMessage("They give some hope.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(72, 76),
      Fade(timeOn = 0.5, timeOff = 0.0),
      Fade(timeOn = 76.8, timeOff = 77.3),
    )

    props.runVideo(allGraphics)
  end apply
  