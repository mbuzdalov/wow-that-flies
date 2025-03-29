package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, RightBlanket, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p6:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))

    val allGraphics = GraphicsConsumer.compose(
      RightBlanket(0.45f, new Color(240, 240, 240, 200), 1.5, 1.9, 16.1, 16.5),
      TextMessage("July 05th. Second test of SC1.",
        msgCF, width * 0.58f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(2, 16),
      TextMessage("Lift from the flaps is proportional",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(3, 16),
      TextMessage("to air velocity squared, so roughly",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(3, 16),
      TextMessage("to rotational speed squared.",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(3, 16),
      TextMessage("Momentum from the motor and",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 5f, HA.Left, VA.Center)
        .enabledBetween(6, 16),
      TextMessage("the propeller, ignoring drag,",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 6f, HA.Left, VA.Center)
        .enabledBetween(6, 16),
      TextMessage("is proportional to rotational speed.",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 7f, HA.Left, VA.Center)
        .enabledBetween(6, 16),
      TextMessage("Increasing mass may help",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 8.5f, HA.Left, VA.Center)
        .enabledBetween(9, 16),
      TextMessage("with yaw control. Will it?",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 9.5f, HA.Left, VA.Center)
        .enabledBetween(9, 16),
      TextMessage("No.",
        msgCF, width * 0.58f, height * 0.06f + msgStep * 11f, HA.Left, VA.Center)
        .enabledBetween(13, 16),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 16.5, timeOff = 17),
    )

    props.runVideo(allGraphics)
  end apply
