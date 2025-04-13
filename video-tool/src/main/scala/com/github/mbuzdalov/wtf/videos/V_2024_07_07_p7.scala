package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, ScaleRotateCropBack, TextMessage, TopBlanket}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p7:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(None, t => 1.0 + t / 100, t => t / 5 / 360 * math.Pi),
      TopBlanket(0.13f, new Color(240, 240, 240, 200), 1.5, 1.9, 9.1, 9.5),
      TextMessage("So, it was time for a radical action. Flaps were enlarged by 2.5 times.",
        msgCF, width * 0.5f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(2, 9),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 9.5, timeOff = 10),
    )

    props.runImage(10, allGraphics)
  end apply
  