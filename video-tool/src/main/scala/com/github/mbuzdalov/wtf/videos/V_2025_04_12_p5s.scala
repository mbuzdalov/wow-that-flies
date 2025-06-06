package com.github.mbuzdalov.wtf.videos

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_04_12_p5s:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val allGraphics = GraphicsConsumer.compose(
      TransparentSeparator(0.5, 0, 0.5, 1, 0.01),
      Fade(timeOn = 0.1, timeOff = 0),
      Fade(timeOn = 12.5, timeOff = 13.0),
    )

    props.runVideo(allGraphics)
  end apply
