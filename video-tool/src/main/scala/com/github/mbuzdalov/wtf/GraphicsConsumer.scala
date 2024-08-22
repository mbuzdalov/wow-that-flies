package com.github.mbuzdalov.wtf

import java.awt.{AlphaComposite, Graphics2D}
import java.awt.image.BufferedImage

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.util.{PiecewiseLinearFunction => PLW}

trait GraphicsConsumer:
  def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit
  def enabledBetween(minTime: Double, maxTime: Double): GraphicsConsumer =
    withAlpha(PLW(minTime, 0) plus (0.5, 1) to (maxTime - 0.5, 1) plus (0.5, 0))
  def enabledBetween(minTime: Alpha, maxTime: Alpha): GraphicsConsumer =
    GraphicsConsumer.Enabler(this, PLW(minTime.time, 0) plus (1.0 / minTime.speed, 1) to (maxTime.time - 1.0 / maxTime.speed, 1) to (maxTime.time, 0))
  def withAlpha(alphaFun: Double => Double): GraphicsConsumer =
    GraphicsConsumer.Enabler(this, alphaFun)

object GraphicsConsumer:
  private class Enabler(base: GraphicsConsumer, alphaFun: Double => Double) extends GraphicsConsumer:
    override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
      val alpha = alphaFun(time)
      if alpha > 0 then
        val oldComposite = g.getComposite
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha.toFloat))
        base.consume(img, g, time, frameNo)
        g.setComposite(oldComposite)

  def compose(consumers: GraphicsConsumer*): GraphicsConsumer = 
    (img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long) => 
      consumers.foreach(_.consume(img, g, time, frameNo))
    