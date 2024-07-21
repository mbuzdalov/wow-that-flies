package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_06_23_p6:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))

    val allGraphics = GraphicsConsumer.compose(
      TextMessage("CX7 can rotate",
        msgCF, width * 0.21f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(5, 20),
      TextMessage("quite freely in the stand",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(5, 20),
      TextMessage("and is balanced",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(5, 20),
      TextMessage("pretty well.",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 3f, HA.Center, VA.Center)
        .enabledBetween(5, 20),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 28.5, timeOff = 29),
    )

    props.runVideo(allGraphics)
  end apply
  