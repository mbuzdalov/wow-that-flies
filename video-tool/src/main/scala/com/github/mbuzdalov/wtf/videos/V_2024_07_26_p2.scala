package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, TextMessage, TimeElapsed}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_26_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      LeftBlanket(0.38f, new Color(240, 240, 240, 200), 1.5, 1.9, 10.1, 10.5),
      TextMessage("July 26th",
        msgCF, width * 0.02, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(2, 10),
      TextMessage("Endurance test of SN1:",
        msgCF, width * 0.02, height * 0.06 + msgStep * 1.0, HA.Left, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("How much time can it hover?",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 2.0, HA.Left, VA.Center)
        .enabledBetween(3, 10),

      TextMessage("Fast forward, 5x",
        msgCF, msgFontSize * 0.2, height - msgStep, HA.Left, VA.Bottom)
        .enabledBetween(25, 340),

      LeftBlanket(0.35f, new Color(240, 240, 240, 200), 352, 352.5, 364.5, 365),

      TextMessage("5.5 minutes of hover.",
        msgCF, width * 0.02f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(353, 364),
      TextMessage("It did not overheat,",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 1.5, HA.Left, VA.Center)
        .enabledBetween(355, 364),
      TextMessage("had not got a locked prop.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 2.5, HA.Left, VA.Center)
        .enabledBetween(355, 364),
      TextMessage("It may even be possible",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 4, HA.Left, VA.Center)
        .enabledBetween(358, 364),
      TextMessage("to run AutoTune.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 5, HA.Left, VA.Center)
        .enabledBetween(358, 364),
      TextMessage("See you next time!",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 7, HA.Left, VA.Center)
        .enabledBetween(361, 364),

      TimeElapsed(_ >= 10, t => math.max(0, math.min(t, 351.7) - 13.7), msgCF),
      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 365, timeOff = 366),
    )

    props.runVideo(allGraphics, (time, frame) => time < 25 || time > 340 || frame % 5 == 0)
  end apply
