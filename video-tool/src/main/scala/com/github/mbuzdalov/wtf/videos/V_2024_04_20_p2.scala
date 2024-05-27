package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{BlinkingCircle, Fade, LastLogMessage, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_20_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 3)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val hWidth = width / 2
    val stickSize = 101 * width / 1280
    val stickGap = 11 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      hWidth - stickSize - stickGap, hWidth + stickGap, stickGap)

    val rpWidth = width / 6
    val rpHeight = height / 5
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 30,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 30,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(240, 240, 0), new Color(0, 0, 0, 120))

    val blinkColor = Color.ORANGE
    val blinkInnerR = height * 0.06
    val blinkOuterR = height * 0.065

    val logFontSize = 24f * width / 1280
    val logCF = TextMessage.ColorFont(logFontSize, new Color(240, 240, 0), new Color(0, 0, 0, 120))

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("April 20, 2024, test #2",
        msgCF, width * 0.5f, height * 0.2f,
        HA.Center, VA.Center, 1, 8),
      TextMessage("I applied the partial result of the previous session, which was:",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 1f,
        HA.Center, VA.Center, 2, 8),
      TextMessage("ATC_RAT_RLL_{P,I}: 0.22 ➞ 0.1, ATC_RAT_RLL_D: 0.005 ➞ 0.0033.",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 2f,
        HA.Center, VA.Center, 2, 8),
      TextMessage("It's strange, but let's test it.",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 3f,
        HA.Center, VA.Center, 2, 8),
      TextMessage("I also found that roll, with new flaps, was agile enough,",
        msgCF, width * 0.5f, height * 0.7f,
        HA.Center, VA.Center, 4, 8),
      TextMessage("so I can increase ATC_ACCEL_R_MAX from 30000 to 65000.",
        msgCF, width * 0.5f, height * 0.7f + msgStep * 1f,
        HA.Center, VA.Center, 4, 8),
      TextMessage("Let's try again...",
        msgCF, width * 0.5f, height * 0.7f + msgStep * 2f,
        HA.Center, VA.Center, 6, 8),
      TextMessage("I chose a different location with a hope for less wind,",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 24, 29),
      TextMessage("but the wind itself got stronger by that time.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center, 24, 29),
      TextMessage("The wind also changed direction,",
        msgCF, width * 0.5f, height * 0.5f,
        HA.Center, VA.Center, 92, 100),
      TextMessage("so my camera setup appeared to be even worse.",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 1f,
        HA.Center, VA.Center, 92, 100),
      TextMessage("Other countries have a climate. Britain has weather.",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 2.5f,
        HA.Center, VA.Center, 96, 100),
      TextMessage("Your ad could be here.",
        msgCF, width * 0.5f, height * 0.5f,
        HA.Center, VA.Center, 180, 189),
      TextMessage("Just kidding. I despise ads.",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 1f,
        HA.Center, VA.Center, 185, 189),
      TextMessage("The university gardeners are nice, right? :)",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 203, 207),
      TextMessage("It looks like I am struggling more than I am tuning, but...",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 220, 230),
      TextMessage("Well, there is no \"but\".",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center, 225, 230),
      TextMessage("Well, at least I did not break it!",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 270, 278),
      TextMessage("Bigger flaps do work better.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center, 273, 278),
      LastLogMessage(reader, logTimeOffset, logCF, width * 0.2f, height * 0.21f, 8, 276),
      BlinkingCircle(107.0, 0.2, 108.0, 0, height * 0.349, blinkInnerR, blinkOuterR, blinkColor),
      BlinkingCircle(143.6, 0.2, 144.6, 0, height * 0.395, blinkInnerR, blinkOuterR, blinkColor),
      BlinkingCircle(189.9, 0.2, 190.9, 0, height * 0.450, blinkInnerR, blinkOuterR, blinkColor),
      BlinkingCircle(237.8, 0.2, 238.8, 0, height * 0.434, blinkInnerR, blinkOuterR, blinkColor),
      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 278.5, timeOff = 279),
    )

    props.runVideo(allGraphics)
  end apply
  