package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_15_p4:
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
    val rpHeight = 3 * height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 20,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("April 15, 2024, test #2.",
        msgCF, width * 0.5f, height * 0.25f,
        HA.Center, VA.Center, 1, 8),
      TextMessage("Pitch rate PIDs are decreased to match the roll's:",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1f,
        HA.Center, VA.Center, 2, 8),
      TextMessage("ATC_RAT_PIT_{P,I}: 0.03 ➞ 0.022",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2f,
        HA.Center, VA.Center, 4, 8),
      TextMessage("ATC_RAT_PIT_D: 0.007 ➞ 0.005",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 3f,
        HA.Center, VA.Center, 4, 8),
      TextMessage("Much. Much. Better.",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 54, 61),
      TextMessage("Some pitch oscillations remain, but",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center, 54, 61),
      TextMessage("maybe it can survive outdoor conditions now?",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 2f,
        HA.Center, VA.Center, 56, 61),
      Fade(timeOn = 0.5, timeOff = 0.0),
      Fade(timeOn = 61.3, timeOff = 61.8),
    )

    props.runVideo(allGraphics)
  end apply
  