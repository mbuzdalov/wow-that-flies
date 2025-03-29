package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p1:
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

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val blinkInnerR = width * 0.045
    val blinkOuterR = width * 0.05

    val allGraphics = GraphicsConsumer.compose(
      sticks,

      BottomBlanket(0.35f, new Color(240, 240, 240, 200), 1.0, 1.4, 9.5, 9.9),
      TextMessage("Morning, March 24, 2025.",
        msgCF, width * 0.5, height * 0.7, HA.Center, VA.Center).enabledBetween(1.5, 9.5),
      TextMessage("I started the SN1 autonomous landing subproject.",
        msgCF, width * 0.5, height * 0.7 + msgStep, HA.Center, VA.Center).enabledBetween(2.5, 9.5),
      TextMessage("In this test, I want to find suitable vertical landing speeds,",
        msgCF, width * 0.5, height * 0.7 + 2.5 * msgStep, HA.Center, VA.Center).enabledBetween(3.5, 9.5),
      TextMessage("so that I can configure basic autonomous landing parameters.",
        msgCF, width * 0.5, height * 0.7 + 3.5 * msgStep, HA.Center, VA.Center).enabledBetween(3.5, 9.5),

      BottomBlanket(0.17f, new Color(240, 240, 240, 200), 13.0, 13.4, 18.5, 18.9),
      TextMessage("I am going to ascend, and then descend,",
        msgCF, width * 0.5, height * 0.88, HA.Center, VA.Center).enabledBetween(13.5, 18.5),
      TextMessage("trying to find the minimum throttle and maximum landing speed.",
        msgCF, width * 0.5, height * 0.88 + msgStep, HA.Center, VA.Center).enabledBetween(14.5, 18.5),

      BottomBlanket(0.1f, new Color(240, 240, 240, 200), 25.0, 25.4, 29.5, 29.9),
      TextMessage("Second test, trying to get higher",
        msgCF, width * 0.5, height * 0.95, HA.Center, VA.Center).enabledBetween(25.5, 29.5),

      BottomBlanket(0.1f, new Color(240, 240, 240, 200), 45.0, 45.4, 49.5, 49.9),
      TextMessage("Third test, look at the left part of the screen...",
        msgCF, width * 0.5, height * 0.95, HA.Center, VA.Center).enabledBetween(45.5, 49.5),

      BlinkingCircle(46, 10, 49, t => 0.05 + (t - 46) * 0.01, t => 0.22 - (t - 46) * 0.052, blinkInnerR, blinkOuterR, new Color(140, 50, 140)),

      BottomBlanket(0.1f, new Color(240, 240, 240, 200), 54.0, 54.4, 59.5, 59.9),
      TextMessage("Fourth test, trying to climb higher (~40 meters)...",
        msgCF, width * 0.5, height * 0.95, HA.Center, VA.Center).enabledBetween(54.5, 59.5),

      BottomBlanket(0.1f, new Color(240, 240, 240, 200), 73.0, 73.4, 77.5, 77.9),
      TextMessage("Now I am just flying around the field, after all, why not?",
        msgCF, width * 0.5, height * 0.95, HA.Center, VA.Center).enabledBetween(73.5, 77.5),

      BottomBlanket(0.17f, new Color(240, 240, 240, 200), 125.0, 125.4, 131.5, 131.9),
      TextMessage("Fifth and the last test,",
        msgCF, width * 0.5, height * 0.88, HA.Center, VA.Center).enabledBetween(125.5, 131.5),
      TextMessage("with roughly half battery left, just in case it matters.",
        msgCF, width * 0.5, height * 0.88 + msgStep, HA.Center, VA.Center).enabledBetween(125.5, 131.5),

      BottomBlanket(0.17f, new Color(240, 240, 240, 200), 163.0, 163.4, 167.5, 167.9),
      TextMessage("Well, the landing was like a charm,",
        msgCF, width * 0.5, height * 0.88, HA.Center, VA.Center).enabledBetween(163.5, 167.5),
      TextMessage("and the overall morning test was quite a success.",
        msgCF, width * 0.5, height * 0.88 + msgStep, HA.Center, VA.Center).enabledBetween(163.5, 167.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 168.5, timeOff = 169.0),
    )

    props.runVideo(allGraphics)
  end apply
