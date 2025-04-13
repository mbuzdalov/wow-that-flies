package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p8:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(what = None, scale = t => 1.0 + t / 100, rotate = t => 0.0),
      RightBlanket(0.4, new Color(255, 255, 255, 200), 1.6, 2, 8.1, 8.5),
      TextMessage("Some of the damage",
        msgCF, width * 0.65, height * 0.3, HA.Left, VA.Center)
        .enabledBetween(2, 8.1),
      TextMessage("was mostly cosmetic",
        msgCF, width * 0.65, height * 0.3 + msgStep, HA.Left, VA.Center)
        .enabledBetween(2, 8.1),
      TextMessage("and would not impact",
        msgCF, width * 0.65, height * 0.3 + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(2, 8.1),
      TextMessage("further testing.",
        msgCF, width * 0.65, height * 0.3 + 3 * msgStep, HA.Left, VA.Center)
        .enabledBetween(2, 8.1),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 8.5, timeOff = 9),
    )

    props.runImage(9, allGraphics)
  end apply
  