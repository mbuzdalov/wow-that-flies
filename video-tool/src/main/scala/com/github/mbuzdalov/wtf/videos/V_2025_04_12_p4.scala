package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_04_12_p4:
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

      TextMessage("Test 4.",
        msgCF, 0.75 * width, 0.05 * height, centerH, centerV)
        .enabledBetween(1, 6),
      TextMessage("Explicitly moving sideways",
        msgCF, 0.75 * width, 0.05 * height + msgStep, centerH, centerV)
        .enabledBetween(1.5, 6),
      TextMessage("and trying not to crash when landing.",
        msgCF, 0.75 * width, 0.05 * height + 2 * msgStep, centerH, centerV)
        .enabledBetween(1.5, 6),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 12.0, timeOff = 12.5),
    )

    props.runVideo(allGraphics)
  end apply
