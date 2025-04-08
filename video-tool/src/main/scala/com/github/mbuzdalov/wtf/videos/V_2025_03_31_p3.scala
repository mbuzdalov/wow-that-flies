package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_31_p3:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 2) // first was not used due to blinking GPS
    val logTimeOffset = autoArmedTimes(1) - props.armTime

    val stickMiddle = (width * 0.42).toInt
    val stickSize = 201 * width / 1920 / 2
    val stickGap = 19 * width / 1920 / 2

    val rpGap = 11 * width / 1280
    val rpWidth = width / 6
    val rpHeight = height / 3
    val fontSize = 16f * width / 1280
    val rpBackground = new Color(210, 255, 200, 180)

    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickMiddle - stickSize - stickGap, stickMiddle + stickGap, height - stickSize - rpGap)

    val npPlot = Plot.createNEPlot(reader, logTimeOffset, 'N', 'P', -20, +5,
      rpGap, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 23)
    val nvPlot = Plot.createNEPlot(reader, logTimeOffset, 'N', 'V', -4, +4,
      rpGap * 2 + rpWidth, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 7)
    val epPlot = Plot.createNEPlot(reader, logTimeOffset, 'E', 'P', -4.5, +20.5,
      width - 2 * rpGap - 2 * rpWidth, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 23)
    val evPlot = Plot.createNEPlot(reader, logTimeOffset, 'E', 'V', -2, +2,
      width - rpGap - rpWidth, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 3)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))
    val fmCF = TextMessage.ColorFont(msgFontSize, new Color(255, 255, 0))

    val allGraphics = GraphicsConsumer.compose(
      locally:
        val qw = (rpGap * 2.0 + rpWidth * 2.0) / width
        val qh = (height - stickSize - rpGap).toDouble / height
        ColoredRectangle(qw, qh, 1 - 2 * qw, stickSize.toDouble / height, new Color(0, 0, 0, 200)).enabledBetween(8, 184),
      sticks.enabledBetween(8, 184),
      npPlot.enabledBetween(8, 184),
      nvPlot.enabledBetween(8, 184),
      epPlot.enabledBetween(8, 184),
      evPlot.enabledBetween(8, 184),
      FlightMode(reader, logTimeOffset, fmCF,
        stickMiddle + stickSize + 2 * stickGap, height - stickSize - rpGap + stickSize / 2, HA.Left, VA.Bottom).enabledBetween(8, 184),
      VerticalSpeed(reader, logTimeOffset, fmCF,
        stickMiddle + stickSize + 2 * stickGap, height - stickSize - rpGap + stickSize / 2, HA.Left, VA.Top).enabledBetween(8, 184),

      LeftBlanket(0.4, new Color(240, 240, 240, 200), 0.5, 1.0, 7.5, 7.9),
      TextMessage("For this test,",
        msgCF, width * 0.2, height * 0.25, HA.Center, VA.Center)
        .enabledBetween(1, 7.5),
      TextMessage("the following gains are set:",
        msgCF, width * 0.2, height * 0.25 + msgStep, HA.Center, VA.Center)
        .enabledBetween(1, 7.5),
      TextMessage("PSC_VELXY_P = 1.5",
        msgCF, width * 0.02, height * 0.25 + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3, 7.5),
      TextMessage("PSC_VELXY_I = 1.0",
        msgCF, width * 0.02, height * 0.25 + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3.5, 7.5),
      TextMessage("PSC_VELXY_D = 0.1",
        msgCF, width * 0.02, height * 0.25 + 4.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(4, 7.5),

      TopBlanket(0.25, new Color(240, 240, 240, 200), 14, 14.5, 23.5, 23.9),
      TextMessage("The initial oscillations seem to be related",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center)
        .enabledBetween(14.5, 23.5),
      TextMessage("to suboptimal PID scaling with voltage, as MOT_OPTIONS bit not being set.",
        msgCF, width * 0.5, height * 0.05 + msgStep, HA.Center, VA.Center)
        .enabledBetween(14.5, 23.5),
      TextMessage("Otherwise, position/velocity holding is quite good!",
        msgCF, width * 0.5, height * 0.05 + 2 * msgStep, HA.Center, VA.Center)
        .enabledBetween(18.5, 23.5),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 40, 40.5, 48.5, 48.9),
      TextMessage("Velocity holding is actually not totally perfect:",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center)
        .enabledBetween(40.5, 48.5),
      TextMessage("target/actual deviations can be up to 2 m/s, up to two meters in position.",
        msgCF, width * 0.5, height * 0.05 + msgStep, HA.Center, VA.Center)
        .enabledBetween(40.5, 48.5),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 72, 72.5, 82.5, 82.9),
      TextMessage("With speed, the machine loses some altitude.",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center)
        .enabledBetween(72.5, 82.5),
      TextMessage("This is a common known tuning issue, I will try to fix it someday.",
        msgCF, width * 0.5, height * 0.05 + msgStep, HA.Center, VA.Center)
        .enabledBetween(72.5, 82.5),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 108, 108.5, 118.5, 118.9),
      TextMessage("Testing different yaw orientations",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center)
        .enabledBetween(108.5, 118.5),
      TextMessage("to see whether there are any direction discrepancies (none are visible).",
        msgCF, width * 0.5, height * 0.05 + msgStep, HA.Center, VA.Center)
        .enabledBetween(108.5, 118.5),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 129.5, 129.9, 135.5, 135.9),
      TextMessage("Internal go/no-go test for a higher altitude landing test...",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center)
        .enabledBetween(129.5, 135.5),
      TextMessage("Decided GO!",
        msgCF, width * 0.5, height * 0.05 + msgStep, HA.Center, VA.Center)
        .enabledBetween(133.5, 135.5),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 153, 153.5, 158.5, 158.9),
      TextMessage("Wanted to switch to Land, but mistakenly switched to AltHold.",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center)
        .enabledBetween(153.5, 158.5),
      TextMessage("This is flown without an FPV feed, so I did not realize this quickly.",
        msgCF, width * 0.5, height * 0.05 + msgStep, HA.Center, VA.Center)
        .enabledBetween(155.5, 158.5),

      TopBlanket(0.27, new Color(240, 240, 240, 200), 180, 180.5, 189.0, 189.5),
      TextMessage("This looks like a proper landing to me.",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center)
        .enabledBetween(180.5, 189),
      TextMessage("While some parts of GPS velocity/position holding do require more tuning,",
        msgCF, width * 0.5, height * 0.05 + 1.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(183, 189),
      TextMessage("overall this is a success, and the tuning can be lived with.",
        msgCF, width * 0.5, height * 0.05 + 2.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(183, 189),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 189.5, timeOff = 190.0),
    )

    props.runVideo(allGraphics)
  end apply
