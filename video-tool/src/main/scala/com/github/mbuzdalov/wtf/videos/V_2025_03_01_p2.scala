package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_01_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 2)
    val logTimeOffset = autoArmedTimes(1) - props.armTime

    val stickW = width / 2
    val stickSize = 201 * stickW / 1920
    val stickGap = 21 * stickW / 1920
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickW - stickSize - stickGap, stickW + stickGap, stickGap)

    val rpGap = 11 * width / 1280
    val rpWidth = width / 6
    val rpHeight = height / 7
    val fontSize = 16f * width / 1280
    val rpBackground = new Color(255, 255, 255, 120)

    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", -1, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 3)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", -1, 30,
      rpGap * 2 + rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 5)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 3, 4,
      width - 2 * rpGap - 2 * rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 3)
    val flapPlot = Plot.createXFlapPlot(reader, logTimeOffset,
      width - rpGap - rpWidth, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2, 3)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val finalMsgFontSize = 36f * width / 1280
    val finalMsgStep = finalMsgFontSize * 1.5f
    val finalMsgCF = TextMessage.ColorFont(finalMsgFontSize, new Color(240, 200, 240))

    val allGraphics = GraphicsConsumer.compose(
      sticks, rollPlot, pitchPlot, yawPlot, flapPlot,

      BottomBlanket(0.25f, new Color(240, 240, 240, 200), 27.0, 27.4, 35.5, 35.9),
      TextMessage("Flying feels smooth enough, but flaps are close to their maximum deflections.",
        msgCF, width * 0.5, height * 0.8, HA.Center, VA.Center).enabledBetween(27.5, 35.5),
      TextMessage("One is worse than another because of non-zero roll input.",
        msgCF, width * 0.5, height * 0.8 + msgStep, HA.Center, VA.Center).enabledBetween(30, 35.5),

      BottomBlanket(0.25f, new Color(240, 240, 240, 200), 62.0, 62.4, 72.6, 73.0),
      TextMessage("Maximum pitch input was around 30 degrees forward.",
        msgCF, width * 0.5, height * 0.8, HA.Center, VA.Center).enabledBetween(62.5, 72.5),
      TextMessage("This is slightly smaller than on January 11th, but I think",
        msgCF, width * 0.5, height * 0.8 + msgStep, HA.Center, VA.Center).enabledBetween(62.5, 72.5),
      TextMessage("some more is possible with more throttle, which requires a bigger field.",
        msgCF, width * 0.5, height * 0.8 + 2 * msgStep, HA.Center, VA.Center).enabledBetween(62.5, 72.5),

      BottomBlanket(0.25f, new Color(240, 240, 240, 200), 84.0, 84.4, 89.6, 90.0),
      TextMessage("Descending is still too painful with big pitch inputs.",
        msgCF, width * 0.5, height * 0.8, HA.Center, VA.Center).enabledBetween(84.5, 89.5),
      TextMessage("Maybe I should resort to vertical landings, like Super Heavy rather than Starship.",
        msgCF, width * 0.5, height * 0.8 + msgStep, HA.Center, VA.Center).enabledBetween(84.5, 89.5),

      BottomBlanket(0.25f, new Color(240, 240, 240, 200), 110.0, 110.4, 118.6, 119.0),
      TextMessage("When flying here, it feels less stable in pitch,",
        msgCF, width * 0.5, height * 0.8, HA.Center, VA.Center).enabledBetween(110.5, 118.5),
      TextMessage("maybe because I'm flying into the wind.",
        msgCF, width * 0.5, height * 0.8 + msgStep, HA.Center, VA.Center).enabledBetween(110.5, 118.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 137.5, timeOff = 138.0),

      TextMessage("Any landing you can walk away from is a good one!",
        finalMsgCF, width * 0.5, height * 0.3, HA.Center, VA.Center).enabledBetween(138, 145),
      TextMessage("Gerald R. Massie, Chuck Yeager, etc.",
        finalMsgCF, width * 0.5, height * 0.3 + finalMsgStep, HA.Left, VA.Center).enabledBetween(138, 145),
      TextMessage("But seriously, an unexpected good landing mode was found,",
        finalMsgCF, width * 0.5, height * 0.3 + 2.5 * finalMsgStep, HA.Center, VA.Center).enabledBetween(140, 145),
      TextMessage("likely made possible by vacuuming to the vertical surface.",
        finalMsgCF, width * 0.5, height * 0.3 + 3.5 * finalMsgStep, HA.Center, VA.Center).enabledBetween(140, 145),
    )

    props.runVideo(allGraphics)
  end apply
