package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, ScaleRotateCropBack, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_06_23_p5:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(t => 1.0 + t / 100, t => t / 5 / 360 * math.Pi),
      TextMessage("In this test stand,",
        msgCF, width * 0.21f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(2, 8),
      TextMessage("CX7 can rotate in pitch,",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(2, 8),
      TextMessage("but is fixed in",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(2, 8),
      TextMessage("all other axes.",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 3f, HA.Center, VA.Center)
        .enabledBetween(2, 8),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 7.5, timeOff = 8),
    )

    props.runImage(8, allGraphics)
  end apply
  