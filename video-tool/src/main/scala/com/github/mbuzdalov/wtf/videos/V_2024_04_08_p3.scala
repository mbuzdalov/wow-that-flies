package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_08_p3:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 3)
    val logTimeOffset = autoArmedTimes(1) - props.armTime

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
    val msgCF1 = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))
    val msgCF2 = TextMessage.ColorFont(msgFontSize, new Color(240, 240, 0))
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("April 08, 2024",
        msgCF1, width * 0.5f, height * 0.2f, HA.Center, VA.Center)
        .enabledBetween(1, 8),
      TextMessage("After re-printing the broken leg and re-assembling CX7,",
        msgCF1, width * 0.5f, height * 0.7f, HA.Center, VA.Center)
        .enabledBetween(3, 8),
      TextMessage("I also found out that SERVO_RATE is set to just 50.",
        msgCF1, width * 0.5f, height * 0.7f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(3, 8),
      TextMessage("For digital servos, it's safe to set it to 400, which I did.",
        msgCF1, width * 0.5f, height * 0.7f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(3, 8),

      TextMessage("Well, it can tolerate inputs for more than 20 degrees in pitch.",
        msgCF1, width * 0.5f, height * 0.7f, HA.Center, VA.Center)
        .enabledBetween(31, 37),
      TextMessage("Maybe the 400Hz change was useful. Pitch still suffers though.",
        msgCF1, width * 0.5f, height * 0.7f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(31, 37),

      TextMessage("The landing of CX7 was also not successful due to the pilot's error.",
        msgCF2, width * 0.5f, height * 0.5f, HA.Center, VA.Center)
        .enabledBetween(40, 47),
      TextMessage("Someday I will have to invest in a proper tripod. Someday.",
        msgCF2, width * 0.5f, height * 0.5f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(40, 47),
    )

    props.runVideo(allGraphics)
  end apply
  