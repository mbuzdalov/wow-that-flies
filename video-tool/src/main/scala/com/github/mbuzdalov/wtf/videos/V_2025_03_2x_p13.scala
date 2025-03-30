package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p13:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 4) // 4 tests, first failed early.
    val logTimeOffset = autoArmedTimes(2) - props.armTime

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
      sticks.enabledBetween(6, 30),
      FlightMode(reader, logTimeOffset, fmCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Bottom).enabledBetween(6, 30),
      VerticalSpeed(reader, logTimeOffset, fmCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Top).enabledBetween(6, 30),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 1.0, 1.4, 5.5, 5.9),
      TextMessage("Test 2.",
        msgCF, width * 0.5, 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(1.5, 5.5),
      TextMessage("Now, flying straight up and down.",
        msgCF, width * 0.5, 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(2.5, 5.5),

      BottomBlanket(0.1, new Color(240, 240, 240, 200), 13.0, 13.4, 17.5, 17.9),
      TextMessage("Max altitude in this test: 15.5 meters.",
        msgCF, width * 0.5, height - 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(13.5, 17.5),

      BottomBlanket(0.16, new Color(240, 240, 240, 200), 23.0, 23.4, 28.5, 28.9),
      TextMessage("Both landing stages were tested.",
        msgCF, width * 0.5, height - 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(23.5, 28.5),
      TextMessage("XY positioning was engaged all the time, and sort of worked.",
        msgCF, width * 0.5, height - 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(25.5, 28.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 29.5, timeOff = 30.0),
    )

    props.runVideo(allGraphics)
  end apply
