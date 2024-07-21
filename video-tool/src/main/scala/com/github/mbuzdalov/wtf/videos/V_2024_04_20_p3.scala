package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_20_p3:
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

    val rpWidth = width / 6
    val rpHeight = height / 5
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 20,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(240, 240, 0), new Color(0, 0, 0, 120))
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("April 20, 2024, test #3",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(1, 8),
      TextMessage("I decided to just fly in the current conditions",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(2, 8),
      TextMessage("with the current settings to get more data,",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(2, 8),
      TextMessage("and to exercise the batteries :)",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 3f, HA.Center, VA.Center)
        .enabledBetween(2, 8),
      TextMessage("Ugh, that was a terrible one!",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(60, 65),
      TextMessage("O HAI!",
        msgCF, width * 0.5f, height * 0.85f, HA.Center, VA.Center)
        .enabledBetween(80, 84),
      TextMessage("O HAI AGAIN!",
        msgCF, width * 0.5f, height * 0.85f, HA.Center, VA.Center)
        .enabledBetween(136, 140),
      TextMessage("This altogether was the first outdoor session",
        msgCF, width * 0.5f, height * 0.4f, HA.Center, VA.Center)
        .enabledBetween(161, 169),
      TextMessage("for CX7 that did not end in a terrible crash.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(161, 169),
      TextMessage("Not a giant leap for the mankind... but a nice milestone still.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(165, 169),
      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 169.5, timeOff = 170),
    )

    props.runVideo(allGraphics)
  end apply
  