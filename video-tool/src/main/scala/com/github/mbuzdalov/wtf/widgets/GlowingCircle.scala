package com.github.mbuzdalov.wtf.widgets

import java.awt.{Color, Graphics2D}
import java.awt.geom.{Area, Ellipse2D}
import java.awt.image.BufferedImage

import com.github.mbuzdalov.wtf.GraphicsConsumer

class GlowingCircle(start: Double, period: Double, finish: Double,
                    x: Double, y: Double, xrInner: Double, yrInner: Double, rDelta: Double,
                    color: Color) extends GraphicsConsumer:
  private val realFinish = math.round((finish - start) / period) * period + start

  private val area = new Area()
  area.add(new Area(new Ellipse2D.Double(x - xrInner - rDelta, y - yrInner - rDelta, 2 * (xrInner + rDelta), 2 * (yrInner + rDelta))))
  area.subtract(new Area(new Ellipse2D.Double(x - xrInner, y - yrInner, 2 * xrInner, 2 * yrInner)))

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    if time >= start && time <= realFinish then
      val alpha = (1 - math.cos((time - start) / period * math.Pi * 2)) / 2
      g.setColor(new Color(color.getRed, color.getGreen, color.getBlue, (color.getAlpha * alpha).toInt))
      g.fill(area)
    end if
