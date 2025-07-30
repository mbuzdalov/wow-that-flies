package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_07_12_p5:
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
      LeftBlanket(0.36, blanketBackground, 0.5, 0.8, 25.2, 25.5),
      TextMessage("Test 4.",
        msgCF, 0.02 * width, 0.05 * height, leftH, centerV)
        .enabledBetween(1, 25),
      TextMessage("To reduce imperfections,",
        msgCF, 0.02 * width, 0.05 * height + 1.5 * msgStep, leftH, centerV)
        .enabledBetween(2, 25),
      TextMessage("I increased PIDs a bit",
        msgCF, 0.02 * width, 0.05 * height + 2.5 * msgStep, leftH, centerV)
        .enabledBetween(2, 25),
      TextMessage("for all axes:",
        msgCF, 0.02 * width, 0.05 * height + 3.5 * msgStep, leftH, centerV)
        .enabledBetween(2, 25),
      TextMessage("P = 0.035,",
        msgCF, 0.05 * width, 0.05 * height + 5 * msgStep, leftH, centerV)
        .enabledBetween(3, 25),
      TextMessage("I = 0.035,",
        msgCF, 0.05 * width, 0.05 * height + 6 * msgStep, leftH, centerV)
        .enabledBetween(3.5, 25),
      TextMessage("D = 0.0002.",
        msgCF, 0.05 * width, 0.05 * height + 7 * msgStep, leftH, centerV)
        .enabledBetween(4, 25),
      TextMessage("Some oscillations do appear,",
        msgCF, 0.02 * width, 0.05 * height + 9.5 * msgStep, leftH, centerV)
        .enabledBetween(17, 25),
      TextMessage("which indicates that P",
        msgCF, 0.02 * width, 0.05 * height + 10.5 * msgStep, leftH, centerV)
        .enabledBetween(17, 25),
      TextMessage("should not be increased.",
        msgCF, 0.02 * width, 0.05 * height + 11.5 * msgStep, leftH, centerV)
        .enabledBetween(17, 25),

      LeftBlanket(0.34, blanketBackground, 35.5, 35.8, 43.2, 43.5),
      TextMessage("The PID values used",
        msgCF, 0.02 * width, 0.05 * height + 1.5 * msgStep, leftH, centerV)
        .enabledBetween(36, 43),
      TextMessage("as autotune seeds will be,",
        msgCF, 0.02 * width, 0.05 * height + 2.5 * msgStep, leftH, centerV)
        .enabledBetween(36, 43),
      TextMessage("for all axes:",
        msgCF, 0.02 * width, 0.05 * height + 3.5 * msgStep, leftH, centerV)
        .enabledBetween(36, 43),
      TextMessage("P = 0.03,",
        msgCF, 0.05 * width, 0.05 * height + 5 * msgStep, leftH, centerV)
        .enabledBetween(36, 43),
      TextMessage("I = 0.03,",
        msgCF, 0.05 * width, 0.05 * height + 6 * msgStep, leftH, centerV)
        .enabledBetween(36, 43),
      TextMessage("D = 0.0005.",
        msgCF, 0.05 * width, 0.05 * height + 7 * msgStep, leftH, centerV)
        .enabledBetween(36, 43),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 44.5, timeOff = 45.0),
    )

    props.runVideo(allGraphics)
  end apply
