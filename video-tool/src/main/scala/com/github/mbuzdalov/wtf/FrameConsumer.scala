package com.github.mbuzdalov.wtf

import java.awt.RenderingHints
import java.awt.image.BufferedImage

trait FrameConsumer extends AutoCloseable:
  def consume(img: BufferedImage, time: Double, frameNo: Long): Unit

object FrameConsumer:
  def apply(c: GraphicsConsumer): FrameConsumer = new FrameConsumer:
    override def consume(img: BufferedImage, time: Double, frameNo: Long): Unit =
      val g = img.createGraphics()
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
      c.consume(img, g, time, frameNo)
      g.dispose()  
    override def close(): Unit = ()

  def compose(consumers: FrameConsumer*): FrameConsumer = new FrameConsumer:
    override def consume(img: BufferedImage, time: Double, frameNo: Long): Unit = 
      consumers.foreach(_.consume(img, time, frameNo))
    override def close(): Unit =
      consumers.foreach(_.close())
    