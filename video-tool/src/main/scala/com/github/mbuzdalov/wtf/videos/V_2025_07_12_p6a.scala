package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}

object V_2025_07_12_p6a:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val bigFontSize = 54f * width / 1280
    val bigStep = bigFontSize * 1.5f
    val bigCF = TextMessage.ColorFont(bigFontSize, new Color(90, 0, 50))

    val blanketBackground = new Color(255, 255, 255, 200)

    val allGraphics = GraphicsConsumer.compose(
      TextMessage("AutoTune",
        bigCF, 0.5 * width, 0.4 * height, HA.Center, VA.Center)
        .enabledBetween(1, 5),
      TextMessage("Phase 1: Roll",
        bigCF, 0.5 * width, 0.4 * height + bigStep, HA.Center, VA.Center)
        .enabledBetween(3, 5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 19.8, timeOff = 20.0),
    )

    props.runVideo(allGraphics)
  end apply
