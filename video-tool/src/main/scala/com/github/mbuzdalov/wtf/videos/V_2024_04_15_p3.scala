package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_15_p3:
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
    val rpHeight = height / 2
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
      TextMessage("April 15, 2024, test #1.",
        msgCF, width * 0.5f, height * 0.25f,
        HA.Center, VA.Center, 1, 7),
      TextMessage("Flying new flaps.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1f,
        HA.Center, VA.Center, 2, 7),
      TextMessage("Apart from servo trimming, no parameters were changed.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2f,
        HA.Center, VA.Center, 4, 7),
      TextMessage("You can clearly see small oscillations in pitch.",
        msgCF, width * 0.5f, height * 0.7f,
        HA.Center, VA.Center, 12, 18),
      TextMessage("With new flaps, the old PIDs are too big.",
        msgCF, width * 0.5f, height * 0.7f + msgStep * 1f,
        HA.Center, VA.Center, 12, 18),
      TextMessage("Overshoots are nearly gone in both pitch and roll.",
        msgCF, width * 0.5f, height * 0.7f,
        HA.Center, VA.Center, 33, 38),
      TextMessage("I like the new flaps!",
        msgCF, width * 0.5f, height * 0.6f,
        HA.Center, VA.Center, 78, 83),
      TextMessage("Let's just back off the pitch PIDs a little...",
        msgCF, width * 0.5f, height * 0.6f + msgStep * 1f,
        HA.Center, VA.Center, 78, 83),
      Fade(timeOn = 0.5, timeOff = 0.0),
      Fade(timeOn = 84.1, timeOff = 84.5),
    )

    props.runVideo(allGraphics)
  end apply
  