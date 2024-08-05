package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(t => 1.0 + t / 100, t => t / 5 / 360 * math.Pi, (w, t) => w * (0.5 - t / 100)),
      LeftBlanket(0.22f, new Color(240, 240, 240, 200), 1.5, 1.9, 15.1, 15.5),
      TextMessage("ArduCopter",
        msgCF, width * 0.02f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(2, 15),
      TextMessage("supports",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 1f, HA.Left, VA.Center)
        .enabledBetween(2, 15),
      TextMessage("only four flaps",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 2f, HA.Left, VA.Center)
        .enabledBetween(2, 15),
      TextMessage("arranged in a +.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 3f, HA.Left, VA.Center)
        .enabledBetween(2, 15),
      TextMessage("Perfect control",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 4.5f, HA.Left, VA.Center)
        .enabledBetween(5, 15),
      TextMessage("is possible",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 5.5f, HA.Left, VA.Center)
        .enabledBetween(5, 15),
      TextMessage("with three.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 6.5f, HA.Left, VA.Center)
        .enabledBetween(5, 15),
      TextMessage("I plan to unify",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 8f, HA.Left, VA.Center)
        .enabledBetween(9, 15),
      TextMessage("Single- and",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 9f, HA.Left, VA.Center)
        .enabledBetween(9, 15),
      TextMessage("CoaxCopter",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 10f, HA.Left, VA.Center)
        .enabledBetween(9, 15),
      TextMessage("and support",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 11f, HA.Left, VA.Center)
        .enabledBetween(9, 15),
      TextMessage("more choices.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 12f, HA.Left, VA.Center)
        .enabledBetween(9, 15),

      Fade(timeOn = 1.0, timeOff = 0),
      Fade(timeOn = 15.5, timeOff = 16),
    )

    props.runImage(16, allGraphics)
  end apply
  