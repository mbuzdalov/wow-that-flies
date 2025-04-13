package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, RightBlanket, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_01_p3:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(None, t => 1.0 + t / 200, t => 0.0),
      RightBlanket(0.5, new Color(255, 255, 255, 200), 1.6, 2, 7.1, 7.5),
      TextMessage("SN1 received longer",
        msgCF, width * 0.55, height * 0.2, HA.Left, VA.Center)
        .enabledBetween(2, 7.1),
      TextMessage("landing leg ends, as well as",
        msgCF, width * 0.55, height * 0.2 + msgStep, HA.Left, VA.Center)
        .enabledBetween(2, 7.1),
      TextMessage("improved flaps.",
        msgCF, width * 0.55, height * 0.2 + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(2, 7.1),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 7.5, timeOff = 8),
    )

    props.runImage(8, allGraphics)
  end apply
  