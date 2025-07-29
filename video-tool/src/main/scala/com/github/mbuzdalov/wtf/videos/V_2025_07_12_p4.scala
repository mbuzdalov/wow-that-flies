package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_07_12_p4:
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
      LeftBlanket(0.36, blanketBackground, 0.5, 0.8, 23.2, 23.5),
      TextMessage("Test 3.",
        msgCF, 0.02 * width, 0.05 * height, leftH, centerV)
        .enabledBetween(1, 23),
      TextMessage("After inspection of logs,",
        msgCF, 0.02 * width, 0.05 * height + 1.5 * msgStep, leftH, centerV)
        .enabledBetween(2, 23),
      TextMessage("PIDs were set as follows",
        msgCF, 0.02 * width, 0.05 * height + 2.5 * msgStep, leftH, centerV)
        .enabledBetween(2, 23),
      TextMessage("for all axes:",
        msgCF, 0.02 * width, 0.05 * height + 3.5 * msgStep, leftH, centerV)
        .enabledBetween(2, 23),
      TextMessage("P = 0.03,",
        msgCF, 0.05 * width, 0.05 * height + 5 * msgStep, leftH, centerV)
        .enabledBetween(3, 23),
      TextMessage("I = 0.03,",
        msgCF, 0.05 * width, 0.05 * height + 6 * msgStep, leftH, centerV)
        .enabledBetween(3.5, 23),
      TextMessage("D = 0.0001.",
        msgCF, 0.05 * width, 0.05 * height + 7 * msgStep, leftH, centerV)
        .enabledBetween(4, 23),
      TextMessage("Well, this is much better!",
        msgCF, 0.02 * width, 0.05 * height + 9.5 * msgStep, leftH, centerV)
        .enabledBetween(17, 23),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 46.5, timeOff = 47.0),
    )

    props.runVideo(allGraphics)
  end apply
