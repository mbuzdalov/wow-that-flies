package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 7) // 6 real ones, 1 did not actually arm
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val stickMiddle = (width * 0.42).toInt
    val stickSize = 201 * width / 1920 / 2
    val stickGap = 21 * width / 1920 / 2
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickMiddle - stickSize - stickGap, stickMiddle + stickGap, stickGap)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val blinkInnerR = width * 0.045
    val blinkOuterR = width * 0.05

    val allGraphics = GraphicsConsumer.compose(
      sticks.enabledBetween(42, 71),
      FlightMode(reader, logTimeOffset, msgCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Bottom).enabledBetween(42, 71),
      VerticalSpeed(reader, logTimeOffset, msgCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Top).enabledBetween(42, 68),

      TopBlanket(0.5, new Color(240, 240, 240, 200), 1.0, 1.4, 17.5, 17.9),
      TextMessage("Evening, March 24, 2025.",
        msgCF, width * 0.5, 0.5 * msgStep, HA.Center, VA.Center).enabledBetween(1.5, 17.5),
      TextMessage("Based on the log analysis, I set the following parameters:",
        msgCF, width * 0.5, 2 * msgStep, HA.Center, VA.Center).enabledBetween(2, 17.5),
      TextMessage("LAND_SPEED_HIGH = 300",
        msgCF, width * 0.1, 3 * msgStep, HA.Left, VA.Center).enabledBetween(4, 17.5),
      TextMessage("(Descent speed when high enough: 3 m/s)",
        msgCF, width * 0.5, 3 * msgStep, HA.Left, VA.Center).enabledBetween(5, 17.5),
      TextMessage("LAND_SPEED = 50",
        msgCF, width * 0.1, 4 * msgStep, HA.Left, VA.Center).enabledBetween(7, 17.5),
      TextMessage("(Descent speed when landing: 0.5 m/s)",
        msgCF, width * 0.5, 4 * msgStep, HA.Left, VA.Center).enabledBetween(8, 17.5),
      TextMessage("LAND_ALT_LOW = 250",
        msgCF, width * 0.1, 5 * msgStep, HA.Left, VA.Center).enabledBetween(10, 17.5),
      TextMessage("(Transition: 2.5 m above the ground)",
        msgCF, width * 0.5, 5 * msgStep, HA.Left, VA.Center).enabledBetween(11, 17.5),
      TextMessage("These are quite conservative, but should be OK for first tests.",
        msgCF, width * 0.5, 6.5 * msgStep, HA.Center, VA.Center).enabledBetween(14, 17.5),

      TopBlanket(0.4, new Color(240, 240, 240, 200), 19.0, 19.4, 31.5, 31.9),
      TextMessage("An important note: SN1 has GPS with some interference from HDZero,",
        msgCF, width * 0.5, 0.5 * msgStep, HA.Center, VA.Center).enabledBetween(19.5, 31.5),
      TextMessage("and a compass with high interference from everything,",
        msgCF, width * 0.5, 1.5 * msgStep, HA.Center, VA.Center).enabledBetween(19.5, 31.5),
      TextMessage("so the compass is disabled, and direction can only be",
        msgCF, width * 0.5, 2.5 * msgStep, HA.Center, VA.Center).enabledBetween(21.5, 31.5),
      TextMessage("inferred via the Gaussian Sum Filter, like on planes.",
        msgCF, width * 0.5, 3.5 * msgStep, HA.Center, VA.Center).enabledBetween(21.5, 31.5),
      TextMessage("I tried to arm in Loiter, but that failed.",
        msgCF, width * 0.5, 5 * msgStep, HA.Center, VA.Center).enabledBetween(25, 31.5),

      TopBlanket(0.27, new Color(240, 240, 240, 200), 34.0, 34.4, 41.5, 41.9),
      TextMessage("The tests will now be done as follows:",
        msgCF, width * 0.5, 1 * msgStep, HA.Center, VA.Center).enabledBetween(34.5, 41.5),
      TextMessage("Arm and lift off in Stabilize, reach some altitude,",
        msgCF, width * 0.5, 2 * msgStep, HA.Center, VA.Center).enabledBetween(34.5, 41.5),
      TextMessage("switch to Land and reposition if needed.",
        msgCF, width * 0.5, 3 * msgStep, HA.Center, VA.Center).enabledBetween(34.5, 41.5),

      BottomBlanket(0.27, new Color(240, 240, 240, 200), 48.0, 48.4, 55.5, 55.9),
      TextMessage("This time I only have a single camera,",
        msgCF, width * 0.5, height - 3 * msgStep, HA.Center, VA.Center).enabledBetween(48.5, 55.5),
      TextMessage("and the goggles were not recording the FPV footage",
        msgCF, width * 0.5, height - 2 * msgStep, HA.Center, VA.Center).enabledBetween(48.5, 55.5),
      TextMessage("for first few tests, sorry for that.",
        msgCF, width * 0.5, height - 1 * msgStep, HA.Center, VA.Center).enabledBetween(48.5, 55.5),

      BottomBlanket(0.16, new Color(240, 240, 240, 200), 60.0, 60.4, 66.5, 66.9),
      TextMessage("Only the slow descent speed was engaged this time,",
        msgCF, width * 0.5, height - 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(60.5, 66.5),
      TextMessage("in the next test I will try to get higher.",
        msgCF, width * 0.5, height - 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(60.5, 66.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 70.5, timeOff = 71.0),
    )

    props.runVideo(allGraphics)
  end apply
