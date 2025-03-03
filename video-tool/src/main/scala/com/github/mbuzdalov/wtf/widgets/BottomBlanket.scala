package com.github.mbuzdalov.wtf.widgets

import java.awt.image.BufferedImage
import java.awt.{Color, Graphics2D}

import com.github.mbuzdalov.wtf.GraphicsConsumer

class BottomBlanket(maxHeight: Double, backgroundColor: Color,
                    timeOnStart: Double, timeOnStop: Double,
                    timeOffStart: Double, timeOffStop: Double) extends GraphicsConsumer:
  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val fillHeight = if time < timeOnStart then 0.0
    else if time < timeOnStop then maxHeight * (time - timeOnStart) / (timeOnStop - timeOnStart)
    else if time < timeOffStart then maxHeight
    else if time < timeOffStop then maxHeight * (time - timeOffStop) / (timeOffStart - timeOffStop)
    else 0.0
    
    val fillHeightAbs = (fillHeight * img.getHeight).toInt
    
    if fillHeight != 0 then
      g.setColor(backgroundColor)
      g.fillRect(0, img.getHeight - fillHeightAbs, img.getWidth, img.getHeight)
  end consume
