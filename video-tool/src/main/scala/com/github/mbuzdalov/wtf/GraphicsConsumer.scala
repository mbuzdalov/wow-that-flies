package com.github.mbuzdalov.wtf

import java.awt.{AlphaComposite, Graphics2D}
import java.awt.image.BufferedImage

trait GraphicsConsumer:
  def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit
  def enabledBetween(minTime: Alpha, maxTime: Alpha): GraphicsConsumer =
    GraphicsConsumer.Enabler(this, minTime, maxTime)

object GraphicsConsumer:
  private class Enabler(base: GraphicsConsumer, timeOn: Alpha, timeOff: Alpha) extends GraphicsConsumer:
    override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
      if time >= timeOn.time && time <= timeOff.time then
        val alpha = math.min(timeOn.asOn(time), timeOff.asOff(time))
        val oldComposite = g.getComposite
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha.toFloat))
        base.consume(img, g, time, frameNo)
        g.setComposite(oldComposite)

  def compose(consumers: GraphicsConsumer*): GraphicsConsumer = 
    (img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long) => 
      consumers.foreach(_.consume(img, g, time, frameNo))
    