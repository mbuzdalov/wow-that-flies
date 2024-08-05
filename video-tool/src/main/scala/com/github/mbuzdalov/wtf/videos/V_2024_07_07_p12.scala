package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p12:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      LeftBlanket(0.38f, new Color(240, 240, 240, 200), 1.5, 1.9, 10.1, 10.5),
      TextMessage("July 07th, test #5.",
        msgCF, width * 0.02f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(2, 10),
      TextMessage("As it gets serious,",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("I applied the same",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("throttle-scaling patch",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("as I did for CX7 before.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 4.5f, HA.Left, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("Roll and pitch P,I were",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 6f, HA.Left, VA.Center)
        .enabledBetween(5, 10),
      TextMessage("changed from 0.35 to 0.2",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 7f, HA.Left, VA.Center)
        .enabledBetween(5, 10),
      TextMessage("to compensate for scaling.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 8f, HA.Left, VA.Center)
        .enabledBetween(5, 10),
      TextMessage("Their D-terms were increased",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 9.5f, HA.Left, VA.Center)
        .enabledBetween(7, 10),
      TextMessage("from 0.001 to 0.003.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 10.5f, HA.Left, VA.Center)
        .enabledBetween(7, 10),
      TextMessage("The D-filters were increased",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 12f, HA.Left, VA.Center)
        .enabledBetween(8, 10),
      TextMessage("from 20 to 40 Hz.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 13f, HA.Left, VA.Center)
        .enabledBetween(8, 10),

      LeftBlanket(0.33f, new Color(240, 240, 240, 200), 15.5, 15.9, 21.1, 21.5),
      TextMessage("This was not flyable...",
        msgCF, width * 0.02f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(16, 21),
      TextMessage("The new pitch and roll",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(16, 21),
      TextMessage("rate P values were",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(16, 21),
      TextMessage("just too small.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(16, 21),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 21.5, timeOff = 22),
    )

    props.runVideo(allGraphics)
  end apply
