package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, RightBlanket, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p5:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))

    val allGraphics = GraphicsConsumer.compose(
      RightBlanket(0.45f, new Color(240, 240, 240, 200), 1.5, 1.9, 16.1, 16.5),
      TextMessage("July 05th. First test of SC1.",
        msgCF, width * 0.58f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(2, 16),
      TextMessage("It seems like the flaps",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(10, 16),
      TextMessage("are too small to compensate",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(10, 16),
      TextMessage("the yaw moment of such a",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(10, 16),
      TextMessage("monstrous motor/propeller pair.",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 4.5f, HA.Left, VA.Center)
        .enabledBetween(10, 16),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 16.5, timeOff = 17),
    )

    props.runVideo(allGraphics)
  end apply
