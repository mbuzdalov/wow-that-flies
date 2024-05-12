package com.github.mbuzdalov.wtf

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color}

class Sticks(logReader: LogReader, timeOffset: Double,
             size: Int, xLeft: Int, xRight: Int, y: Int) extends FrameConsumer:
  private val timing = logReader.timingConnect("RCIN")
  private val rcIn = (1 to 4).map(i => logReader.connect[Numbers.UInt16]("RCIN", s"C$i"))
  private val borderColor = Color.RED
  private val fillColor = new Color(0, 0, 0, 150)
  private val thinStroke = new BasicStroke(1)
  private val borderStroke = new BasicStroke(3)
  private val stickColor = Color.YELLOW

  override def consume(img: BufferedImage, time: Double, frameNo: Long): Unit =
    val g = img.createGraphics()
    GraphicsUtils.setHints(g)
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
      g.fillOval(x1 - 5, y1 - 5, 11, 11)
      g.fillOval(x2 - 5, y2 - 5, 11, 11)

  override def close(): Unit = ()