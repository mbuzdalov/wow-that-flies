package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, RightBlanket, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p13:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      LeftBlanket(0.38f, new Color(240, 240, 240, 200), 1.5, 1.9, 10.1, 10.5),
      TextMessage("July 07th, test #6.",
        msgCF, width * 0.02f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(2, 10),
      TextMessage("Well, let's increase PIDs again.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("This was:",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("ATC_RAT_{PIT,RLL}_{P,I}:",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 4f, HA.Left, VA.Center)
        .enabledBetween(4, 10),
      TextMessage("0.2 ➞ 0.5",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 5f, HA.Left, VA.Center)
        .enabledBetween(4, 10),
      TextMessage("ATC_RAT_{PIT,RLL}_D:",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 6.5f, HA.Left, VA.Center)
        .enabledBetween(6, 10),
      TextMessage("0.003 ➞ 0.004",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 7.5f, HA.Left, VA.Center)
        .enabledBetween(6, 10),
      TextMessage("ATC_RAT_YAW_{P,I}:",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 9f, HA.Left, VA.Center)
        .enabledBetween(8, 10),
      TextMessage("0.1 ➞ 0.15",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 10f, HA.Left, VA.Center)
        .enabledBetween(8, 10),

      LeftBlanket(0.36f, new Color(240, 240, 240, 200), 38.5, 38.9, 48.1, 48.5),
      TextMessage("Okay, this was quite nice.",
        msgCF, width * 0.02f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(39, 48),
      TextMessage("By this time, I also enabled",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(42, 48),
      TextMessage("more logging, so, to get",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(42, 48),
      TextMessage("more data for further tuning...",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(42, 48),
      TextMessage("... let's fly again!",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 5f, HA.Left, VA.Center)
        .enabledBetween(46, 48),

      RightBlanket(0.4f, new Color(240, 240, 240, 200), 130.5, 130.9, 136.1, 136.5),
      TextMessage("Nice flying experience overall.",
        msgCF, width * 0.62f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(131, 136),
      TextMessage("More code and configuration",
        msgCF, width * 0.62f, height * 0.06f + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(133, 136),
      TextMessage("testing with SC1 will follow,",
        msgCF, width * 0.62f, height * 0.06f + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(133, 136),
      TextMessage("stay tuned!",
        msgCF, width * 0.62f, height * 0.06f + msgStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(133, 136),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 136.5, timeOff = 137),
    )

    props.runVideo(allGraphics)
  end apply
