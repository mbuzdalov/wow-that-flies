package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_11_14_CX7_p3:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(240, 240, 0), new Color(0, 0, 0, 120))

    val textMiddle = width * 0.22
    val textTop = height * 0.07
    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(t => 1.0 + t / 100, t => t / 5 / 360 * math.Pi),
      TextMessage("All the electronics were fine.",
        msgCF, textMiddle, textTop, HA.Center, VA.Center)
        .enabledBetween(1, 19.5),
      TextMessage("The frame cracked in several places,",
        msgCF, textMiddle, textTop + msgStep * 1.5, HA.Center, VA.Center)
        .enabledBetween(4, 19.5),
      TextMessage("so needed to be reprinted anyway.",
        msgCF, textMiddle, textTop + msgStep * 2.5, HA.Center, VA.Center)
        .enabledBetween(4, 19.5),
      TextMessage("This is an opportunity to fix",
        msgCF, textMiddle, textTop + msgStep * 4, HA.Center, VA.Center)
        .enabledBetween(10, 19.5),
      TextMessage("known design issues, so:",
        msgCF, textMiddle, textTop + msgStep * 5, HA.Center, VA.Center)
        .enabledBetween(10, 19.5),
      TextMessage("Goodbye, CX7!",
        msgCF, textMiddle, textTop + msgStep * 6.5, HA.Center, VA.Center)
        .enabledBetween(13, 19.5),
      TextMessage("Next up: CX8!",
        msgCF, textMiddle, textTop + msgStep * 7.5, HA.Center, VA.Center)
        .enabledBetween(15, 19.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 19.5, timeOff = 20),
    )

    props.runImage(20, allGraphics)
  end apply
  