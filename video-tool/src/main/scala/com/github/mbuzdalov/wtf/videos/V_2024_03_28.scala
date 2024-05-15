package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}
import com.github.mbuzdalov.wtf.widgets.{RollPitchPlots, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment => HA, VerticalAlignment => VA}

object V_2024_03_28:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes.head - props.armTime

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
    val rollPlot = RollPitchPlots.create(reader, logTimeOffset, "Roll", 3, 15,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = RollPitchPlots.create(reader, logTimeOffset, "Pitch", 4, 15,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(10, 10, 50)
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("March 28, 2024",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f,
        HA.Center, VA.Center, 1, 10),
      TextMessage("To aid investigations, detailed logging was enabled:",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 1f,
        HA.Center, VA.Center, 3, 10),
      TextMessage("INS_RAW_LOG_OPT = 9",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 2.2f,
        HA.Center, VA.Center, 3, 10),
      TextMessage("LOG_BITMASK = 180223",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 3f,
        HA.Center, VA.Center, 3, 10),
      TextMessage("This is a test run.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 4.5f,
        HA.Center, VA.Center, 3, 10),

      TextMessage("Well, CX7 did not crash this time,",
        msgFontSize, msgColor, width * 0.5f, height * 0.33f,
        HA.Center, VA.Center, 101, 110),
      TextMessage("but the discrepancy between desired and actual roll and pitch,",
        msgFontSize, msgColor, width * 0.5f, height * 0.33f + msgStep * 1f,
        HA.Center, VA.Center, 101, 110),
      TextMessage("is quite large even in a gentle flight.",
        msgFontSize, msgColor, width * 0.5f, height * 0.33f + msgStep * 2f,
        HA.Center, VA.Center, 101, 110),

      TextMessage("Stay tuned! (Pun intended)",
        msgFontSize, msgColor, width * 0.5f, height * 0.33f + msgStep * 3.5f,
        HA.Center, VA.Center, 107, 110),
    )

    props.run(allGraphics)
  end apply