package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_08_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(10, 10, 50)
    val msgCF = TextMessage.ColorFont(msgFontSize, msgColor)

    val allGraphics = GraphicsConsumer.compose(
      new ScaleRotateCropBack(t => 1.0 + t / 100.0, t => math.toRadians(t / 4)),
      TextMessage("This was the result...",
        msgCF, width * 0.25f, height * 0.65f, HA.Center, VA.Center)
        .enabledBetween(1, 5),
      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 5.5, timeOff = 6.0),
    )

    props.runVideo(allGraphics)
  end apply
  