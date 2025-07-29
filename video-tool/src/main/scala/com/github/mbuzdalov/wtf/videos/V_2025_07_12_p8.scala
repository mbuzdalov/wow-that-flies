package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_07_12_p8:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val blanketBackground = new Color(255, 255, 255, 200)

    val allGraphics = GraphicsConsumer.compose(
      LeftBlanket(0.35, blanketBackground, 0.5, 0.8, 11.2, 11.5),
      TextMessage("AutoTune Phase 3: Yaw D",
        msgCF, 0.02 * width, 0.05 * height, HA.Left, VA.Center)
        .enabledBetween(1, 11),
      TextMessage("For nerds: no Yaw E phase,",
        msgCF, 0.02 * width, 0.05 * height + 1.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(4, 11),
      TextMessage("because yaw is controlled",
        msgCF, 0.02 * width, 0.05 * height + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(4, 11),
      TextMessage("not by motor torque, so the",
        msgCF, 0.02 * width, 0.05 * height + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(4, 11),
      TextMessage("error filter is disabled.",
        msgCF, 0.02 * width, 0.05 * height + 4.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(4, 11),

      LeftBlanket(0.48, blanketBackground, 18.5, 18.8, 29.2, 29.5),
      TextMessage("The results are:",
        msgCF, 0.02 * width, 0.05 * height, HA.Left, VA.Center)
        .enabledBetween(19, 29),

      TextMessage("ATC_RAT_YAW_{P,I}:",
        msgCF, 0.06 * width, 0.05 * height + 1 * msgStep, HA.Left, VA.Center)
        .enabledBetween(20, 29),
      TextMessage("0.0465",
        msgCF, 0.35 * width, 0.05 * height + 1 * msgStep, HA.Left, VA.Center)
        .enabledBetween(20.5, 29),

      TextMessage("ATC_RAT_YAW_D:",
        msgCF, 0.06 * width, 0.05 * height + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(22, 29),
      TextMessage("0.00036",
        msgCF, 0.35 * width, 0.05 * height + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(22.5, 29),

      TextMessage("ATC_ANG_YAW_P:",
        msgCF, 0.06 * width, 0.05 * height + 3 * msgStep, HA.Left, VA.Center)
        .enabledBetween(24, 29),
      TextMessage("9.38",
        msgCF, 0.35 * width, 0.05 * height + 3 * msgStep, HA.Left, VA.Center)
        .enabledBetween(24.5, 29),

      TextMessage("ATC_ACCEL_Y_MAX:",
        msgCF, 0.06 * width, 0.05 * height + 4 * msgStep, HA.Left, VA.Center)
        .enabledBetween(25, 29),
      TextMessage("147340",
        msgCF, 0.35 * width, 0.05 * height + 4 * msgStep, HA.Left, VA.Center)
        .enabledBetween(25.5, 29),


      Fade(timeOn = 0.2, timeOff = 0),
      Fade(timeOn = 29.5, timeOff = 30.0),
    )

    props.runVideo(allGraphics)
  end apply
