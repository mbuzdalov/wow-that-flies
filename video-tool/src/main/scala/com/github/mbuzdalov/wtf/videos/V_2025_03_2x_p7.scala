package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p7:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 7) // 6 real ones, 1 did not actually arm
    val logTimeOffset = autoArmedTimes(6) - props.armTime

    val stickMiddle = (width * 0.42).toInt
    val stickSize = 201 * width / 1920 / 2
    val stickGap = 21 * width / 1920 / 2
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickMiddle - stickSize - stickGap, stickMiddle + stickGap, stickGap)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      sticks.enabledBetween(18, 37),
      FlightMode(reader, logTimeOffset, msgCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Bottom).enabledBetween(18, 37),
      VerticalSpeed(reader, logTimeOffset, msgCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Top).enabledBetween(18, 37),
      NEDWidget(reader, logTimeOffset,
        minN = 6.5, maxN = 6.5 + 20.0 * 20 / 21,
        minE = 3, maxE = 3 + 20.0 * 20 / 21,
        minAlt = -2, maxAlt = 18,
        visMinX = 0.1, visMaxX = 0.9, visMinY = 0.3, visMaxY = 0.9,
        yawOverride = Some(_ => 170.0)).enabledBetween(23, 37.5),

      TopBlanket(0.35, new Color(240, 240, 240, 200), 1.0, 1.4, 11.5, 11.9),
      TextMessage("So, we had large slow oscillations during landing,",
        msgCF, width * 0.5, 0.5 * msgStep, HA.Center, VA.Center).enabledBetween(1.5, 11.5),
      TextMessage("both during the initial and the final phases, but not in Stabilize.",
        msgCF, width * 0.5, 1.5 * msgStep, HA.Center, VA.Center).enabledBetween(1.5, 11.5),
      TextMessage("Despite that, SN1 did not fly away, and got no visible damage.",
        msgCF, width * 0.5, 3 * msgStep, HA.Center, VA.Center).enabledBetween(4.5, 11.5),
      TextMessage("This did look like a tuning issue, but not in low-level PID tuning.",
        msgCF, width * 0.5, 4.5 * msgStep, HA.Center, VA.Center).enabledBetween(7, 11.5),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 13.0, 13.4, 17.5, 17.9),
      TextMessage("Sixth test.",
        msgCF, width * 0.5, 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(13.5, 17.5),
      TextMessage("Was this just a fluke, or can this be reproduced?",
        msgCF, width * 0.5, 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(13.5, 17.5),

      BottomBlanket(0.16, new Color(240, 240, 240, 200), 38.0, 38.4, 41.5, 41.9),
      TextMessage("Oooooops...",
        msgCF, width * 0.5, height - 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(38.5, 41.5),
      TextMessage("Surely this requires a mishap investigation...",
        msgCF, width * 0.5, height - 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(38.5, 41.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 42.5, timeOff = 43.0),
    )

    props.runVideo(allGraphics)
  end apply
