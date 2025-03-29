package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_11_14_CX7_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 3)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

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

    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 20,
      rpGap, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 20,
      rpGap * 2 + rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 1, 2,
      rpGap * 3 + 2 * rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 26f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, Color.YELLOW, Color.BLACK)
    val msgLeft = (1280.0 / 1920) * width + 0.3 * msgFontSize
    val msgTop = 2 * msgFontSize

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot, yawPlot,
      TextMessage("I decided to blow off some dust",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(1, 23),
      TextMessage("from CX7 and give it a go.",
        msgCF, msgLeft, msgTop + msgStep * 1, HA.Left, VA.Center)
        .enabledBetween(1, 23),
      TextMessage("Unfortunately, CX7 was not",
        msgCF, msgLeft, msgTop + msgStep * 2.5, HA.Left, VA.Center)
        .enabledBetween(8, 23),
      TextMessage("in a good shape.",
        msgCF, msgLeft, msgTop + msgStep * 3.5, HA.Left, VA.Center)
        .enabledBetween(8, 23),
      TextMessage("A lot of flap saturation...",
        msgCF, msgLeft, msgTop + msgStep * 5, HA.Left, VA.Center)
        .enabledBetween(13, 23),
      TextMessage("Especially now. Really bad...",
        msgCF, msgLeft, msgTop + msgStep * 6, HA.Left, VA.Center)
        .enabledBetween(18, 23),

      TextMessage("But it would have been too dull",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(25, 30),
      TextMessage("to stop testing now, right?...",
        msgCF, msgLeft, msgTop + msgStep * 1, HA.Left, VA.Center)
        .enabledBetween(25, 30),

      TextMessage("Apart from the design flaws,",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(32, 46),
      TextMessage("it seems as if PLA warped",
        msgCF, msgLeft, msgTop + msgStep * 1, HA.Left, VA.Center)
        .enabledBetween(32, 46),
      TextMessage("a lot because of exposure",
        msgCF, msgLeft, msgTop + msgStep * 2, HA.Left, VA.Center)
        .enabledBetween(32, 46),
      TextMessage("to house heating.",
        msgCF, msgLeft, msgTop + msgStep * 3, HA.Left, VA.Center)
        .enabledBetween(32, 46),
      TextMessage("Especially the flaps.",
        msgCF, msgLeft, msgTop + msgStep * 4.5, HA.Left, VA.Center)
        .enabledBetween(40, 46),
      TextMessage("They became just terrible.",
        msgCF, msgLeft, msgTop + msgStep * 5.5, HA.Left, VA.Center)
        .enabledBetween(40, 46),

      TextMessage("I could not even dodge",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(62, 69),
      TextMessage("a branch :/",
        msgCF, msgLeft, msgTop + msgStep * 1, HA.Left, VA.Center)
        .enabledBetween(62, 69),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 69.5, timeOff = 70.0),
    )

    props.runVideo(allGraphics)
  end apply
