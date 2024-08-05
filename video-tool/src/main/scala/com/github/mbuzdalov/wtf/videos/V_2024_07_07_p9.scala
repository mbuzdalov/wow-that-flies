package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, RightBlanket, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p9:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      RightBlanket(0.42f, new Color(240, 240, 240, 200), 1.5, 1.9, 15.1, 15.5),
      TextMessage("July 07th, test #2.",
        msgCF, width * 0.6f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(2, 15),
      TextMessage("Broken leg re-printed.",
        msgCF, width * 0.6f, height * 0.06f + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(4, 15),
      TextMessage("ATC_RAT_YAW_P: 0.35 ➞ 0.18.",
        msgCF, width * 0.6f, height * 0.06f + msgStep * 3f, HA.Left, VA.Center)
        .enabledBetween(6, 15),
      TextMessage("ATC_RAT_YAW_I: 0.35 ➞ 0.18.",
        msgCF, width * 0.6f, height * 0.06f + msgStep * 4f, HA.Left, VA.Center)
        .enabledBetween(6, 15),
      TextMessage("For SingleCopter,",
        msgCF, width * 0.6f, height * 0.06f + msgStep * 5.5f, HA.Left, VA.Center)
        .enabledBetween(9, 15),
      TextMessage("P=I in yaw unlike others,",
        msgCF, width * 0.6f, height * 0.06f + msgStep * 6.5f, HA.Left, VA.Center)
        .enabledBetween(9, 15),
      TextMessage("as yaw is controlled by flaps,",
        msgCF, width * 0.6f, height * 0.06f + msgStep * 7.5f, HA.Left, VA.Center)
        .enabledBetween(9, 15),
      TextMessage("not by motor torque.",
        msgCF, width * 0.6f, height * 0.06f + msgStep * 8.5f, HA.Left, VA.Center)
        .enabledBetween(9, 15),

      RightBlanket(0.3f, new Color(240, 240, 240, 200), 22.5, 22.9, 28.1, 28.5),
      TextMessage("Much better!",
        msgCF, width * 0.72f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(23, 28),
      TextMessage("Still not",
        msgCF, width * 0.72f, height * 0.06f + msgStep * 2f, HA.Left, VA.Center)
        .enabledBetween(25, 28),
      TextMessage("very controllable,",
        msgCF, width * 0.72f, height * 0.06f + msgStep * 3f, HA.Left, VA.Center)
        .enabledBetween(25, 28),
      TextMessage("but definitely a step",
        msgCF, width * 0.72f, height * 0.06f + msgStep * 4f, HA.Left, VA.Center)
        .enabledBetween(25, 28),
      TextMessage("in the right direction.",
        msgCF, width * 0.72f, height * 0.06f + msgStep * 5f, HA.Left, VA.Center)
        .enabledBetween(25, 28),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 28.5, timeOff = 29),
    )

    props.runVideo(allGraphics)
  end apply
