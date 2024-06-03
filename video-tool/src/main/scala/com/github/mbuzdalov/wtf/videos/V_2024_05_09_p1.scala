package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_05_09_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(t => 1.0 + t / 100, t => t / 5 / 360 * math.Pi),
      TextMessage("On May 8th,",
        msgCF, width * 0.11f, height * 0.1f,
        HA.Center, VA.Center, 1, 14.5),
      TextMessage("CX7 received",
        msgCF, width * 0.11f, height * 0.1f + msgStep * 1f,
        HA.Center, VA.Center, 1, 14.5),
      TextMessage("a video system,",
        msgCF, width * 0.11f, height * 0.1f + msgStep * 2f,
        HA.Center, VA.Center, 1, 14.5),
      TextMessage("the HDZero",
        msgCF, width * 0.11f, height * 0.1f + msgStep * 3f,
        HA.Center, VA.Center, 1, 14.5),
      TextMessage("Whoop Lite",
        msgCF, width * 0.11f, height * 0.1f + msgStep * 4f,
        HA.Center, VA.Center, 1, 14.5),
      TextMessage("bundle.",
        msgCF, width * 0.11f, height * 0.1f + msgStep * 5f,
        HA.Center, VA.Center, 1, 14.5),
      TextMessage("It's a digital",
        msgCF, width * 9.0f / 10, height * 0.1f,
        HA.Center, VA.Center, 7, 14.5),
      TextMessage("low-latency",
        msgCF, width * 9.0f / 10, height * 0.1f + msgStep * 1f,
        HA.Center, VA.Center, 7, 14.5),
      TextMessage("system",
        msgCF, width * 9.0f / 10, height * 0.1f + msgStep * 2f,
        HA.Center, VA.Center, 7, 14.5),
      TextMessage("streaming",
        msgCF, width * 9.0f / 10, height * 0.1f + msgStep * 3f,
        HA.Center, VA.Center, 7, 14.5),
      TextMessage("in 960x720",
        msgCF, width * 9.0f / 10, height * 0.1f + msgStep * 4f,
        HA.Center, VA.Center, 7, 14.5),
      TextMessage("at 60 fps",
        msgCF, width * 9.0f / 10, height * 0.1f + msgStep * 5f,
        HA.Center, VA.Center, 7, 14.5),
      TextMessage("by default.",
        msgCF, width * 9.0f / 10, height * 0.1f + msgStep * 6f,
        HA.Center, VA.Center, 7, 14.5),
      Fade(timeOff = 15.0, timeOn = 14.2),
    )
    props.runImage(15, allGraphics)
  end apply
