package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_14_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes.head - props.armTime

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
      TextMessage("April 14, 2024, test #1.",
        msgCF, width * 0.5f, height * 0.25f,
        HA.Center, VA.Center, 1, 8),
      TextMessage("While overshooting, CX7 builds the difference",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1.0f,
        HA.Center, VA.Center, 2, 8),
      TextMessage(" between actual and desired angles mostly gradually.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2.0f,
        HA.Center, VA.Center, 2, 8),
      TextMessage("One of the ways to make it react quicker is to increase",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 3.0f,
        HA.Center, VA.Center, 2, 8),
      TextMessage("the angle P term. Here, ATC_ANG_{PIT,RLL}_P: 4.5 ➞ 9.0.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 4.0f,
        HA.Center, VA.Center, 2, 8),
      TextMessage("For roll, the control definitely feels better!",
        msgCF, width * 0.5f, height * 0.8f,
        HA.Center, VA.Center, 13, 17),
      TextMessage("For pitch, not so much.",
        msgCF, width * 0.5f, height * 0.8f,
        HA.Center, VA.Center, 20, 23),
      TextMessage("Quick changes in pitch are sloppy, long changes seem somewhat better.",
        msgCF, width * 0.5f, height * 0.8f,
        HA.Center, VA.Center, 30, 40),
      TextMessage("I liked this change!",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 63, 69),
      TextMessage("Generally, ATC_ANG_*_P values are increased by AutoTune,",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1.0f,
        HA.Center, VA.Center, 64, 69),
      TextMessage("up to 36 in the best cases. Let's try a bigger change...",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 2.0f,
        HA.Center, VA.Center, 64, 69),
      Fade(timeOn = 68.9, timeOff = 69.4),
    )

    props.runVideo(allGraphics)
  end apply
  