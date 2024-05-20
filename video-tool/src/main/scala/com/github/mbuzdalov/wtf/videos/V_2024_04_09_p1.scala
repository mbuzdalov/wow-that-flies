package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_09_p1:
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
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 20,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("April 09, 2024, test #1.",
        msgCF, width * 0.5f, height * 0.25f,
        HA.Center, VA.Center, 1, 6),
      TextMessage("Yet again trying to decrease the rate I-terms,",
        msgCF, width * 0.5f, height * 0.25f + 1.0f * msgStep,
        HA.Center, VA.Center, 1, 6),
      TextMessage("as they may wind up in the beginning of the twitch and hit at the end.",
        msgCF, width * 0.5f, height * 0.25f + 2.0f * msgStep,
        HA.Center, VA.Center, 1, 6),
      TextMessage("ATC_RAT_{PIT,RLL}_I: 0.2 ➞ 0.02",
        msgCF, width * 0.5f, height * 0.25f + 3.0f * msgStep,
        HA.Center, VA.Center, 1, 6),

      TextMessage("Well, looking at both pitch and roll, we see that decreasing the I-terms",
        msgCF, width * 0.5f, height * 0.8f,
        HA.Center, VA.Center, 12, 25),
      TextMessage("is rather bad, as actual values are constantly higher than desired ones.",
        msgCF, width * 0.5f, height * 0.8f + 1.0f * msgStep,
        HA.Center, VA.Center, 12, 25),
      TextMessage("Especially right now, and look at the pitch...",
        msgCF, width * 0.5f, height * 0.8f + 2.0f * msgStep,
        HA.Center, VA.Center, 14, 25),

      TextMessage("The control was sloppy, to say the least.",
        msgCF, width * 0.5f, height * 0.3f,
        HA.Center, VA.Center, 60, 67),
      TextMessage("For the next test, the I-terms were restored back to 0.2.",
        msgCF, width * 0.5f, height * 0.3f + 1.0f * msgStep,
        HA.Center, VA.Center, 60, 67),
      TextMessage("I had another thing to test for today: one more notch filter.",
        msgCF, width * 0.5f, height * 0.3f + 2.0f * msgStep,
        HA.Center, VA.Center, 63, 67),
      Fade(timeOn = 67.1, timeOff = 67.6)
    )

    props.runVideo(allGraphics)
  end apply
  