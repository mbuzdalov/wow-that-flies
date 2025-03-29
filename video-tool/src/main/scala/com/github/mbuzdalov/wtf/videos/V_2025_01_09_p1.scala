package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_01_09_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val stickW = width / 2
    val stickSize = 201 * stickW / 1920
    val stickGap = 21 * stickW / 1920
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickW - stickSize - stickGap, stickW + stickGap, stickGap)

    val rpGap = 11 * width / 1280
    val rpWidth = (width - 5 * rpGap) / 4
    val rpHeight = (height * (1 - 1280.0 / 1920) - 2 * rpGap).toInt
    val fontSize = 16f * width / 1280
    val rpBackground = Color.BLACK

    val bottomPlotH = height - rpHeight - rpGap

    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", -1, 50,
      rpGap, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2, 4)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", -1, 50,
      rpGap * 2 + rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2, 4)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 3, 4,
      rpGap * 3 + 2 * rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val flapPlot = Plot.createXFlapPlot(reader, logTimeOffset,
      rpGap * 4 + 3 * rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val osdMsgFontSize = 26f * width / 1280
    val osdMsgStep = osdMsgFontSize * 1.5f
    val osdMsgCF = TextMessage.ColorFont(osdMsgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      sticks.whenTopLeftBlack,
      rollPlot.whenTopLeftBlack,
      pitchPlot.whenTopLeftBlack,
      yawPlot.whenTopLeftBlack,
      flapPlot.whenTopLeftBlack,

      LeftBlanket(0.35f, new Color(240, 240, 240, 200), 1.5, 1.9, 9.1, 9.5),
      TextMessage("January 09th, 2025.",
        msgCF, width * 0.02, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(2, 9),
      TextMessage("Testing battery covers",
        msgCF, width * 0.02, height * 0.06 + msgStep * 1.5, HA.Left, VA.Center)
        .enabledBetween(3, 9),
      TextMessage("that replace parasitic lift",
        msgCF, width * 0.02, height * 0.06 + msgStep * 2.5, HA.Left, VA.Center)
        .enabledBetween(3, 9),
      TextMessage("from batteries with",
        msgCF, width * 0.02, height * 0.06 + msgStep * 3.5, HA.Left, VA.Center)
        .enabledBetween(3, 9),
      TextMessage("uniform lift.",
        msgCF, width * 0.02, height * 0.06 + msgStep * 4.5, HA.Left, VA.Center)
        .enabledBetween(3, 9),

      TextMessage("Batteries were not fully charged,",
        osdMsgCF, width * 0.5, height * 0.5, HA.Center, VA.Center)
        .enabledBetween(19, 24),
      TextMessage("and it was quite cold.",
        osdMsgCF, width * 0.5, height * 0.5 + osdMsgStep, HA.Center, VA.Center)
        .enabledBetween(19, 24),

      TopBlanket(0.215f, new Color(240, 240, 240, 200), 25.5, 25.9, 34.1, 34.5),
      TextMessage("Feels very good so far!",
        msgCF, width * 0.5, height * 0.04, HA.Center, VA.Center)
        .enabledBetween(26, 34),
      TextMessage("Almost no disturbance when flying mostly forward,",
        msgCF, width * 0.5, height * 0.04 + msgStep * 1, HA.Center, VA.Center)
        .enabledBetween(26, 34),
      TextMessage("only starts to wobble with more roll input.",
        msgCF, width * 0.5, height * 0.04 + msgStep * 2, HA.Center, VA.Center)
        .enabledBetween(26, 34),

      TopBlanket(0.17f, new Color(240, 240, 240, 200), 64.5, 64.9, 70.1, 70.5),
      TextMessage("Flying this way was not possible at all",
        msgCF, width * 0.5, height * 0.04, HA.Center, VA.Center)
        .enabledBetween(65, 70),
      TextMessage("before installation of battery covers!",
        msgCF, width * 0.5, height * 0.04 + msgStep * 1, HA.Center, VA.Center)
        .enabledBetween(65, 70),

      LeftBlanket(0.38f, new Color(240, 240, 240, 200), 116.5, 116.9, 126.6, 127),
      TextMessage("Okay, the recipe is:",
        msgCF, width * 0.02, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(117, 126.5),
      TextMessage("if you want to fly fast,",
        msgCF, width * 0.02, height * 0.06 + msgStep * 1.5, HA.Left, VA.Center)
        .enabledBetween(118, 126.5),
      TextMessage("fly mostly forward, roll gently.",
        msgCF, width * 0.02, height * 0.06 + msgStep * 2.5, HA.Left, VA.Center)
        .enabledBetween(118, 126.5),
      TextMessage("Pitch inputs up to 40 degrees",
        msgCF, width * 0.02, height * 0.06 + msgStep * 3.5, HA.Left, VA.Center)
        .enabledBetween(118, 126.5),
      TextMessage("definitely work.",
        msgCF, width * 0.02, height * 0.06 + msgStep * 4.5, HA.Left, VA.Center)
        .enabledBetween(118, 126.5),
      TextMessage("If you want to roll a lot,",
        msgCF, width * 0.02, height * 0.06 + msgStep * 6, HA.Left, VA.Center)
        .enabledBetween(121, 126.5),
      TextMessage("decrease pitch first.",
        msgCF, width * 0.02, height * 0.06 + msgStep * 7, HA.Left, VA.Center)
        .enabledBetween(121, 126.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 145.5, timeOff = 146.0),
    )

    props.runVideo(allGraphics)
  end apply
