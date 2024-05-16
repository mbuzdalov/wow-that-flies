package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.*
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}

object V_2024_03_29_p3:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes.head - props.armTime

    val hWidth = width / 2
    val stickSize = 71 * width / 1280
    val stickGap = 7 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      width - 2 * stickSize - 2 * stickGap, width - stickSize - stickGap, height - stickSize - stickGap)

    val rpWidth = width / 4
    val rpHeight = height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 20,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(10, 10, 50)
    val msgCF = TextMessage.ColorFont(msgFontSize, msgColor)

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("March 29, 2024, test 3 (actually 5)",
        msgCF, width * 0.5f, height * 0.11f,
        HA.Center, VA.Center, 1, 10),
      TextMessage("Maybe the reduction was not enough?",
        msgCF, width * 0.5f, height * 0.11f + msgStep * 1.5f,
        HA.Center, VA.Center, 3, 10),
      TextMessage("Now ATC_ACCEL_{R,P}_MAX changed from 40000 further down to 30000.",
        msgCF, width * 0.5f, height * 0.11f + msgStep * 2.5f,
        HA.Center, VA.Center, 3, 10),
      TextMessage("Also, plots of D-terms showed some high-freq vibrations.",
        msgCF, width * 0.5f, height * 0.11f + msgStep * 4.0f,
        HA.Center, VA.Center, 5, 10),
      TextMessage("To counteract that, D-terms changed to be more aggressive:",
        msgCF, width * 0.5f, height * 0.11f + msgStep * 5.0f,
        HA.Center, VA.Center, 5, 10),
      TextMessage("ATC_RAT_{PIT,RLL}_FLTD from 40 to 20.",
        msgCF, width * 0.5f, height * 0.11f + msgStep * 6.0f,
        HA.Center, VA.Center, 5, 10),
      TextMessage("Sorry, but the shooting angle is really bad...",
        msgCF, width * 0.5f, height * 0.5f,
        HA.Center, VA.Center, 19, 24),
      TextMessage("Well, overshoots are still there.",
        msgCF, width * 0.5f, height * 0.25f,
        HA.Center, VA.Center, 81, 87),
      TextMessage("But they appear to be less severe now, and don't turn to oscillations.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 1.0f,
        HA.Center, VA.Center, 83, 87),
      TextMessage("Probably CX7 is now able to survive an autotune session.",
        msgCF, width * 0.5f, height * 0.25f + msgStep * 2.0f,
        HA.Center, VA.Center, 83, 87),
      new Fade(timeOn = 0.5, timeOff = 0),
    )

    props.run(allGraphics)
  end apply
  