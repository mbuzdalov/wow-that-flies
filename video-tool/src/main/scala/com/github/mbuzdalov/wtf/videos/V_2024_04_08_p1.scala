package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_08_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

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
    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", 3, 15,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 15,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(240, 240, 0)
    val msgCF = TextMessage.ColorFont(msgFontSize, msgColor, new Color(0, 0, 0, 120))
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("April 08, 2024",
        msgCF, width * 0.5f, height * 0.2f,
        HA.Center, VA.Center, 2, 15),
      TextMessage("Normally I would perform AutoTune outdoors,",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 1.5f,
        HA.Center, VA.Center, 3, 15),
      TextMessage("in no wind and no rain, especially for larger vehicles.",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 2.5f,
        HA.Center, VA.Center, 3, 15),
      TextMessage("This is the first filmed attempt for CX7.",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 3.5f,
        HA.Center, VA.Center, 3, 15),
      TextMessage("In ArduPilot, AutoTune is a flight mode that runs atop AltHold,",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 5f,
        HA.Center, VA.Center, 6, 15),
      TextMessage("another mode where the altitude is kept stable, and pilot does pitch and roll.",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 6f,
        HA.Center, VA.Center, 6, 15),
      TextMessage("On no pilot input, AutoTune performs twitches with different parameters",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 7f,
        HA.Center, VA.Center, 6, 15),
      TextMessage("and measures the response. The pilot only keeps the craft away from obstacles.",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 8f,
        HA.Center, VA.Center, 6, 15),

      Fade(timeOn = 23, timeOff = 23.5),

      TextMessage("Generalized Iceberg Theorem:",
        msgCF, width * 0.5f, height * 0.3f,
        HA.Center, VA.Center, 18, 27),
      TextMessage("Seven eighths of everything can't be seen.",
        msgCF, width * 0.5f, height * 0.3f + msgStep * 1f,
        HA.Center, VA.Center, 18, 27),

      TextMessage("Bernstein's Second Law:",
        msgCF, width * 0.5f, height * 0.5f,
        HA.Center, VA.Center, 22, 27),
      TextMessage("A falling body always rolls to the most inaccessible spot.",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 1f,
        HA.Center, VA.Center, 22, 27),
    )

    props.runVideo(allGraphics)
  end apply
  