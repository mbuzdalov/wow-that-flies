package com.github.mbuzdalov.wtf.widgets

import java.awt.geom.{Area, Ellipse2D, Rectangle2D}
import java.awt.image.BufferedImage
import java.awt.{Color, Graphics2D}

import com.github.mbuzdalov.wtf.GraphicsConsumer

class GlowingRectangle(start: Double, period: Double, finish: Double,
                       x: Double, y: Double, w: Double, h: Double,
                       color: Color) extends GraphicsConsumer:
  private val realFinish = math.round((finish - start) / period) * period + start
  private val area = new Area(new Rectangle2D.Double(x, y, w, h))

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    if time >= start && time <= realFinish then
      val alpha = (1 - math.cos((time - start) / period * math.Pi * 2)) / 2
      g.setColor(new Color(color.getRed, color.getGreen, color.getBlue, (color.getAlpha * alpha).toInt))
      g.fill(area)
    end if
