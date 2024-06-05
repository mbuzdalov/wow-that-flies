package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_05_10_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 2)
    val logTimeOffset = autoArmedTimes(1) - props.armTime

    val hWidth = width / 2
    val stickSize = 61 * width / 1280
    val stickGap = 5 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      hWidth - stickSize - stickGap / 2, hWidth + stickGap / 2, stickGap)

    val rpWidth = width / 7
    val rpHeight = height / 4
    val rpGap = 5 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 15,
      rpGap, height - 3 * (rpHeight + rpGap), rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 15,
      rpGap, height - 2 * (rpHeight + rpGap), rpWidth, rpHeight, fontSize, rpBackground, 2)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 1, 2,
      rpGap, height - (rpHeight + rpGap), rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(240, 240, 0), new Color(0, 0, 0, 120))

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot, yawPlot,
      TextMessage("Now, it's time to fly FPV,",
        msgCF, width * 0.5f, height * 0.15f,
        HA.Center, VA.Center, 1, 5),
      TextMessage("for the first time.",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 1f,
        HA.Center, VA.Center, 1, 5),

      TextMessage("I feel confident enough",
        msgCF, width * 0.5f, height * 0.15f,
        HA.Center, VA.Center, 55.5, 60.5),
      TextMessage("to explore the trees...",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 1f,
        HA.Center, VA.Center, 55.5, 60.5),

      TextMessage("Well, this was good, for the first attempt.",
        msgCF, width * 0.5f, height * 0.15f,
        HA.Center, VA.Center, 220, 229),
      TextMessage("More adjustments are needed though",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 1f,
        HA.Center, VA.Center, 224, 229),
      TextMessage("to fly Acro. Stay tuned!",
        msgCF, width * 0.5f, height * 0.15f + msgStep * 2f,
        HA.Center, VA.Center, 224, 229),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 229.5, timeOff = 230),
    )

    props.runVideo(allGraphics)
  end apply
  