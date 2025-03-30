package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p5:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 7) // 6 real ones, 1 did not actually arm
    val logTimeOffset = autoArmedTimes(4) - props.armTime

    val stickMiddle = (width * 0.42).toInt
    val stickSize = 201 * width / 1920 / 2
    val stickGap = 21 * width / 1920 / 2
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickMiddle - stickSize - stickGap, stickMiddle + stickGap, stickGap)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      sticks.enabledBetween(7, 33),
      FlightMode(reader, logTimeOffset, msgCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Bottom).enabledBetween(7, 33),
      VerticalSpeed(reader, logTimeOffset, msgCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Top).enabledBetween(7, 33),
      NEDWidget(reader, logTimeOffset,
        6.5, 6.5 + 20.0 * 10 / 14, -2, -2 + 20.0 * 10 / 14, -2, 18,
        0.15, 0.85, 0.15, 0.95).enabledBetween(11, 26),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 1.0, 1.4, 6.5, 6.9),
      TextMessage("Fourth test.",
        msgCF, width * 0.5, 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(1.5, 6.5),
      TextMessage("Hoped to start in Loiter, but failed again, so just repeating.",
        msgCF, width * 0.5, 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(1.5, 6.5),

      BottomBlanket(0.27, new Color(240, 240, 240, 200), 27.0, 27.4, 34.5, 34.9),
      TextMessage("I occasionally dropped throttle to zero before",
        msgCF, width * 0.5, height - 3 * msgStep, HA.Center, VA.Center).enabledBetween(27.5, 34.5),
      TextMessage("switching to Land, but it went OK, perhaps non-surprisingly.",
        msgCF, width * 0.5, height - 2 * msgStep, HA.Center, VA.Center).enabledBetween(27.5, 34.5),
      TextMessage("Otherwise, very clean transitions, descent speeds etc.",
        msgCF, width * 0.5, height - 1 * msgStep, HA.Center, VA.Center).enabledBetween(30.5, 34.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 35.5, timeOff = 36.0),
    )

    props.runVideo(allGraphics)
  end apply
