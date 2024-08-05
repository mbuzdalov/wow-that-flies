package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p8:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      LeftBlanket(0.4f, new Color(240, 240, 240, 200), 1.5, 1.9, 24.1, 24.5),
      TextMessage("July 07th.",
        msgCF, width * 0.02f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(2, 24),
      TextMessage("First test with new flaps.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 1f, HA.Left, VA.Center)
        .enabledBetween(2, 24),
      TextMessage("Bigger flaps ideally mean",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(4, 24),
      TextMessage("more lift for the same air velocity.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(4, 24),
      TextMessage("The flap airfoil shape, E169,",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 5f, HA.Left, VA.Center)
        .enabledBetween(7, 24),
      TextMessage("remains the same, and is in fact",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 6f, HA.Left, VA.Center)
        .enabledBetween(7, 24),
      TextMessage("somewhat more suitable",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 7f, HA.Left, VA.Center)
        .enabledBetween(7, 24),
      TextMessage("for this size.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 8f, HA.Left, VA.Center)
        .enabledBetween(7, 24),

      TextMessage("Oops...",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 9.5f, HA.Left, VA.Center)
        .enabledBetween(16, 24),
      TextMessage("It seemed actually overcontrolled",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 11f, HA.Left, VA.Center)
        .enabledBetween(19, 24),
      TextMessage("in yaw, but other axes were not.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 12f, HA.Left, VA.Center)
        .enabledBetween(19, 24),


      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 24.5, timeOff = 25),
    )

    props.runVideo(allGraphics)
  end apply
