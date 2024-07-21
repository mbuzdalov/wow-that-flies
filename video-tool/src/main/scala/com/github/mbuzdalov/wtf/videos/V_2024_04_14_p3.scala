package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_14_p3:
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
      TextMessage("April 14, 2024, test #3.",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(1, 12),
      TextMessage("I felt I can increase some gains now for sharper control:",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(2, 12),
      TextMessage("ATC_RAT_RLL_{P,I}: 0.2 ➞ 0.22",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(4, 12),
      TextMessage("ATC_RAT_RLL_D: 0.004 ➞ 0.005",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 3f, HA.Center, VA.Center)
        .enabledBetween(4, 12),
      TextMessage("ATC_RAT_PIT_{P,I}: 0.2 ➞ 0.3",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 4f, HA.Center, VA.Center)
        .enabledBetween(6, 12),
      TextMessage("ATC_RAT_{PIT,RLL}_FLTD: 20 ➞ 40",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 5f, HA.Center, VA.Center)
        .enabledBetween(8, 12),
      TextMessage("Roll is still quite fine,",
        msgCF, width * 0.5f, height * 0.35f, HA.Center, VA.Center)
        .enabledBetween(50, 56),
      TextMessage("pitch is still quite miserable in one of the directions...",
        msgCF, width * 0.5f, height * 0.35f + msgStep * 1.0f, HA.Center, VA.Center)
        .enabledBetween(50, 56),
      TextMessage("Positive pitch is quite nice.",
        msgCF, width * 0.5f, height * 0.8f, HA.Center, VA.Center)
        .enabledBetween(62, 70),
      TextMessage("Negative pitch is a shame.",
        msgCF, width * 0.5f, height * 0.8f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(65, 70),
      TextMessage("Well, it's definitely better than last time.",
        msgCF, width * 0.5f, height * 0.35f, HA.Center, VA.Center)
        .enabledBetween(124, 132),
      TextMessage("But based on the pitch/roll difference,",
        msgCF, width * 0.5f, height * 0.35f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(125.5, 132),
      TextMessage("it seems that I am overlooking some design flaw.",
        msgCF, width * 0.5f, height * 0.35f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(125.5, 132),
      TextMessage("Which one? You will see it in the next video...",
        msgCF, width * 0.5f, height * 0.35f + msgStep * 3f, HA.Center, VA.Center)
        .enabledBetween(127, 132),
      Fade(timeOn = 0.5, timeOff = 0.0),
      Fade(timeOn = 131.9, timeOff = 132.4),
    )

    props.runVideo(allGraphics)
  end apply
  