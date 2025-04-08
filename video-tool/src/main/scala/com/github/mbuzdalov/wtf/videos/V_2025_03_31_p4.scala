package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_31_p4:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

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

    val npPlot = Plot.createNEPlot(reader, logTimeOffset, 'N', 'P', -4, +1,
      rpGap, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 4)
    val nvPlot = Plot.createNEPlot(reader, logTimeOffset, 'N', 'V', -1, +1,
      rpGap * 2 + rpWidth, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 1)
    val epPlot = Plot.createNEPlot(reader, logTimeOffset, 'E', 'P', -4, +38,
      width - 2 * rpGap - 2 * rpWidth, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 41)
    val evPlot = Plot.createNEPlot(reader, logTimeOffset, 'E', 'V', -3, +3,
      width - rpGap - rpWidth, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 5)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))
    val fmCF = TextMessage.ColorFont(msgFontSize, new Color(255, 255, 0))

    val allGraphics = GraphicsConsumer.compose(
      locally:
        val qw = (rpGap * 2.0 + rpWidth * 2.0) / width
        val qh = (height - stickSize - rpGap).toDouble / height
        ColoredRectangle(qw, qh, 1 - 2 * qw, stickSize.toDouble / height, new Color(0, 0, 0, 200)).enabledBetween(15, 92),
      sticks.enabledBetween(15, 92),
      npPlot.enabledBetween(15, 92),
      nvPlot.enabledBetween(15, 92),
      epPlot.enabledBetween(15, 92),
      evPlot.enabledBetween(15, 92),
      FlightMode(reader, logTimeOffset, fmCF,
        stickMiddle + stickSize + 2 * stickGap, height - stickSize - rpGap + stickSize / 2, HA.Left, VA.Bottom).enabledBetween(15, 92),
      VerticalSpeed(reader, logTimeOffset, fmCF,
        stickMiddle + stickSize + 2 * stickGap, height - stickSize - rpGap + stickSize / 2, HA.Left, VA.Top).enabledBetween(15, 92),

      TopBlanket(0.35, new Color(240, 240, 240, 200), 0.5, 1.0, 9.5, 9.9),
      TextMessage("Evening, March 31, 2025",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center)
        .enabledBetween(1, 9.5),
      TextMessage("Same PID tuning is used, now testing the Return-To-Launch mode.",
        msgCF, width * 0.5, height * 0.05 + 1.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(2, 9.5),
      TextMessage("The waypoint speed is set to 2.5 m/s, min altitude to 8 m.",
        msgCF, width * 0.5, height * 0.05 + 2.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(2, 9.5),
      TextMessage("I will lift off, get to some distance and hit RTL.",
        msgCF, width * 0.5, height * 0.05 + 3.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(6, 9.5),

      TopBlanket(0.1, new Color(240, 240, 240, 200), 48.5, 49.0, 53.5, 53.9),
      TextMessage("About to switch to RTL now...",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center)
        .enabledBetween(49, 53.5),

      TopBlanket(0.1, new Color(240, 240, 240, 200), 73.5, 74.0, 77.5, 77.9),
      TextMessage("Reached position, waits 2 seconds, then starts landing.",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center)
        .enabledBetween(74, 77.5),

      TopBlanket(0.16, new Color(240, 240, 240, 200), 93.5, 94.0, 99.0, 99.3),
      TextMessage("Minor instabilities on landing (could be due to MOT_OPTIONS),",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center)
        .enabledBetween(94, 99.0),
      TextMessage("but otherwise it works!",
        msgCF, width * 0.5, height * 0.05 + msgStep, HA.Center, VA.Center)
        .enabledBetween(94, 99.0),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 99.3, timeOff = 99.8),
    )

    props.runVideo(allGraphics)
  end apply
