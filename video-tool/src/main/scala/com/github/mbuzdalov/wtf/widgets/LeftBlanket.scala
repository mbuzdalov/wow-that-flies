package com.github.mbuzdalov.wtf.widgets

import java.awt.{Color, Graphics2D}
import java.awt.image.BufferedImage

import com.github.mbuzdalov.wtf.GraphicsConsumer

class LeftBlanket(maxWidth: Double, backgroundColor: Color,
                  timeOnStart: Double, timeOnStop: Double,
                  timeOffStart: Double, timeOffStop: Double) extends GraphicsConsumer:
  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val fillWidth = if time < timeOnStart then 0.0
    else if time < timeOnStop then maxWidth * (time - timeOnStart) / (timeOnStop - timeOnStart)
    else if time < timeOffStart then maxWidth
    else if time < timeOffStop then maxWidth * (time - timeOffStop) / (timeOffStart - timeOffStop)
    else 0.0
    
    if fillWidth != 0 then
      g.setColor(backgroundColor)
      g.fillRect(0, 0, (fillWidth * img.getWidth).toInt, img.getHeight)
  end consume
