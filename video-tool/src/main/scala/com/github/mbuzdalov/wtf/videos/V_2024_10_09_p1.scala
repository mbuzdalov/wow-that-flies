package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_10_09_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(240, 240, 0), new Color(0, 0, 0, 120))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(None, t => 1.0 + t / 100, t => t / 5 / 360 * math.Pi),
      TextMessage("Our breakthrough product,",
        msgCF, width * 0.2, height * 0.2, HA.Center, VA.Center)
        .enabledBetween(1, 10.5),
      TextMessage("“The Landing Cage”,",
        msgCF, width * 0.2, height * 0.2 + msgStep, HA.Center, VA.Center)
        .enabledBetween(1, 10.5),
      TextMessage("will never let your rocket down,",
        msgCF, width * 0.2, height * 0.2 + msgStep * 2, HA.Center, VA.Center)
        .enabledBetween(1, 10.5),
      TextMessage("so you can become a",
        msgCF, width * 0.2, height * 0.2 + msgStep * 3, HA.Center, VA.Center)
        .enabledBetween(1, 10.5),
      TextMessage("“Happy Rocketeer”!",
        msgCF, width * 0.2, height * 0.2 + msgStep * 4, HA.Center, VA.Center)
        .enabledBetween(1, 10.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 10.5, timeOff = 11),
    )

    props.runImage(11, allGraphics)
  end apply
  