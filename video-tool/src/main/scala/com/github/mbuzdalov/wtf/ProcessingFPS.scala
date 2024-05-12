package com.github.mbuzdalov.wtf
import java.awt.Font
import java.awt.image.BufferedImage

object ProcessingFPS extends FrameConsumer:
  private val myFont = new Font(Font.SANS_SERIF, Font.PLAIN, 18)
  private var prevFrameTime = 0L

  override def consume(img: BufferedImage, time: Double, frameNo: Long): Unit =
    val currFrameTime = System.nanoTime()
    if prevFrameTime != 0 && currFrameTime != prevFrameTime then
      val fps = 1e9 / (currFrameTime - prevFrameTime)
      val g = img.createGraphics()
      GraphicsUtils.setHints(g)
      g.setFont(myFont)
      g.drawString(f"FPS: $fps%.1f", 600, 30)
    prevFrameTime = currFrameTime

  override def close(): Unit = ()