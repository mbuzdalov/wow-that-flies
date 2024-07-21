package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{BlinkingCircle, Fade, LastLogMessage, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_20_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val hWidth = width / 2
    val stickSize = 101 * width / 1280
    val stickGap = 11 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      hWidth - stickSize - stickGap, hWidth + stickGap, height - stickSize - stickGap)

    val rpWidth = width / 6
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
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(240, 240, 0), new Color(0, 0, 0, 120))

    val blinkColor = Color.ORANGE
    val blinkInnerR = height * 0.06
    val blinkOuterR = height * 0.065

    val logFontSize = 24f * width / 1280
    val logCF = TextMessage.ColorFont(logFontSize, new Color(240, 240, 0), new Color(0, 0, 0, 120))

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("April 20, 2024, test #1",
        msgCF, width * 0.5f, height * 0.1f, HA.Center, VA.Center)
        .enabledBetween(1, 8),
      TextMessage("This is the second attempt to perform AutoTune outdoors.",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 1.5f, HA.Center, VA.Center)
        .enabledBetween(3, 8),
      TextMessage("Before this test, I did few flights, including an endurance test",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 2.5f, HA.Center, VA.Center)
        .enabledBetween(3, 8),
      TextMessage("which showed that the hover time of CX7 was 11.5 minutes.",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 3.5f, HA.Center, VA.Center)
        .enabledBetween(3, 8),
      TextMessage("These tests showed that yaw is sloppy, so PIDs were increased:",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 5f, HA.Center, VA.Center)
        .enabledBetween(5, 8),
      TextMessage("ATC_RAT_YAW_P: 0.18 ➞ 0.3, ATC_RAT_YAW_I: 0.018 ➞ 0.03.",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 6f, HA.Center, VA.Center)
        .enabledBetween(5, 8),
      TextMessage("It may look like there is no wind,",
        msgCF, width * 0.5f, height * 0.6f, HA.Center, VA.Center)
        .enabledBetween(17, 22),
      TextMessage("but there is, and it changes a lot over time.",
        msgCF, width * 0.5f, height * 0.6f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(17, 22),
      TextMessage("AutoTune is started,",
        msgCF, width * 0.5f, height * 0.6f, HA.Center, VA.Center)
        .enabledBetween(29, 34),
      TextMessage("with occasional interruptions to get the vehicle back.",
        msgCF, width * 0.5f, height * 0.6f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(29, 34),
      TextMessage("You can see the last log message below.",
        msgCF, width * 0.5f, height * 0.6f, HA.Center, VA.Center)
        .enabledBetween(36, 40),
      TextMessage("When the copter is out of the view,",
        msgCF, width * 0.5f, height * 0.6f, HA.Center, VA.Center)
        .enabledBetween(134, 139),
      TextMessage("relax and enjoy the views...",
        msgCF, width * 0.5f, height * 0.6f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(134, 139),
      TextMessage("CX7 is playing peek-a-boo, there will be",
        msgCF, width * 0.5f, height * 0.6f, HA.Center, VA.Center)
        .enabledBetween(144, 148),
      TextMessage("a blinking orange circle to highlight where it comes back.",
        msgCF, width * 0.5f, height * 0.6f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(144, 148),
      TextMessage("You may hear overshoot events (and see them in the logs)",
        msgCF, width * 0.5f, height * 0.6f, HA.Center, VA.Center)
        .enabledBetween(150, 156),
      TextMessage("when CX7 cannot hold the attitude fighting against the wind.",
        msgCF, width * 0.5f, height * 0.6f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(150, 156),
      TextMessage("That's why AutoTune is recommended to be done in no wind...",
        msgCF, width * 0.5f, height * 0.6f, HA.Center, VA.Center)
        .enabledBetween(180, 186),

      TextMessage("You can see that the wind is indeed non-zero.",
        msgCF, width * 0.5f, height * 0.4f, HA.Center, VA.Center)
        .enabledBetween(200, 205),
      BlinkingCircle(200, 10, 205, 0, height * 0.3, height * 0.25, height * 0.255, blinkColor),

      TextMessage("Normally to tune pitch you need to face the wind,",
        msgCF, width * 0.5f, height * 0.55f, HA.Center, VA.Center)
        .enabledBetween(244, 250),
      TextMessage("but I'm now 90 degrees clockwise,",
        msgCF, width * 0.5f, height * 0.55f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(244, 250),
      TextMessage("because roll is tracked a little bit better.",
        msgCF, width * 0.5f, height * 0.55f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(244, 250),
      TextMessage("It landed in one piece! :)",
        msgCF, width * 0.5f, height * 0.4f, HA.Center, VA.Center)
        .enabledBetween(390, 404),
      TextMessage("It could not land upright, but there was no damage.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(390, 404),
      TextMessage("AutoTune was not finished, but based on the logs,",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 2.5f, HA.Center, VA.Center)
        .enabledBetween(395, 404),
      TextMessage("one can retrieve the tested PID values and restart from these",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 3.5f, HA.Center, VA.Center)
        .enabledBetween(395, 404),
      TextMessage("if they are not a complete nonsense.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 4.5f, HA.Center, VA.Center)
        .enabledBetween(395, 404),

      LastLogMessage(reader, logTimeOffset, logCF, width * 0.2f, height * 0.82f)
        .enabledBetween(8, 410),
      BlinkingCircle(115.05, 0.2, 116.05, width * 0.76, height * 0.418, blinkInnerR, blinkOuterR, blinkColor),
      BlinkingCircle(139.7, 0.2, 140.7, width, height * 0.542, blinkInnerR, blinkOuterR, blinkColor),
      BlinkingCircle(159.7, 0.2, 160.7, width, height * 0.511, blinkInnerR, blinkOuterR, blinkColor),
      BlinkingCircle(170.4, 0.2, 171.4, width, height * 0.349, blinkInnerR, blinkOuterR, blinkColor),
      BlinkingCircle(215.8, 0.2, 216.8, width, height * 0.233, blinkInnerR, blinkOuterR, blinkColor),
      BlinkingCircle(258.05, 0.2, 259.05, width * 0.310, height * 0.426, blinkInnerR, blinkOuterR, blinkColor),
      BlinkingCircle(318.05, 0.2, 319.05, width * 0.368, height * 0.434, blinkInnerR, blinkOuterR, blinkColor),
      BlinkingCircle(346.05, 0.2, 347.05, width * 0.342, height * 0.418, blinkInnerR, blinkOuterR, blinkColor),
      Fade(timeOn = 409.5, timeOff = 410),
    )

    props.runVideo(allGraphics)
  end apply
  