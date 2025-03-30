package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p3:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 7) // 6 real ones, 1 did not actually arm
    val logTimeOffset = autoArmedTimes(1) - props.armTime

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
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Top).enabledBetween(8, 36),
      NEDWidget(reader, logTimeOffset,
        minN = 11.5, maxN = 21.5,
        minE = -2, maxE = 8,
        minAlt = -2, maxAlt = 12,
        visMinX = 0.2, visMaxX = 0.8, visMinY = 0.3, visMaxY = 0.9,
        yawOverride = Some(_ => 180.0)).enabledBetween(10, 36),

      TopBlanket(0.27, new Color(240, 240, 240, 200), 1.0, 1.4, 7.5, 7.9),
      TextMessage("Second test, no configuration changes.",
        msgCF, width * 0.5, 1 * msgStep, HA.Center, VA.Center).enabledBetween(1.5, 7.5),
      TextMessage("This time, only few seconds of the flight are visible on the camera,",
        msgCF, width * 0.5, 2 * msgStep, HA.Center, VA.Center).enabledBetween(3, 7.5),
      TextMessage("so I will display the position (to scale) based on logs.",
        msgCF, width * 0.5, 3 * msgStep, HA.Center, VA.Center).enabledBetween(3, 7.5),

      BottomBlanket(0.16, new Color(240, 240, 240, 200), 36.0, 36.4, 40.5, 40.9),
      TextMessage("This time, both parts of the descent trajectory",
        msgCF, width * 0.5, height - 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(36.5, 40.5),
      TextMessage("were tested, and worked quite well.",
        msgCF, width * 0.5, height - 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(36.5, 40.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 40.5, timeOff = 41.0),
    )

    props.runVideo(allGraphics)
  end apply
