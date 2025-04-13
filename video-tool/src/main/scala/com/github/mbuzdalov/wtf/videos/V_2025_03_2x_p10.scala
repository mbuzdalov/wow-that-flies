package com.github.mbuzdalov.wtf.videos

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p10:
  def apply(props: BasicProperties): Unit =
    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(what = None, scale = t => 1.0 + t / 70, rotate = t => 0.0, scaleCenterX = (w, t) => 0.55 * w, scaleCenterY = (h, t) => 0.75 * h),
      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 6.5, timeOff = 7),
    )

    props.runImage(7, allGraphics)
  end apply
  