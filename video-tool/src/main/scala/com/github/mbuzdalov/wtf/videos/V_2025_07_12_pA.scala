package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_07_12_pA:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val blanketBackground = new Color(255, 255, 255, 200)

    val allGraphics = GraphicsConsumer.compose(
      BottomBlanket(0.1, blanketBackground, 14.5, 14.8, 20.2, 20.5),
      TextMessage("Roll is obviously not done (and low PIDs for roll also indicate that)",
        msgCF, 0.5 * width, (1 - 0.05) * height, HA.Center, VA.Center)
        .enabledBetween(15, 20),

      BottomBlanket(0.1, blanketBackground, 44.5, 44.8, 54.2, 54.5),
      TextMessage("Wipe your lenses before you test, that's the lesson...",
        msgCF, 0.5 * width, (1 - 0.05) * height, HA.Center, VA.Center)
        .enabledBetween(45, 54),

      BottomBlanket(0.1, blanketBackground, 74.5, 74.8, 88.2, 88.5),
      TextMessage("The machine does feel that little breeze that blows towards the tower...",
        msgCF, 0.5 * width, (1 - 0.05) * height, HA.Center, VA.Center)
        .enabledBetween(75, 88),

      BottomBlanket(0.1, blanketBackground, 102.5, 102.8, 109.2, 109.5),
      TextMessage("Overall, this one flies! Part 2 coming soon...",
        msgCF, 0.5 * width, (1 - 0.05) * height, HA.Center, VA.Center)
        .enabledBetween(103, 109),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 110.5, timeOff = 111.0),
    )

    props.runVideo(allGraphics)
  end apply
