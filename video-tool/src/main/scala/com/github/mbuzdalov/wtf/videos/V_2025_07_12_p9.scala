package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_07_12_p9:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val blanketBackground = new Color(255, 255, 255, 200)

    val allGraphics = GraphicsConsumer.compose(
      TopBlanket(0.1, blanketBackground, 0.5, 0.8, 7.2, 7.5),
      TextMessage("A post-tune indoor test with an aggressive throttle profile.",
        msgCF, 0.5 * width, 0.05 * height, HA.Center, VA.Center)
        .enabledBetween(1, 7),

      TopBlanket(0.24, blanketBackground, 20.5, 20.8, 30.2, 30.5),
      TextMessage("The apparent wobbling of front motors on descents",
        msgCF, 0.5 * width, 0.05 * height, HA.Center, VA.Center)
        .enabledBetween(21, 30),
      TextMessage("is a consequence of low throttle and inverse output scaling.",
        msgCF, 0.5 * width, 0.05 * height + msgStep, HA.Center, VA.Center)
        .enabledBetween(21, 30),
      TextMessage("This should improve a bit in the next version of the code.",
        msgCF, 0.5 * width, 0.05 * height + msgStep * 2, HA.Center, VA.Center)
        .enabledBetween(21, 30),

      TopBlanket(0.1, blanketBackground, 46.5, 46.8, 52.2, 52.5),
      TextMessage("Well, this is probably good enough to try flying outdoors.",
        msgCF, 0.5 * width, 0.05 * height, HA.Center, VA.Center)
        .enabledBetween(47, 52),

      Fade(timeOn = 0.2, timeOff = 0),
      Fade(timeOn = 52.5, timeOff = 53.0),
    )

    props.runVideo(allGraphics)
  end apply
