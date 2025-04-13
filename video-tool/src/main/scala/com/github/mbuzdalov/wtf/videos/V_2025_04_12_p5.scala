package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_04_12_p5:
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

      TextMessage("Test 5.",
        msgCF, 0.75 * width, 0.05 * height, centerH, centerV)
        .enabledBetween(1, 5.5),
      TextMessage("Explicitly moving sideways",
        msgCF, 0.75 * width, 0.05 * height + msgStep, centerH, centerV)
        .enabledBetween(1.5, 5.5),
      TextMessage("and trying HARDER not to crash :)",
        msgCF, 0.75 * width, 0.05 * height + 2 * msgStep, centerH, centerV)
        .enabledBetween(1.5, 5.5),

      TextMessage("Well, overall the concept works!",
        msgCF, 0.75 * width, 0.05 * height, centerH, centerV)
        .enabledBetween(17, 23),
      TextMessage("It should simplify lift-offs, and remove",
        msgCF, 0.75 * width, 0.05 * height + msgStep, centerH, centerV)
        .enabledBetween(18.5, 23),
      TextMessage("the need for a separate launchpad.",
        msgCF, 0.75 * width, 0.05 * height + 2 * msgStep, centerH, centerV)
        .enabledBetween(18.5, 23),
      TextMessage("Stay tuned!",
        msgCF, 0.75 * width, 0.05 * height + 3.5 * msgStep, centerH, centerV)
        .enabledBetween(20, 23),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 23.5, timeOff = 24.0),
    )

    props.runVideo(allGraphics)
  end apply
