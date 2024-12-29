package com.github.mbuzdalov.wtf

import java.awt.{AlphaComposite, Graphics2D}
import java.awt.image.BufferedImage

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.util.{PiecewiseLinearFunction => PLW}

trait GraphicsConsumer:
  def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit
  def enabledBetween(minTime: Double, maxTime: Double): GraphicsConsumer =
    withAlpha(PLW(minTime, 0) to (minTime + 0.5, 1) to (maxTime - 0.5, 1) to (maxTime, 0))
  def withAlpha(alphaFun: Double => Double): GraphicsConsumer =
    GraphicsConsumer.TimeEnabler(this, alphaFun)
  def whenTopLeftBlack: GraphicsConsumer =
    GraphicsConsumer.PixelEnabler(this, 0, 0, 10, rgb => ((rgb | (rgb >>> 8) | (rgb >>> 16)) & 0xff) < 4)

object GraphicsConsumer:
  private class TimeEnabler(base: GraphicsConsumer, alphaFun: Double => Double) extends GraphicsConsumer:
    override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
      val alpha = alphaFun(time)
      if alpha > 0 then
        val oldComposite = g.getComposite
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha.toFloat))
        base.consume(img, g, time, frameNo)
        g.setComposite(oldComposite)

  private class PixelEnabler(base: GraphicsConsumer, x: Int, y: Int, wh: Int, colorPredicate: Int => Boolean) extends GraphicsConsumer:
    override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
      var good = true
      for 
        dx <- 0 until wh
        dy <- 0 until wh
      do good &= colorPredicate(img.getRGB(x + dx, y + dy))

      if good then
        base.consume(img, g, time, frameNo)

  def compose(consumers: GraphicsConsumer*): GraphicsConsumer = 
    (img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long) => 
      consumers.foreach(_.consume(img, g, time, frameNo))
    