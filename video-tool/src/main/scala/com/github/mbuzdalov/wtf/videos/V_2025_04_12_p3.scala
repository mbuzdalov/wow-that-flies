package com.github.mbuzdalov.wtf.videos

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_04_12_p3:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val allGraphics = GraphicsConsumer.compose(
      TransparentSeparator(0.5, 0, 0.5, 1, 0.01),
      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 16.0, timeOff = 16.5),
    )

    props.runVideo(allGraphics)
  end apply
