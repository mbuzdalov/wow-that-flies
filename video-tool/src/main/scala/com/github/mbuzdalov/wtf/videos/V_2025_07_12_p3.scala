package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_07_12_p3:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val leftH = TextMessage.HorizontalAlignment.Left
    val centerV = TextMessage.VerticalAlignment.Center
    val blanketBackground = new Color(255, 255, 255, 200)

    val allGraphics = GraphicsConsumer.compose(
      LeftBlanket(0.36, blanketBackground, 0.5, 0.8, 19.2, 19.5),
      TextMessage("Test 2.",
        msgCF, 0.02 * width, 0.05 * height, leftH, centerV)
        .enabledBetween(1, 19),
      TextMessage("I decided that yaw has to go:",
        msgCF, 0.02 * width, 0.05 * height + 1.5 * msgStep, leftH, centerV)
        .enabledBetween(2, 19),
      TextMessage("P = 0.03,",
        msgCF, 0.05 * width, 0.05 * height + 2.5 * msgStep, leftH, centerV)
        .enabledBetween(3, 19),
      TextMessage("I = 0.005,",
        msgCF, 0.05 * width, 0.05 * height + 3.5 * msgStep, leftH, centerV)
        .enabledBetween(3.5, 19),
      TextMessage("D = 0.0003.",
        msgCF, 0.05 * width, 0.05 * height + 4.5 * msgStep, leftH, centerV)
        .enabledBetween(4, 19),
      TextMessage("Well, it was not yaw...",
        msgCF, 0.02 * width, 0.05 * height + 7 * msgStep, leftH, centerV)
        .enabledBetween(15, 19),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 19.5, timeOff = 20.0),
    )

    props.runVideo(allGraphics)
  end apply
