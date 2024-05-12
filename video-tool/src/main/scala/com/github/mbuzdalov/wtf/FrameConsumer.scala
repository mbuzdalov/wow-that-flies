package com.github.mbuzdalov.wtf

import java.awt.image.BufferedImage

trait FrameConsumer extends AutoCloseable:
  def consume(img: BufferedImage, time: Double, frameNo: Long): Unit

object FrameConsumer:
  def compose(consumers: FrameConsumer*): FrameConsumer = new FrameConsumer:
    override def consume(img: BufferedImage, time: Double, frameNo: Long): Unit = 
      consumers.foreach(_.consume(img, time, frameNo))
    override def close(): Unit =
      consumers.foreach(_.close())
    