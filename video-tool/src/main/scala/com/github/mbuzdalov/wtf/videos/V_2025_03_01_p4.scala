package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, RightBlanket, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_01_p4:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(what = None, scale = t => 1.0 + t / 400, rotate = t => 0.0, scaleCenterX = (x, t) => 0.0),
      LeftBlanket(0.4, new Color(255, 255, 255, 200), 1.6, 2, 13.1, 13.5),
      TextMessage("The new flaps are longer,",
        msgCF, width * 0.01, height * 0.2, HA.Left, VA.Center)
        .enabledBetween(2, 13.1),
      TextMessage("have the E169 airfoil shape,",
        msgCF, width * 0.01, height * 0.2 + msgStep, HA.Left, VA.Center)
        .enabledBetween(2, 13.1),
      TextMessage("and are limited to 25 degrees.",
        msgCF, width * 0.01, height * 0.2 + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(2, 13.1),
      TextMessage("The flaps and the leg extensions",
        msgCF, width * 0.01, height * 0.2 + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(7, 13.1),
      TextMessage("are printed with PLA-Aero, which",
        msgCF, width * 0.01, height * 0.2 + 4.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(7, 13.1),
      TextMessage("did not increase the overall weight",
        msgCF, width * 0.01, height * 0.2 + 5.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(7, 13.1),
      TextMessage("too much (300 g dry, 410 g wet).",
        msgCF, width * 0.01, height * 0.2 + 6.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(7, 13.1),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 13.5, timeOff = 14),
    )

    props.runImage(14, allGraphics)
  end apply
  