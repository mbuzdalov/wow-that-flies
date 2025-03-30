package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p4:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 7) // 6 real ones, 1 did not actually arm
    val logTimeOffset = autoArmedTimes(2) - props.armTime

    val stickMiddle = (width * 0.42).toInt
    val stickSize = 201 * width / 1920 / 2
    val stickGap = 21 * width / 1920 / 2
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickMiddle - stickSize - stickGap, stickMiddle + stickGap, stickGap)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      sticks.enabledBetween(8, 39),
      FlightMode(reader, logTimeOffset, msgCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Bottom).enabledBetween(8, 39),
      VerticalSpeed(reader, logTimeOffset, msgCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Top).enabledBetween(8, 39),
      NEDWidget(reader, logTimeOffset,
        6.5, 23.643, -2, 15.143, -2, 22,
        0.15, 0.85, 0.15, 0.95).enabledBetween(9, 39),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 1.0, 1.4, 7.5, 7.9),
      TextMessage("Third test, trying to get even higher.",
        msgCF, width * 0.5, 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(1.5, 7.5),
      TextMessage("Even less of this test is visible on the camera...",
        msgCF, width * 0.5, 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(3, 7.5),

      BottomBlanket(0.33, new Color(240, 240, 240, 200), 40.0, 40.4, 48.5, 48.9),
      TextMessage("The too-early transition was due to tree branches",
        msgCF, width * 0.5, height - 4 * msgStep, HA.Center, VA.Center).enabledBetween(40.5, 48.5),
      TextMessage("detected by the rangefinder on descent.",
        msgCF, width * 0.5, height - 3 * msgStep, HA.Center, VA.Center).enabledBetween(40.5, 48.5),
      TextMessage("Otherwise, the settings behaved quite well so far,",
        msgCF, width * 0.5, height - 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(44.5, 48.5),
      TextMessage("and 3 m/s of descent did not feel too slow!",
        msgCF, width * 0.5, height - 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(44.5, 48.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 49.5, timeOff = 50.0),
    )

    props.runVideo(allGraphics)
  end apply
