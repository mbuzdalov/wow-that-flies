package com.github.mbuzdalov.wtf

import java.awt.image.BufferedImage

trait FrameConsumer:
  def consume(img: BufferedImage, time: Double, frameNo: Long): Unit

object FrameConsumer:
  def compose(consumers: FrameConsumer*): FrameConsumer = 
    (img: BufferedImage, time: Double, frameNo: Long) => consumers.foreach(_.consume(img, time, frameNo))
    