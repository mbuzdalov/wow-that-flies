package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_06_23_p4:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(None, t => 1.0 + t / 100, t => t / 5 / 360 * math.Pi),
      TextMessage("Sometimes, it is the time",
        msgCF, width * 0.21f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(2, 10),
      TextMessage("to put a flying article",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(2, 10),
      TextMessage("in a test stand, in order",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(2, 10),
      TextMessage("to better understand",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 3f, HA.Center, VA.Center)
        .enabledBetween(2, 10),
      TextMessage("what's happening.",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 4f, HA.Center, VA.Center)
        .enabledBetween(2, 10),

      Fade(timeOn = 1.0, timeOff = 0),
      Fade(timeOn = 9.5, timeOff = 10),
    )

    props.runImage(10, allGraphics)
  end apply
  