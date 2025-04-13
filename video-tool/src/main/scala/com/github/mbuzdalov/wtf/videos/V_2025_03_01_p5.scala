package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_01_p5:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(what = None, scale = t => 1.0 + t / 400, rotate = t => 0.0, scaleCenterY = (y, t) => y),
      BottomBlanket(0.2, new Color(255, 255, 255, 200), 1.6, 2, 6.1, 6.5),
      TextMessage("The landing cage has been significantly upgraded too!",
        msgCF, width * 0.5, height * 0.9, HA.Center, VA.Center)
        .enabledBetween(2, 6.1),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 6.5, timeOff = 7),
    )

    props.runImage(7, allGraphics)
  end apply
  