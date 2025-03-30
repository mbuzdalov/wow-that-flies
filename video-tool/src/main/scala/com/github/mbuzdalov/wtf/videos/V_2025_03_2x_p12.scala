package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p12:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 4) // 4 tests, first failed early.
    val logTimeOffset = autoArmedTimes(1) - props.armTime

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
      sticks.enabledBetween(6, 127),
      FlightMode(reader, logTimeOffset, fmCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Bottom).enabledBetween(6, 127),
      VerticalSpeed(reader, logTimeOffset, fmCF,
        stickMiddle + stickSize + 2 * stickGap, stickGap + stickSize / 2, HA.Left, VA.Top).enabledBetween(6, 127),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 1.0, 1.4, 5.5, 5.9),
      TextMessage("March 25, 2025.",
        msgCF, width * 0.5, 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(1.5, 5.5),
      TextMessage("Tests with XY positioning gains divided by 5.",
        msgCF, width * 0.5, 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(2.5, 5.5),

      BottomBlanket(0.16, new Color(240, 240, 240, 200), 10.0, 10.4, 16.5, 16.9),
      TextMessage("First, I am flying some time in Stabilize,",
        msgCF, width * 0.5, height - 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(10.5, 16.5),
      TextMessage("such that the absolute yaw gets established.",
        msgCF, width * 0.5, height - 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(10.5, 16.5),

      BottomBlanket(0.16, new Color(240, 240, 240, 200), 24.0, 24.4, 43.5, 43.9),
      TextMessage("“EKF3 IMU0 is now using GPS”, first transitioning to AltHold...",
        msgCF, width * 0.5, height - 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(24.5, 43.5),
      TextMessage("... and then to Loiter, and nothing bad happens!",
        msgCF, width * 0.5, height - 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(39.5, 43.5),

      BottomBlanket(0.16, new Color(240, 240, 240, 200), 60.0, 60.4, 69.5, 69.9),
      TextMessage("But now Loiter does not hold position very accurately either,",
        msgCF, width * 0.5, height - 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(60.5, 69.5),
      TextMessage("so dividing by 5 was probably too much.",
        msgCF, width * 0.5, height - 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(60.5, 69.5),

      BottomBlanket(0.16, new Color(240, 240, 240, 200), 104.0, 104.4, 112.5, 112.9),
      TextMessage("Anyway, this should not hurt during the landing.",
        msgCF, width * 0.5, height - 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(104.5, 112.5),
      TextMessage("Switching from FPV to line-of-sight so that I can reposition while landing...",
        msgCF, width * 0.5, height - 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(104.5, 112.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 126.5, timeOff = 127.0),
    )

    props.runVideo(allGraphics)
  end apply
