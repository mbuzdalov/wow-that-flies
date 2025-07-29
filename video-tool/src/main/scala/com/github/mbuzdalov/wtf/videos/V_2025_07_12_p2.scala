package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_07_12_p2:
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
      LeftBlanket(0.36, blanketBackground, 0.5, 0.8, 17.2, 17.5),
      TextMessage("Test 1.",
        msgCF, 0.02 * width, 0.05 * height, leftH, centerV)
        .enabledBetween(1, 17),
      TextMessage("The frame code in all tests",
        msgCF, 0.02 * width, 0.05 * height + 1.5 * msgStep, leftH, centerV)
        .enabledBetween(2, 17),
      TextMessage("keeps the rear motor upright",
        msgCF, 0.02 * width, 0.05 * height + 2.5 * msgStep, leftH, centerV)
        .enabledBetween(2, 17),
      TextMessage("and uses the same tilt angle",
        msgCF, 0.02 * width, 0.05 * height + 3.5 * msgStep, leftH, centerV)
        .enabledBetween(2, 17),
      TextMessage("for front motors.",
        msgCF, 0.02 * width, 0.05 * height + 4.5 * msgStep, leftH, centerV)
        .enabledBetween(2, 17),
      TextMessage("The initial PIDs are:",
        msgCF, 0.02 * width, 0.05 * height + 6 * msgStep, leftH, centerV)
        .enabledBetween(3.5, 17),
      TextMessage("P = I = 0.05,",
        msgCF, 0.04 * width, 0.05 * height + 7 * msgStep, leftH, centerV)
        .enabledBetween(4, 17),
      TextMessage("D = 0.001,",
        msgCF, 0.04 * width, 0.05 * height + 8 * msgStep, leftH, centerV)
        .enabledBetween(4, 17),
      TextMessage("for pitch, roll, yaw.",
        msgCF, 0.04 * width, 0.05 * height + 9 * msgStep, leftH, centerV)
        .enabledBetween(4, 17),
      TextMessage("Clearly, some values",
        msgCF, 0.04 * width, 0.05 * height + 11 * msgStep, leftH, centerV)
        .enabledBetween(13, 17),
      TextMessage("need to be smaller.",
        msgCF, 0.04 * width, 0.05 * height + 12 * msgStep, leftH, centerV)
        .enabledBetween(13, 17),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 17.5, timeOff = 18.0),
    )

    props.runVideo(allGraphics)
  end apply
