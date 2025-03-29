package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_06_23_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val stickSize = 101 * width / 1280
    val stickGap = 5 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      width - 2 * (stickSize + stickGap), width - (stickSize + stickGap), stickGap)

    val rpWidth = width / 3
    val rpHeight = height / 2
    val rpGap = 5 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 90,
      rpGap, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 4)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))

    val allGraphics = GraphicsConsumer.compose(sticks, pitchPlot.enabledBetween(15, Double.PositiveInfinity),
      TextMessage("June 23, 2024.",
        msgCF, width * 0.22f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(1, 15),
      TextMessage("Test #1: no side wind.",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(1, 15),
      TextMessage("CX7 is in Acro, which means",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 2.5f, HA.Center, VA.Center)
        .enabledBetween(3, 15),
      TextMessage("the sticks control the angular rate.",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 3.5f, HA.Center, VA.Center)
        .enabledBetween(3, 15),
      TextMessage("The roll flap is disabled",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 5f, HA.Center, VA.Center)
        .enabledBetween(7, 15),
      TextMessage("to avoid potential interference.",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 6f, HA.Center, VA.Center)
        .enabledBetween(7, 15),
      TextMessage("Yaw control is not disabled,",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 7.5f, HA.Center, VA.Center)
        .enabledBetween(10, 15),
      TextMessage("which is the reason for sound changes",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 8.5f, HA.Center, VA.Center)
        .enabledBetween(10, 15),
      TextMessage("at some orientations, which, hopefully,",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 9.5f, HA.Center, VA.Center)
        .enabledBetween(10, 15),
      TextMessage("does not affect the test much.",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 10.5f, HA.Center, VA.Center)
        .enabledBetween(10, 15),
      TextMessage("The servo output",
        msgCF, width * 0.22f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(135, 149),
      TextMessage("is quite stable for all",
        msgCF, width * 0.22f, height * 0.06f + msgStep, HA.Center, VA.Center)
        .enabledBetween(135, 149),
      TextMessage("pitch values, which means",
        msgCF, width * 0.22f, height * 0.06f + 2 * msgStep, HA.Center, VA.Center)
        .enabledBetween(135, 149),
      TextMessage("poor control at speed and",
        msgCF, width * 0.22f, height * 0.06f + 3 * msgStep, HA.Center, VA.Center)
        .enabledBetween(135, 149),
      TextMessage("in the wind is due to air",
        msgCF, width * 0.22f, height * 0.06f + 4 * msgStep, HA.Center, VA.Center)
        .enabledBetween(135, 149),
      TextMessage("coming from the side.",
        msgCF, width * 0.22f, height * 0.06f + 5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(135, 149),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 148.5, timeOff = 149),
    )

    props.runVideo(allGraphics)
  end apply
  
