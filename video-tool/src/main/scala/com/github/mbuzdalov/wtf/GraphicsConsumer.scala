package com.github.mbuzdalov.wtf

import java.awt.Graphics2D
import java.awt.image.BufferedImage

trait GraphicsConsumer:
  def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit

object GraphicsConsumer:
  def compose(consumers: GraphicsConsumer*): GraphicsConsumer = 
    (img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long) => 
      consumers.foreach(_.consume(img, g, time, frameNo))
    