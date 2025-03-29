package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_01_p6:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(scale = t => 1.0 + t / 400, rotate = t => 0.0, scaleCenterX = (x, t) => 0.0),
      LeftBlanket(0.4, new Color(255, 255, 255, 200), 1.6, 2, 14.1, 14.5),
      TextMessage("Among the upgrades, there are",
        msgCF, width * 0.01, height * 0.22, HA.Left, VA.Center)
        .enabledBetween(2, 14.1),
      TextMessage("support arms and airflow vents.",
        msgCF, width * 0.01, height * 0.22 + msgStep, HA.Left, VA.Center)
        .enabledBetween(2, 14.1),
      TextMessage("The support arms help stabilize",
        msgCF, width * 0.01, height * 0.22 + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(4, 14.1),
      TextMessage("the cage during rough landings.",
        msgCF, width * 0.01, height * 0.22 + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(4, 14.1),
      TextMessage("The airflow vents must be there",
        msgCF, width * 0.01, height * 0.22 + 5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(6, 14.1),
      TextMessage("because an airtight cage would",
        msgCF, width * 0.01, height * 0.22 + 6 * msgStep, HA.Left, VA.Center)
        .enabledBetween(6, 14.1),
      TextMessage("reflect the airflow back to the,",
        msgCF, width * 0.01, height * 0.22 + 7 * msgStep, HA.Left, VA.Center)
        .enabledBetween(6, 14.1),
      TextMessage("vehicle, which destabilizes it.",
        msgCF, width * 0.01, height * 0.22 + 8 * msgStep, HA.Left, VA.Center)
        .enabledBetween(6, 14.1),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 14.5, timeOff = 15),
    )

    props.runImage(15, allGraphics)
  end apply
  