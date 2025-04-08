package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_31_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 2) // the first was into the ground
    val logTimeOffset = autoArmedTimes(1) - props.armTime

    val stickMiddle = (width * 0.5).toInt
    val stickSize = 201 * width / 1920 / 2
    val stickGap = 21 * width / 1920 / 2
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickMiddle - stickSize - stickGap, stickMiddle + stickGap, stickGap)

    val rpGap = 11 * width / 1280
    val rpWidth = width / 6
    val rpHeight = height / 7
    val fontSize = 16f * width / 1280
    val rpBackground = new Color(255, 255, 255, 120)

    val npPlot = Plot.createNEPlot(reader, logTimeOffset, 'N', 'P', -8, -3,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 4)
    val nvPlot = Plot.createNEPlot(reader, logTimeOffset, 'N', 'V', -1, +1,
      rpGap * 2 + rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 3)
    val epPlot = Plot.createNEPlot(reader, logTimeOffset, 'E', 'P', 2, 6,
      width - 2 * rpGap - 2 * rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 3)
    val evPlot = Plot.createNEPlot(reader, logTimeOffset, 'E', 'V', -1, +1,
      width - rpGap - rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 3)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      sticks.enabledBetween(8, 52),
      npPlot.enabledBetween(8, 52),
      nvPlot.enabledBetween(8, 52),
      epPlot.enabledBetween(8, 52),
      evPlot.enabledBetween(8, 52),

      BottomBlanket(0.3, new Color(240, 240, 240, 200), 0.5, 1.0, 9.5, 9.9),
      TextMessage("March 31, 2025.",
        msgCF, width * 0.5, height * 0.95 - 3 * msgStep, HA.Center, VA.Center)
        .enabledBetween(1, 9.5),
      TextMessage("Today, I am tuning PSC_VELXY params (velocity PIDs).",
        msgCF, width * 0.5, height * 0.95 - 2 * msgStep, HA.Center, VA.Center)
        .enabledBetween(1.5, 9.5),
      TextMessage("Based on previous flights, I am going to test half defaults first:",
        msgCF, width * 0.5, height * 0.95 - msgStep, HA.Center, VA.Center)
        .enabledBetween(1.5, 9.5),
      TextMessage("PSC_VELXY_P = 1",
        msgCF, width * 0.15, height * 0.95, HA.Center, VA.Center)
        .enabledBetween(3.5, 9.5),
      TextMessage("PSC_VELXY_I = 0.5",
        msgCF, width * 0.50, height * 0.95, HA.Center, VA.Center)
        .enabledBetween(4.5, 9.5),
      TextMessage("PSC_VELXY_D = 0.25",
        msgCF, width * 0.85, height * 0.95, HA.Center, VA.Center)
        .enabledBetween(5.5, 9.5),

      BottomBlanket(0.25, new Color(240, 240, 240, 200), 22.0, 22.5, 29.5, 29.9),
      TextMessage("We see oscillations in speed, but they are not centered on target.",
        msgCF, width * 0.5, height * 0.95 - 2 * msgStep, HA.Center, VA.Center)
        .enabledBetween(22.5, 29.5),
      TextMessage("'Not centered' means that P and I terms are too small to do their job.",
        msgCF, width * 0.5, height * 0.95 - 1 * msgStep, HA.Center, VA.Center)
        .enabledBetween(24.5, 29.5),
      TextMessage("If such, the presence of oscillations means that D is too large.",
        msgCF, width * 0.5, height * 0.95, HA.Center, VA.Center)
        .enabledBetween(26.5, 29.5),

      BottomBlanket(0.16, new Color(240, 240, 240, 200), 40.5, 40.9, 48.5, 48.9),
      TextMessage("Okay, in the next test I will increase PSC_VELXY_{P, I}",
        msgCF, width * 0.5, height * 0.95 - 1 * msgStep, HA.Center, VA.Center)
        .enabledBetween(41, 48.5),
      TextMessage("and decrease PSC_VELXY_D, both significantly.",
        msgCF, width * 0.5, height * 0.95, HA.Center, VA.Center)
        .enabledBetween(41.5, 48.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 52.5, timeOff = 53.0),
    )

    props.runVideo(allGraphics)
  end apply
