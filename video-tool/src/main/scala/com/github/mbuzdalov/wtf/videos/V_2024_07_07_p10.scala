package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p10:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      LeftBlanket(0.33f, new Color(240, 240, 240, 200), 1.5, 1.9, 10.1, 10.5),
      TextMessage("July 07th, test #3.",
        msgCF, width * 0.02f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(2, 10),
      TextMessage("Moving yaw PIDs further:",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(4, 10),
      TextMessage("ATC_RAT_YAW_{P,I}:",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 3f, HA.Left, VA.Center)
        .enabledBetween(4, 10),
      TextMessage("0.18 ➞ 0.1.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 4f, HA.Left, VA.Center)
        .enabledBetween(4, 10),

      LeftBlanket(0.27f, new Color(240, 240, 240, 200), 19.5, 19.9, 24.1, 24.5),
      TextMessage("Even better,",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 6f, HA.Left, VA.Center)
        .enabledBetween(20, 24),
      TextMessage("but yaw oscillations",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 7f, HA.Left, VA.Center)
        .enabledBetween(20, 24),
      TextMessage("are still there.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 8f, HA.Left, VA.Center)
        .enabledBetween(20, 24),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 24.5, timeOff = 25),
    )

    props.runVideo(allGraphics)
  end apply
