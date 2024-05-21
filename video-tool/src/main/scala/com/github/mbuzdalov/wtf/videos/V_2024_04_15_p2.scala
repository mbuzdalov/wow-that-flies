package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}
import com.github.mbuzdalov.wtf.widgets.{Fade, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}

object V_2024_04_15_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(t => 1.0 + t / 100, t => 0.0),
      TextMessage("So I made",
        msgCF, width * 1.0f / 6, height * 0.1f,
        HA.Center, VA.Center, 1, 13),
      TextMessage("new flaps.",
        msgCF, width * 1.0f / 6, height * 0.1f + msgStep * 1f,
        HA.Center, VA.Center, 1, 13),
      TextMessage("They are 1.5x",
        msgCF, width * 1.0f / 6, height * 0.1f + msgStep * 2.5f,
        HA.Center, VA.Center, 3, 13),
      TextMessage("bigger by height.",
        msgCF, width * 1.0f / 6, height * 0.1f + msgStep * 3.5f,
        HA.Center, VA.Center, 3, 13),
      TextMessage("Their shape",
        msgCF, width * 5.0f / 6, height * 0.1f,
        HA.Center, VA.Center, 6, 13),
      TextMessage("makes the areas",
        msgCF, width * 5.0f / 6, height * 0.1f + msgStep * 1f,
        HA.Center, VA.Center, 6, 13),
      TextMessage("more similar",
        msgCF, width * 5.0f / 6, height * 0.1f + msgStep * 2f,
        HA.Center, VA.Center, 6, 13),
      TextMessage("in the center,",
        msgCF, width * 5.0f / 6, height * 0.1f + msgStep * 3f,
        HA.Center, VA.Center, 6, 13),
      TextMessage("where the air",
        msgCF, width * 5.0f / 6, height * 0.1f + msgStep * 4f,
        HA.Center, VA.Center, 6, 13),
      TextMessage("speed is bigger.",
        msgCF, width * 5.0f / 6, height * 0.1f + msgStep * 5f,
        HA.Center, VA.Center, 6, 13),

      Fade(timeOff = 0.0, timeOn = 0.5),
      Fade(timeOff = 14.0, timeOn = 13.5),
    )
    props.runImage(14, allGraphics)
  end apply
