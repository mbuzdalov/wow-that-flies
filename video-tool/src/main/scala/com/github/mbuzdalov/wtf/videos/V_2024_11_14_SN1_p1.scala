package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.util.PiecewiseLinearFunction
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_11_14_SN1_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val stickW = ((1280.0 / 1920) * width / 2).toInt
    val stickSize = 201 * stickW / 1280
    val stickGap = 21 * stickW / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickW - stickSize - stickGap, stickW + stickGap, stickGap)

    val rpGap = 11 * width / 1280
    val rpWidth = (width - 5 * rpGap) / 4
    val rpHeight = (height * (1 - 1280.0 / 1920) - 2 * rpGap).toInt
    val fontSize = 16f * width / 1280
    val rpBackground = Color.BLACK

    val bottomPlotH = height - rpHeight - rpGap

    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", -1, 180,
      rpGap, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2, 5)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", -1, 90,
      rpGap * 2 + rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2, 2)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 3, 4,
      rpGap * 3 + 2 * rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val flapPlot = Plot.createXFlapPlot(reader, logTimeOffset,
      rpGap * 4 + 3 * rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 26f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, Color.YELLOW)
    val msgDCF = TextMessage.ColorFont(msgFontSize, Color.RED)
    val msgLeft = (1280.0 / 1920) * width + 0.3 * msgFontSize
    val msgTop = 2 * msgFontSize

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot, yawPlot, flapPlot,
      TextMessage("I knew that SN1 was pretty stable",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(1, 12),
      TextMessage("when landing vertically,",
        msgCF, msgLeft, msgTop + msgStep * 1, HA.Left, VA.Center)
        .enabledBetween(1, 12),
      TextMessage("so decided to test the following:",
        msgCF, msgLeft, msgTop + msgStep * 2, HA.Left, VA.Center)
        .enabledBetween(1, 12),
      TextMessage("- the Acro mode,",
        msgCF, msgLeft, msgTop + msgStep * 3.5, HA.Left, VA.Center)
        .enabledBetween(3, 12),
      TextMessage("- higher throttle,",
        msgCF, msgLeft, msgTop + msgStep * 4.5, HA.Left, VA.Center)
        .enabledBetween(4, 12),
      TextMessage("- the belly flop.",
        msgCF, msgLeft, msgTop + msgStep * 5.5, HA.Left, VA.Center)
        .enabledBetween(5, 12),
      TextMessage("Was I right?...",
        msgCF, msgLeft, msgTop + msgStep * 7, HA.Left, VA.Center)
        .enabledBetween(9, 12),

      TextMessage("Belly flop starts in",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(16, 24),
      TextMessage("3",
        msgCF, msgLeft, msgTop + msgStep, HA.Left, VA.Center)
        .withAlpha(PiecewiseLinearFunction(18.59, 0) to (18.6, 1) to (23.5, 1) to (24, 0)),
      TextMessage("2",
        msgCF, msgLeft + 1 * msgFontSize, msgTop + msgStep, HA.Left, VA.Center)
        .withAlpha(PiecewiseLinearFunction(19.59, 0) to (19.6, 1) to (23.5, 1) to (24, 0)),
      TextMessage("1",
        msgCF, msgLeft + 2 * msgFontSize, msgTop + msgStep, HA.Left, VA.Center)
        .withAlpha(PiecewiseLinearFunction(20.59, 0) to (20.6, 1) to (23.5, 1) to (24, 0)),
      TextMessage("now!",
        msgCF, msgLeft + 3 * msgFontSize, msgTop + msgStep, HA.Left, VA.Center)
        .withAlpha(PiecewiseLinearFunction(21.59, 0) to (21.6, 1) to (23.5, 1) to (24, 0)),

      TextMessage("Max vertical speed: 11 m/s",
        msgDCF, msgLeft, bottomPlotH - 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(24, 48),
      TextMessage("Max altitude: 64 m",
        msgDCF, msgLeft, bottomPlotH - msgStep, HA.Left, VA.Center)
        .enabledBetween(24, 48),

      TextMessage("This is not a proper behavior",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(25, 35),
      TextMessage("in Acro. As a pilot, I didn't",
        msgCF, msgLeft, msgTop + msgStep, HA.Left, VA.Center)
        .enabledBetween(25, 35),
      TextMessage("know where the setpoint was,",
        msgCF, msgLeft, msgTop + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(25, 35),
      TextMessage("because CX7 could not",
        msgCF, msgLeft, msgTop + 3 * msgStep, HA.Left, VA.Center)
        .enabledBetween(25, 35),
      TextMessage("track it nicely.",
        msgCF, msgLeft, msgTop + 4 * msgStep, HA.Left, VA.Center)
        .enabledBetween(25, 35),
      TextMessage("The correct action was to",
        msgCF, msgLeft, msgTop + 5.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(30, 35),
      TextMessage("switch to Stabilize, but alas.",
        msgCF, msgLeft, msgTop + 6.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(30, 35),
      TextMessage("Hello, library building!",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(36, 48),
      TextMessage("SN1 was in fact intact,",
        msgCF, msgLeft, msgTop + 1.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(41, 48),
      TextMessage("and was brought to me",
        msgCF, msgLeft, msgTop + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(41, 48),
      TextMessage("by a maintenance worker.",
        msgCF, msgLeft, msgTop + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(41, 48),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 48.5, timeOff = 49.0),
    )

    props.runVideo(allGraphics)
  end apply
