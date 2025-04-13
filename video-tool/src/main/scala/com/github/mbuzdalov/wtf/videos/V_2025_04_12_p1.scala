package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_04_12_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val centerH = TextMessage.HorizontalAlignment.Center
    val centerV = TextMessage.VerticalAlignment.Center

    val allGraphics = GraphicsConsumer.compose(
      TransparentSeparator(0.5, 0, 0.5, 1, 0.01),

      TextMessage("Test 1.",
        msgCF, 0.75 * width, 0.05 * height, centerH, centerV)
        .enabledBetween(1, 5.5),
      TextMessage("A very energetic lift-off, just in case",
        msgCF, 0.75 * width, 0.05 * height + msgStep, centerH, centerV)
        .enabledBetween(1.5, 5.5),
      TextMessage("the machine gets hit when the cap opens.",
        msgCF, 0.75 * width, 0.05 * height + 2 * msgStep, centerH, centerV)
        .enabledBetween(1.5, 5.5),

      TextMessage("It appears that there was no need",
        msgCF, 0.75 * width, 0.05 * height, centerH, centerV)
        .enabledBetween(17, 22),
      TextMessage("to hurry up at all. Gentle lift-off",
        msgCF, 0.75 * width, 0.05 * height + msgStep, centerH, centerV)
        .enabledBetween(17, 22),
      TextMessage("is what we actually need.",
        msgCF, 0.75 * width, 0.05 * height + 2 * msgStep, centerH, centerV)
        .enabledBetween(17, 22),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 22.5, timeOff = 23.0),
    )

    props.runVideo(allGraphics)
  end apply
