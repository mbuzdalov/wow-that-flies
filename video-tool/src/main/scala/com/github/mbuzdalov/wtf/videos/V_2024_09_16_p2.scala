package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_09_16_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(240, 240, 0), new Color(0, 0, 0, 180))

    val allGraphics = GraphicsConsumer.compose(
      TextMessage("September 16th, 2024. First FPV test for SN1.",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(2, 5),
      TextMessage("A rough start, but then OK...",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(10, 15),
      TextMessage("Some jello in the FPV feed, but not too bad.",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(18, 22),
      TextMessage("This was output saturation, not my input.",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(35, 38),
      TextMessage("Flies nicely downwind :)",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(40, 43),
      TextMessage("Upwind is noticeably harder...",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(64, 69),
      TextMessage("What if we throttle up?",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(80, 84),
      TextMessage("Nice-ish, as expected...",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(86, 88),
      TextMessage("Output saturation and wind gusts",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(106, 111),
      TextMessage("make it harder to maintain attitude to land.",
        msgCF, width * 0.5, height * 0.1 + msgStep, HA.Center, VA.Center)
        .enabledBetween(106, 111),
      TextMessage("Will increase ATC_THR_MIX_MAN next time...",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(113, 116),
      TextMessage("It ain't much, but it's honest work.",
        msgCF, width * 0.5, height * 0.1, HA.Center, VA.Center)
        .enabledBetween(118, 122),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 122.5, timeOff = 123),
    )

    props.runVideo(allGraphics)
  end apply
