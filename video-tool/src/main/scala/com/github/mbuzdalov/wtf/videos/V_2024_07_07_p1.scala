package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_07_07_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(None, t => 1.0 + t / 100, t => t / 5 / 360 * math.Pi, (w, t) => w * (0.5 - t / 100)),
      LeftBlanket(0.28f, new Color(240, 240, 240, 200), 1.5, 1.9, 13.1, 13.5),
      TextMessage("Meet SC1,",
        msgCF, width * 0.02f, height * 0.06f, HA.Left, VA.Center)
        .enabledBetween(2, 13),
      TextMessage("my first SingleCopter!",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 1f, HA.Left, VA.Center)
        .enabledBetween(2, 13),
      TextMessage("It is driven by",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(5, 13),
      TextMessage("just one propeller,",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(5, 13),
      TextMessage("but has 12 slots",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 4.5f, HA.Left, VA.Center)
        .enabledBetween(5, 13),
      TextMessage("for different",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 5.5f, HA.Left, VA.Center)
        .enabledBetween(5, 13),
      TextMessage("flap arrangements!",
        msgCF, width * 0.02f, height * 0.06f + msgStep * 6.5f, HA.Left, VA.Center)
        .enabledBetween(5, 13),

      Fade(timeOn = 1.0, timeOff = 0),
      Fade(timeOn = 13.5, timeOff = 14),
    )

    props.runImage(14, allGraphics)
  end apply
  