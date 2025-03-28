package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p1a:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      BottomBlanket(0.1f, new Color(240, 240, 240, 200), 1.0, 1.4, 5.5, 5.9),
      TextMessage("Landing replay from the pilot's perspective",
        msgCF, width * 0.5, height * 0.95, HA.Center, VA.Center).enabledBetween(1.5, 5.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 18.5, timeOff = 19.0),
    )

    props.runVideo(allGraphics)
  end apply
