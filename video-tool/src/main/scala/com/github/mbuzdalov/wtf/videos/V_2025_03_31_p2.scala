package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_31_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val stickMiddle = (width * 0.5).toInt
    val stickSize = 201 * width / 1920 / 2
    val stickGap = 21 * width / 1920 / 2
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickMiddle - stickSize - stickGap, stickMiddle + stickGap, stickGap)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      sticks.enabledBetween(7, 12.5),

      LeftBlanket(0.4, new Color(240, 240, 240, 200), 0.5, 1.0, 6.5, 6.9),
      TextMessage("For this test,",
        msgCF, width * 0.2, height * 0.25, HA.Center, VA.Center)
        .enabledBetween(1, 6.5),
      TextMessage("the following gains are set:",
        msgCF, width * 0.2, height * 0.25 + msgStep, HA.Center, VA.Center)
        .enabledBetween(1, 6.5),
      TextMessage("PSC_VELXY_P = 4",
        msgCF, width * 0.02, height * 0.25 + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3, 6.5),
      TextMessage("PSC_VELXY_I = 2",
        msgCF, width * 0.02, height * 0.25 + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3.5, 6.5),
      TextMessage("PSC_VELXY_D = 0.1",
        msgCF, width * 0.02, height * 0.25 + 4.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(4, 6.5),

      RightBlanket(0.5, new Color(240, 240, 240, 200), 12.5, 13.0, 17.0, 17.5),
      TextMessage("Well, the increase of P",
        msgCF, width * 0.75, height * 0.4, HA.Center, VA.Center)
        .enabledBetween(13, 17),
      TextMessage("was definitely by too much!",
        msgCF, width * 0.75, height * 0.4 + msgStep, HA.Center, VA.Center)
        .enabledBetween(13, 17),
      TextMessage("(But the endpoints are found)",
        msgCF, width * 0.75, height * 0.4 + 2.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(14.5, 17),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 17.5, timeOff = 18.0),
    )

    props.runVideo(allGraphics)
  end apply
