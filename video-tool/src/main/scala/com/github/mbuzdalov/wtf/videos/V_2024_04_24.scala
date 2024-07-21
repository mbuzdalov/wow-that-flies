package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_24:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 2)
    val logTimeOffset = autoArmedTimes(1) - props.armTime

    val hWidth = width / 2
    val stickSize = 61 * width / 1280
    val stickGap = 5 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickGap, 2 * stickGap + stickSize, stickGap)

    val rpWidth = width / 7
    val rpHeight = height / 5
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 30,
      rpGap, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 30,
      width - rpWidth - rpGap, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 75))

    val tailFontSize = 25f * width / 1280
    val tailStep = tailFontSize * 1.5f
    val tailCF = TextMessage.ColorFont(tailFontSize, new Color(240, 240, 0))
    val goodCF = TextMessage.ColorFont(tailFontSize, new Color(0, 210, 0))
    val badCF = TextMessage.ColorFont(tailFontSize, new Color(190, 0, 0))

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("April 24, 2024.",
        msgCF, width * 0.5f, height * 0.25f, HA.Center, VA.Center)
        .enabledBetween(1, 6),
      TextMessage("I attached a GPS+Compass module to the top",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(2, 6),
      TextMessage("to make the center of gravity higher",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(2, 6),
      TextMessage("and to test whether the compass can work there.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 3f, HA.Center, VA.Center)
        .enabledBetween(2, 6),
      TextMessage("Then I ran AutoTune again, and here is the result...",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 4f, HA.Center, VA.Center)
        .enabledBetween(4, 6),
      TextMessage("Well, CX7 is not completely locked-in yet,",
        msgCF, width * 0.5f, height * 0.4f, HA.Center, VA.Center)
        .enabledBetween(53, 57),
      TextMessage("but it is really quick now!",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(54, 57),
      TextMessage("(P.S.: the compass did not work)",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 3f, HA.Center, VA.Center)
        .enabledBetween(56, 57),

      Fade(timeOn = 57.5, timeOff = 58),

      TextMessage("The changes before this video (also with aggressiveness 0.1) were:",
        tailCF, width * 0.5f, height * 0.07f, HA.Center, VA.Center)
        .enabledBetween(58, 70),

      TextMessage("ATC_RAT_RLL_{P,I}: 0.129 ➞ 0.162",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("ATC_RAT_RLL_D: 0.001 ➞ 0.001",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("Still the minimum D-term value set!",
        badCF, width * 0.5f, height * 0.07f + tailStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(62, 70),
      TextMessage("ATC_ANG_RLL_P: 6.338 ➞ 6",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("Rounded, don't remember why",
        tailCF, width * 0.5f, height * 0.07f + tailStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(62, 70),
      TextMessage("ATC_ACCEL_R_MAX: 65803 ➞ 70866",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 4.5f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("Moderate roll improvement",
        tailCF, width * 0.5f, height * 0.07f + tailStep * 4.5f, HA.Left, VA.Center)
        .enabledBetween(62, 70),

      TextMessage("ATC_RAT_PIT_{P,I}: 0.114 ➞ 0.246",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 6f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("Increased twice, interesting",
        tailCF, width * 0.5f, height * 0.07f + tailStep * 6f, HA.Left, VA.Center)
        .enabledBetween(62, 70),
      TextMessage("ATC_RAT_PIT_D: 0.00515 ➞ 0.00519",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 7f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("ATC_ANG_PIT_P: 6.274 ➞ 8",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 8f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("Increased (then rounded)",
        goodCF, width * 0.5f, height * 0.07f + tailStep * 8f, HA.Left, VA.Center)
        .enabledBetween(62, 70),
      TextMessage("ATC_ACCEL_P_MAX: 32300 ➞ 52592",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 9f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("A good improvement for pitch",
        goodCF, width * 0.5f, height * 0.07f + tailStep * 9f, HA.Left, VA.Center)
        .enabledBetween(62, 70),

      TextMessage("ATC_RAT_YAW_P: 0.473 ➞ 0.76",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 10.5f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("Yaw tightened even more!",
        goodCF, width * 0.5f, height * 0.07f + tailStep * 10.5f, HA.Left, VA.Center)
        .enabledBetween(62, 70),
      TextMessage("ATC_RAT_YAW_I: 0.0473 ➞ 0.076",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 11.5f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("ATC_RAT_YAW_D: 0.119 ➞ 0.119",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 12.5f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("ATC_RAT_YAW_FLTE: 1.28 ➞ 1.96",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 13.5f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("ATC_ANG_YAW_P: 14.67 ➞ 15.8",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 14.5f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("ATC_ACCEL_Y_MAX: 96140 ➞ 135117",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 15.5f, HA.Left, VA.Center)
        .enabledBetween(58, 70),
      TextMessage("Wow, just wow!",
        goodCF, width * 0.5f, height * 0.07f + tailStep * 15.5f, HA.Left, VA.Center)
        .enabledBetween(62, 70),
    )

    props.runVideo(allGraphics)
  end apply
  