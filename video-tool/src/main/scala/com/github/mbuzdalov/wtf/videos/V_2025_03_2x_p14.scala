package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p14:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 4) // 4 tests, first failed early.
    val logTimeOffset = autoArmedTimes(3) - props.armTime

    val stickMiddle = (width * 0.42).toInt
    val stickSize = 201 * width / 1920 / 2
    val stickGap = 21 * width / 1920 / 2
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickMiddle - stickSize - stickGap, stickMiddle + stickGap, stickGap)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val fmCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40), new Color(255, 255, 255, 150))

    val allGraphics = GraphicsConsumer.compose(
      sticks.enabledBetween(6, 20),
      FlightMode(reader, logTimeOffset, fmCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Bottom).enabledBetween(6, 20),
      VerticalSpeed(reader, logTimeOffset, fmCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Top).enabledBetween(6, 20),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 1.0, 1.4, 5.5, 5.9),
      TextMessage("Test 3.",
        msgCF, width * 0.5, 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(1.5, 5.5),
      TextMessage("Yet another run.",
        msgCF, width * 0.5, 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(2.5, 5.5),

      TopBlanket(0.33, new Color(240, 240, 240, 200), 21.0, 21.4, 36.5, 36.9),
      TextMessage("Oops...",
        msgCF, width * 0.5, 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(21.5, 36.5),
      TextMessage("This did look somewhat like Test 6 yesterday.",
        msgCF, width * 0.5, 2 * msgStep, HA.Center, VA.Center).enabledBetween(22.5, 36.5),
      TextMessage("No wild oscillations, but nothing like a vertical landing either.",
        msgCF, width * 0.5, 3 * msgStep, HA.Center, VA.Center).enabledBetween(26.5, 36.5),
      TextMessage("Wind wasn't a reason either, it was not that bad.",
        msgCF, width * 0.5, 4 * msgStep, HA.Center, VA.Center).enabledBetween(29.5, 36.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 37.5, timeOff = 38.0),
    )

    props.runVideo(allGraphics)
  end apply
