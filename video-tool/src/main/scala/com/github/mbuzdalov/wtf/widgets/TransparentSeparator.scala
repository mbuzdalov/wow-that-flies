package com.github.mbuzdalov.wtf.widgets

import java.awt.Graphics2D
import java.awt.image.BufferedImage

import com.github.mbuzdalov.wtf.GraphicsConsumer

class TransparentSeparator(x1: Double, y1: Double, x2: Double, y2: Double, w: Double) extends GraphicsConsumer:
  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val d12sq = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)
    // Yes, I can do this faster. But right now this does not matter much.
    for
      y <- 0 until img.getHeight
      x <- 0 until img.getWidth
    do
      val xi = x.toDouble / img.getWidth
      val yi = y.toDouble / img.getHeight
      val q = ((xi - x1) * (x2 - x1) + (yi - y1) * (y2 - y1)) / d12sq
      if q >= 0 && q <= 1 then
        val xp = x1 + (x2 - x1) * q
        val yp = y1 + (y2 - y1) * q
        val d2 = (xi - xp) * (xi - xp) + (yi - yp) * (yi - yp)
        val bq = d2 / (w * w)
        if bq < 1 then
          val bqr = math.pow(bq, 0.2)
          val rgb = img.getRGB(x, y)
          val rn = ((rgb & 0xFF) * bqr).toInt
          val gn = (((rgb >>> 8) & 0xFF) * bqr).toInt << 8
          val bn = (((rgb >>> 16) & 0xFF) * bqr).toInt << 16
          img.setRGB(x, y, rn ^ gn ^ bn)
