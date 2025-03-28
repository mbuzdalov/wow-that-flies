package com.github.mbuzdalov.wtf.widgets

import java.awt.{Color, Graphics2D}
import java.awt.image.BufferedImage

import com.github.mbuzdalov.wtf.GraphicsConsumer

class BlinkingCircle(start: Double, period: Double, finish: Double,
                     x: Double => Double, y: Double => Double, rInner: Double, rOuter: Double,
                     color: Color) extends GraphicsConsumer:
  def this(start: Double, period: Double, finish: Double,
           x: Double, y: Double, rInner: Double, rOuter: Double,
           color: Color) = {
    this(start, period, finish, _ => x, _ => y, rInner, rOuter, color)
  }

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val remainder = (time - start) % period
    if time >= start && time <= finish && (remainder < period / 2 - 1e-9 || remainder > 1 - 1e-9) then
      val x = this.x(time) * img.getWidth
      val y = this.y(time) * img.getHeight
      val xMin = math.max(0, math.floor(x - rOuter).toInt)
      val xMax = math.min(img.getWidth - 1, math.ceil(x + rOuter).toInt)
      val yMin = math.max(0, math.floor(y - rOuter).toInt)
      val yMax = math.min(img.getHeight - 1, math.ceil(y + rOuter).toInt)
      if xMin <= xMax && yMin <= yMax then
        var yy = yMin
        while yy <= yMax do
          var xx = xMin
          while xx <= xMax do
            val dist = math.hypot(xx - x, yy - y)
            if dist >= rInner && dist <= rOuter then
              img.setRGB(xx, yy, color.getRGB)
            xx += 1
          yy += 1
    end if
