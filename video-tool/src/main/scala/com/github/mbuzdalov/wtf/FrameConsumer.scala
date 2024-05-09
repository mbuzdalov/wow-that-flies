package com.github.mbuzdalov.wtf

import java.awt.image.BufferedImage

trait FrameConsumer:
  def consume(img: BufferedImage, time: Double, frameNo: Long): Unit
  