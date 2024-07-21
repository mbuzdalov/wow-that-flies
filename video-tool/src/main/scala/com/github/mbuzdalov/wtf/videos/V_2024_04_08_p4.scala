package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_08_p4:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 3)
    val logTimeOffset = autoArmedTimes(2) - props.armTime

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
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("Flying the same setup again.",
        msgCF, width * 0.5f, height * 0.23f, HA.Center, VA.Center)
        .enabledBetween(1, 7),
      TextMessage("Just because a test ending in a crash cannot be called successful :)",
        msgCF, width * 0.5f, height * 0.23f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(2, 7),

      TextMessage("This time, apart from short twitches,",
        msgCF, width * 0.5f, height * 0.8f, HA.Center, VA.Center)
        .enabledBetween(23, 28),
      TextMessage("I also perform longer moves with the same attitude.",
        msgCF, width * 0.5f, height * 0.8f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(23, 28),
      TextMessage("The reason is that CX7 tracks long moves much worse than short ones.",
        msgCF, width * 0.5f, height * 0.8f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(23, 28),

      TextMessage("Well, CX7 is alive and kicking,",
        msgCF, width * 0.5f, height * 0.33f, HA.Center, VA.Center)
        .enabledBetween(87, 94),
      TextMessage("and the 400Hz change is most likely beneficial.",
        msgCF, width * 0.5f, height * 0.33f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(87, 94),
      TextMessage("But more manual tuning is needed before attempting AutoTune again.",
        msgCF, width * 0.5f, height * 0.33f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(87, 94),
    )

    props.runVideo(allGraphics)
  end apply
  