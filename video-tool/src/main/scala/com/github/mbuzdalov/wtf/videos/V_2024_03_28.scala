package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}
import com.github.mbuzdalov.wtf.widgets.{Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}

object V_2024_03_28:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val hWidth = width / 2
    val stickSize = 101 * width / 1280
    val stickGap = 11 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      hWidth - stickSize - stickGap, hWidth + stickGap, stickGap)

    val rpWidth = width / 4
    val rpHeight = height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 15,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 15,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(10, 10, 50)
    val msgCF = TextMessage.ColorFont(msgFontSize, msgColor)

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("March 28, 2024",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(1, 10),
      TextMessage("To aid investigations, detailed logging was enabled:",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("INS_RAW_LOG_OPT = 9",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2.2f, HA.Center, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("LOG_BITMASK = 180223",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 3f, HA.Center, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("This is a test run.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 4.5f, HA.Center, VA.Center)
        .enabledBetween(3, 10),

      TextMessage("Well, CX7 did not crash this time,",
        msgCF, width * 0.5f, height * 0.33f, HA.Center, VA.Center)
        .enabledBetween(101, 110),
      TextMessage("but the discrepancy between desired and actual roll and pitch,",
        msgCF, width * 0.5f, height * 0.33f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(101, 110),
      TextMessage("is quite large even in a gentle flight.",
        msgCF, width * 0.5f, height * 0.33f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(101, 110),

      TextMessage("Stay tuned! (Pun intended)",
        msgCF, width * 0.5f, height * 0.33f + msgStep * 3.5f, HA.Center, VA.Center)
        .enabledBetween(107, 110),
    )

    props.runVideo(allGraphics)
  end apply
  