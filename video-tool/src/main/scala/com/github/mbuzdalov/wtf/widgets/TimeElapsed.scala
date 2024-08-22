package com.github.mbuzdalov.wtf.widgets

import java.awt.image.BufferedImage
import java.awt.{Font, Graphics2D}

import com.github.mbuzdalov.wtf.GraphicsConsumer

class TimeElapsed(shouldDisplay: Double => Boolean, displayFun: Double => Double,
                  colorFont: TextMessage.ColorFont) extends GraphicsConsumer:
  private val font = new Font(Font.SANS_SERIF, Font.PLAIN, (colorFont.fontSize + 0.5).toInt)

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val t = displayFun(time)
    if shouldDisplay(time) then
      g.setColor(colorFont.fontColor)
      g.setFont(font)
      val intPart = t.toLong
      val decPart = ((t - intPart) * 100).toLong

      g.drawString(f"${intPart / 60}:${intPart % 60}%02d.$decPart%02d",
        (colorFont.fontSize * 0.2).toFloat, (img.getHeight - colorFont.fontSize * 0.2).toFloat)
