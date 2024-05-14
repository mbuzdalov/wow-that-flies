package com.github.mbuzdalov.wtf

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Graphics2D}

import scala.language.implicitConversions

class Sticks(logReader: LogReader, timeOffset: Double,
             size: Int, xLeft: Int, xRight: Int, y: Int) extends GraphicsConsumer:
  private val timing = logReader.timingConnect("RCIN")
  private val rcIn = (1 to 4).map(i => logReader.connect[Numbers.UInt16]("RCIN", s"C$i"))
  private val borderColor = Color.RED
  private val fillColor = new Color(0, 0, 0, 150)
  private val thinStroke = new BasicStroke(size * 1e-2f)
  private val borderStroke = new BasicStroke(size * 3e-2f)
  private val stickColor = Color.YELLOW

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    g.setColor(fillColor)
    g.fillRect(xLeft, y, size, size)
    g.fillRect(xRight, y, size, size)
    g.setColor(borderColor)
    g.setStroke(thinStroke)
    g.drawLine(xLeft, y + size / 2, xLeft + size, y + size / 2)
    g.drawLine(xRight, y + size / 2, xRight + size, y + size / 2)
    g.drawLine(xLeft + size / 2, y, xLeft + size / 2, y + size)
    g.drawLine(xRight + size / 2, y, xRight + size / 2, y + size)
    g.setStroke(borderStroke)
    g.drawRect(xLeft, y, size, size)
    g.drawRect(xRight, y, size, size)

    val index = timing.indexForTime(time + timeOffset)
    if index >= 0 then
      val Seq(roll, pitch, throttle, yaw) = rcIn.map(_.get(index))
      val x1 = xLeft + size * (yaw - 1000) / 1000
      val y1 = y + size * (2000 - throttle) / 1000
      val x2 = xRight + size * (roll - 1000) / 1000
      val y2 = y + size * (2000 - pitch) / 1000
      g.setColor(stickColor)
      val stickR = math.max(1, size / 20)
      val stickD = stickR * 2 + 1
      g.fillOval(x1 - stickR, y1 - stickR, stickD, stickD)
      g.fillOval(x2 - stickR, y2 - stickR, stickD, stickD)
