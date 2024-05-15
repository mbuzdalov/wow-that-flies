package com.github.mbuzdalov.wtf.widgets

import java.awt.image.BufferedImage
import java.awt.{Color, Font, Graphics2D}

import com.github.mbuzdalov.wtf.GraphicsConsumer

object ProcessingFPS extends GraphicsConsumer:
  private val myFont = new Font(Font.SANS_SERIF, Font.PLAIN, 18)
  private var prevFrameTime = 0L

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val currFrameTime = System.nanoTime()
    if prevFrameTime != 0 && currFrameTime != prevFrameTime then
      val fps = 1e9 / (currFrameTime - prevFrameTime)
      g.setColor(Color.BLACK)
      g.setFont(myFont)
      g.drawString(f"FPS: $fps%.1f", 600, 30)
    prevFrameTime = currFrameTime
