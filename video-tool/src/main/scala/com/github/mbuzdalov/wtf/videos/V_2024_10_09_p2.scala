package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, TextMessage, TopBlanket}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_10_09_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      TopBlanket(0.28f, new Color(240, 240, 240, 200), 4.5, 4.9, 9.1, 9.5),

      TextMessage("October 09th, 2024. First landing cage tests.",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(5, 9),
      TextMessage("Can I land into the cage? Will it work?",
        msgCF, width * 0.5, height * 0.1 + msgStep, HA.Center, VA.Center)
        .enabledBetween(6, 9),

      TopBlanket(0.21f, new Color(240, 240, 240, 200), 22.5, 22.9, 27.1, 27.5),
      TextMessage("Yes! This seems to work!",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(23, 27),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 28.5, timeOff = 29),
    )

    props.runVideo(allGraphics)
  end apply
