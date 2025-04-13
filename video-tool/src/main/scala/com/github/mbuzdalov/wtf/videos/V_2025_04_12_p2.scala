package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_04_12_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val centerH = TextMessage.HorizontalAlignment.Center
    val centerV = TextMessage.VerticalAlignment.Center

    val allGraphics = GraphicsConsumer.compose(
      TransparentSeparator(0.5, 0, 0.5, 1, 0.01),

      TextMessage("Test 2.",
        msgCF, 0.75 * width, 0.05 * height, centerH, centerV)
        .enabledBetween(1, 5),
      TextMessage("This time, a gentle lift-off.",
        msgCF, 0.75 * width, 0.05 * height + msgStep, centerH, centerV)
        .enabledBetween(1.5, 5),

      TextMessage("The outflow keeps the cap from opening!",
        msgCF, 0.75 * width, 0.05 * height, centerH, centerV)
        .enabledBetween(9, 13),
      TextMessage("Have to move sideways to let it open...",
        msgCF, 0.75 * width, 0.05 * height + msgStep, centerH, centerV)
        .enabledBetween(9.5, 13),

      TextMessage("So, there is no danger that the cap",
        msgCF, 0.75 * width, 0.05 * height, centerH, centerV)
        .enabledBetween(16, 20),
      TextMessage("hits the machine on lift-off, instead,",
        msgCF, 0.75 * width, 0.05 * height + msgStep, centerH, centerV)
        .enabledBetween(16, 20),
      TextMessage("I even need to move away.",
        msgCF, 0.75 * width, 0.05 * height + 2 * msgStep, centerH, centerV)
        .enabledBetween(16, 20),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 20.5, timeOff = 21.0),
    )

    props.runVideo(allGraphics)
  end apply
