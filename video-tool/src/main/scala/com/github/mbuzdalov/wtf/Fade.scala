package com.github.mbuzdalov.wtf
import java.awt.{Color, Graphics2D}
import java.awt.image.BufferedImage

class Fade(timeOn: Double, timeOff: Double) extends GraphicsConsumer:
  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val scale = (math.tanh((time - timeOff) / (timeOn - timeOff) * 6 - 3) + 1) / 2
    assert(0 <= scale && scale <= 1)
    if scale < 0.995 then
      g.setColor(new Color(0, 0, 0, (255 * (1 - scale)).toInt))
      g.fillRect(0, 0, img.getWidth, img.getHeight)
