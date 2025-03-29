package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, TextMessage, TopBlanket}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_10_09_p3:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      TopBlanket(0.28f, new Color(240, 240, 240, 200), 4.5, 4.9, 8.1, 8.5),

      TextMessage("October 09th, 2024. Second flight.",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(5, 8),
      TextMessage("Can I repeat the success?",
        msgCF, width * 0.5, height * 0.1 + msgStep, HA.Center, VA.Center)
        .enabledBetween(6, 8),

      TopBlanket(0.21f, new Color(240, 240, 240, 200), 20.5, 20.9, 25.1, 25.5),
      TextMessage("Seems nice enough. It's time to test this outdoors...",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(21, 25),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 25.5, timeOff = 26),
    )

    props.runVideo(allGraphics)
  end apply
