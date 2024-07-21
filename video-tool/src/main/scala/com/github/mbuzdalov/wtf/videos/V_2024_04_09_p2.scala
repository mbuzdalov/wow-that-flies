package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_09_p2:
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
      TextMessage("April 09, 2024, test #2.",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(1, 8),
      TextMessage("There was some constant noise in the frequency plots at around 106 Hz,",
        msgCF, width * 0.5f, height * 0.25f + 1.0f * msgStep, HA.Center, VA.Center)
        .enabledBetween(2, 8),
      TextMessage("for which I added a notch. This will subsequently allow to increase the D-terms.",
        msgCF, width * 0.5f, height * 0.25f + 2.0f * msgStep, HA.Center, VA.Center)
        .enabledBetween(2, 8),
      TextMessage("The sound change is due to yaw input (visible on the sticks).",
        msgCF, width * 0.5f, height * 0.35f, HA.Center, VA.Center)
        .enabledBetween(31, 36),
      TextMessage("Okay, at least I can keep my hands off the I-terms for now.",
        msgCF, width * 0.5f, height * 0.35f, HA.Center, VA.Center)
        .enabledBetween(50, 55),
      Fade(timeOn = 0.5, timeOff = 0.0),
      Fade(timeOn = 55.0, timeOff = 55.5),
    )

    props.runVideo(allGraphics)
  end apply
  