package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, RightBlanket, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p11:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      LeftBlanket(0.33f, new Color(240, 240, 240, 200), 1.5, 1.9, 15.1, 15.5),
      TextMessage("July 07th, test #4.",
        msgCF, width * 0.02f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(2, 15),
      TextMessage("Yet another thing",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(4, 15),
      TextMessage("that is normally only used",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(4, 15),
      TextMessage("with torque-controlled axes",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(4, 15),
      TextMessage("is the error filter.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 4.5f, HA.Left, VA.Center)
        .enabledBetween(4, 15),
      TextMessage("Here, yaw is controlled",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 6f, HA.Left, VA.Center)
        .enabledBetween(9, 15),
      TextMessage("with flaps, so this",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 7f, HA.Left, VA.Center)
        .enabledBetween(9, 15),
      TextMessage("can be removed.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 8f, HA.Left, VA.Center)
        .enabledBetween(9, 15),
      TextMessage("ATC_RAT_YAW_FLTE:",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 9.5f, HA.Left, VA.Center)
        .enabledBetween(12, 15),
      TextMessage("2.5 ➞ 0.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 10.5f, HA.Left, VA.Center)
        .enabledBetween(12, 15),
      TextMessage("Also, ATC_RAT_YAW_D:",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 12f, HA.Left, VA.Center)
        .enabledBetween(12, 15),
      TextMessage("0.001 ➞ 0.004.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 13f, HA.Left, VA.Center)
        .enabledBetween(12, 15),

      RightBlanket(0.27f, new Color(240, 240, 240, 200), 31.5, 31.9, 37.1, 37.5),
      TextMessage("Wow, that flies!",
        msgCF, width * 0.75f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(32, 37),
      TextMessage("Still a way to go,",
        msgCF, width * 0.75f, height * 0.06f + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(32, 37),
      TextMessage("but looks nice...",
        msgCF, width * 0.75f, height * 0.06f + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(32, 37),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 37.5, timeOff = 38),
    )

    props.runVideo(allGraphics)
  end apply
