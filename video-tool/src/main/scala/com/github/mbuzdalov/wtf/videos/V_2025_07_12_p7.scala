package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_07_12_p7:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val blanketBackground = new Color(255, 255, 255, 200)

    val allGraphics = GraphicsConsumer.compose(
      LeftBlanket(0.48, blanketBackground, 0.5, 0.8, 19.2, 19.5),
      TextMessage("AutoTune Phase 2: Pitch:",
        msgCF, 0.02 * width, 0.05 * height, HA.Left, VA.Center)
        .enabledBetween(1, 19),

      TextMessage("The results are:",
        msgCF, 0.02 * width, 0.05 * height + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(8, 19),

      TextMessage("ATC_RAT_PIT_{P,I}:",
        msgCF, 0.06 * width, 0.05 * height + 3 * msgStep, HA.Left, VA.Center)
        .enabledBetween(9, 19),
      TextMessage("0.0725",
        msgCF, 0.35 * width, 0.05 * height + 3 * msgStep, HA.Left, VA.Center)
        .enabledBetween(9.5, 19),

      TextMessage("ATC_RAT_PIT_D:",
        msgCF, 0.06 * width, 0.05 * height + 4 * msgStep, HA.Left, VA.Center)
        .enabledBetween(11, 19),
      TextMessage("0.0025",
        msgCF, 0.35 * width, 0.05 * height + 4 * msgStep, HA.Left, VA.Center)
        .enabledBetween(11.5, 19),

      TextMessage("ATC_ANG_PIT_P:",
        msgCF, 0.06 * width, 0.05 * height + 5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(13, 19),
      TextMessage("23.82",
        msgCF, 0.35 * width, 0.05 * height + 5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(13.5, 19),

      TextMessage("ATC_ACCEL_P_MAX:",
        msgCF, 0.06 * width, 0.05 * height + 6 * msgStep, HA.Left, VA.Center)
        .enabledBetween(15, 19),
      TextMessage("195319",
        msgCF, 0.35 * width, 0.05 * height + 6 * msgStep, HA.Left, VA.Center)
        .enabledBetween(15.5, 19),


      Fade(timeOn = 0.2, timeOff = 0),
      Fade(timeOn = 19.8, timeOff = 20.0),
    )

    props.runVideo(allGraphics)
  end apply
