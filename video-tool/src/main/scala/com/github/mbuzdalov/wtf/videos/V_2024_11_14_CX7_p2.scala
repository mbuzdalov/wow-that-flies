package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.util.PiecewiseLinearFunction
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_11_14_CX7_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 3)
    val logTimeOffset = autoArmedTimes(2) - props.armTime

    val stickW = ((1280.0 / 1920) * width / 2).toInt
    val stickSize = 201 * stickW / 1280
    val stickGap = 21 * stickW / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickW - stickSize - stickGap, stickW + stickGap, stickGap)

    val rpGap = 11 * width / 1280
    val rpWidth = (width - 4 * rpGap) / 3
    val rpHeight = (height * (1 - 1280.0 / 1920) - 2 * rpGap).toInt
    val fontSize = 16f * width / 1280
    val rpBackground = Color.BLACK

    val bottomPlotH = height - rpHeight - rpGap

    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 30,
      rpGap, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 30,
      rpGap * 2 + rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 1, 2,
      rpGap * 3 + 2 * rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 26f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, Color.YELLOW, Color.BLACK)
    val msgLeft = (1280.0 / 1920) * width + 0.3 * msgFontSize
    val msgTop = 2 * msgFontSize

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot, yawPlot,
      TextMessage("I usually perform testing",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(1, 12),
      TextMessage("following the principle:",
        msgCF, msgLeft, msgTop + msgStep * 1, HA.Left, VA.Center)
        .enabledBetween(1, 12),
      TextMessage("“more data — more better!”",
        msgCF, msgLeft, msgTop + msgStep * 2, HA.Left, VA.Center)
        .enabledBetween(1, 12),
      TextMessage("This time, it was ",
        msgCF, msgLeft, msgTop + msgStep * 3.5, HA.Left, VA.Center)
        .enabledBetween(6, 12),
      TextMessage("no exception...",
        msgCF, msgLeft, msgTop + msgStep * 4.5, HA.Left, VA.Center)
        .enabledBetween(6, 12),

      TextMessage("Clearly, pitch saturation,",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(27, 40),
      TextMessage("unsurprisingly, did not improve.",
        msgCF, msgLeft, msgTop + msgStep * 1, HA.Left, VA.Center)
        .enabledBetween(27, 40),
      TextMessage("The idea of the testing was",
        msgCF, msgLeft, msgTop + msgStep * 2.5, HA.Left, VA.Center)
        .enabledBetween(27, 40),
      TextMessage("to collect more data for",
        msgCF, msgLeft, msgTop + msgStep * 3.5, HA.Left, VA.Center)
        .enabledBetween(27, 40),
      TextMessage("different pitch angles",
        msgCF, msgLeft, msgTop + msgStep * 4.5, HA.Left, VA.Center)
        .enabledBetween(27, 40),
      TextMessage("with different throttle.",
        msgCF, msgLeft, msgTop + msgStep * 5.5, HA.Left, VA.Center)
        .enabledBetween(27, 40),

      TextMessage("In coax copters, more throttle",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(48, 60),
      TextMessage("means more control, so I chose",
        msgCF, msgLeft, msgTop + msgStep * 1, HA.Left, VA.Center)
        .enabledBetween(48, 60),
      TextMessage("to test what CX7 can do at",
        msgCF, msgLeft, msgTop + msgStep * 2, HA.Left, VA.Center)
        .enabledBetween(48, 60),
      TextMessage("high throttle.",
        msgCF, msgLeft, msgTop + msgStep * 3, HA.Left, VA.Center)
        .enabledBetween(48, 60),
      TextMessage("Ready...",
        msgCF, msgLeft, msgTop + msgStep * 4.5, HA.Left, VA.Center)
        .withAlpha(PiecewiseLinearFunction(52, 0) to (52.01, 1) to (59.5, 1) to (60, 0)),
      TextMessage("Steady...",
        msgCF, msgLeft, msgTop + msgStep * 5.5, HA.Left, VA.Center)
        .withAlpha(PiecewiseLinearFunction(53, 0) to (53.01, 1) to (59.5, 1) to (60, 0)),
      TextMessage("Go!",
        msgCF, msgLeft, msgTop + msgStep * 6.5, HA.Left, VA.Center)
        .withAlpha(PiecewiseLinearFunction(54, 0) to (54.01, 1) to (59.5, 1) to (60, 0)),

      TextMessage("Hi there, Mother Earth...",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(61, 74),
      TextMessage("Well, an obvious lesson learned:",
        msgCF, msgLeft, msgTop + msgStep * 1.5, HA.Left, VA.Center)
        .enabledBetween(68, 74),
      TextMessage("don't attempt extreme tests",
        msgCF, msgLeft, msgTop + msgStep * 2.5, HA.Left, VA.Center)
        .enabledBetween(68, 74),
      TextMessage("if the copter flies badly.",
        msgCF, msgLeft, msgTop + msgStep * 3.5, HA.Left, VA.Center)
        .enabledBetween(68, 74),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 74.5, timeOff = 75.0),
    )

    props.runVideo(allGraphics)
  end apply
