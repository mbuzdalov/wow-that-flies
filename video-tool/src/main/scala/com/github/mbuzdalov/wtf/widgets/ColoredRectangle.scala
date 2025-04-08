package com.github.mbuzdalov.wtf.widgets

import java.awt.image.BufferedImage
import java.awt.{Color, Graphics2D}

import com.github.mbuzdalov.wtf.GraphicsConsumer

class ColoredRectangle(x: Double, y: Double, w: Double, h: Double, backgroundColor: Color) extends GraphicsConsumer:
  assert(w >= 0, "Width is negative, nothing will be visible")
  assert(h >= 0, "Height is negative, nothing will be visible")
  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    g.setColor(backgroundColor)
    g.fillRect((x * img.getWidth).toInt, (y * img.getHeight).toInt, (w * img.getWidth).toInt, (h * img.getHeight).toInt)
  end consume
