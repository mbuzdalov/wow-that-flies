package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_07_12_p6f:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val blanketBackground = new Color(255, 255, 255, 200)

    val allGraphics = GraphicsConsumer.compose(
      LeftBlanket(0.48, blanketBackground, 0.5, 0.8, 14.2, 14.5),
      TextMessage("AutoTune results for Roll:",
        msgCF, 0.02 * width, 0.05 * height, HA.Left, VA.Center)
        .enabledBetween(1, 14),

      TextMessage("ATC_RAT_RLL_{P,I}:",
        msgCF, 0.06 * width, 0.05 * height + 3 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3, 14),
      TextMessage("0.0167",
        msgCF, 0.35 * width, 0.05 * height + 3 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3.5, 14),

      TextMessage("ATC_RAT_RLL_D:",
        msgCF, 0.06 * width, 0.05 * height + 4 * msgStep, HA.Left, VA.Center)
        .enabledBetween(5, 14),
      TextMessage("0.000324",
        msgCF, 0.35 * width, 0.05 * height + 4 * msgStep, HA.Left, VA.Center)
        .enabledBetween(5.5, 14),

      TextMessage("ATC_ANG_RLL_P:",
        msgCF, 0.06 * width, 0.05 * height + 5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(7, 14),
      TextMessage("10.94",
        msgCF, 0.35 * width, 0.05 * height + 5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(7.5, 14),

      TextMessage("ATC_ACCEL_R_MAX:",
        msgCF, 0.06 * width, 0.05 * height + 6 * msgStep, HA.Left, VA.Center)
        .enabledBetween(9, 14),
      TextMessage("161475",
        msgCF, 0.35 * width, 0.05 * height + 6 * msgStep, HA.Left, VA.Center)
        .enabledBetween(9.5, 14),


      Fade(timeOn = 0.2, timeOff = 0),
      Fade(timeOn = 14.5, timeOff = 15.0),
    )

    props.runVideo(allGraphics)
  end apply
