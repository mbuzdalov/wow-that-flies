package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p3:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(t => 1.0 + t / 100, t => t / 5 / 360 * math.Pi, (w, t) => w * (0.5 - t / 100)),
      LeftBlanket(0.24f, new Color(240, 240, 240, 200), 1.5, 1.9, 12.1, 12.5),
      TextMessage("Flaps and servos",
        msgCF, width * 0.02f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(2, 12),
      TextMessage("are designed to be",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 1f, HA.Left, VA.Center)
        .enabledBetween(2, 12),
      TextMessage("“easily” movable,",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 2f, HA.Left, VA.Center)
        .enabledBetween(2, 12),
      TextMessage("so that at least",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 3f, HA.Left, VA.Center)
        .enabledBetween(2, 12),
      TextMessage("+, X, and two Y",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 4f, HA.Left, VA.Center)
        .enabledBetween(2, 12),
      TextMessage("arrangements",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 5f, HA.Left, VA.Center)
        .enabledBetween(2, 12),
      TextMessage("are possible.",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 6f, HA.Left, VA.Center)
        .enabledBetween(2, 12),

      Fade(timeOn = 1.0, timeOff = 0),
      Fade(timeOn = 12.5, timeOff = 13),
    )

    props.runImage(13, allGraphics)
  end apply
  