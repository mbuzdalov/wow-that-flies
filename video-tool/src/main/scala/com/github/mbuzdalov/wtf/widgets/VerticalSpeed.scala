package com.github.mbuzdalov.wtf.widgets

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.util.Locale

import com.github.mbuzdalov.wtf.{GraphicsConsumer, LogReader}

class VerticalSpeed(logReader: LogReader, timeOffset: Double,
                    colorFont: TextMessage.ColorFont,
                    xFun: Double => Double, yFun: Double => Double,
                    hAlignment: TextMessage.HorizontalAlignment,
                    vAlignment: TextMessage.VerticalAlignment) extends GraphicsConsumer:
  def this(logReader: LogReader, timeOffset: Double, colorFont: TextMessage.ColorFont,
           x: Double, y: Double, hAlignment: TextMessage.HorizontalAlignment, vAlignment: TextMessage.VerticalAlignment) =
    this(logReader, timeOffset, colorFont, _ => x, _ => y, hAlignment, vAlignment)

  private val xkfTiming = logReader.timingConnect("XKF1")
  private val velD = logReader.connect[Float]("XKF1", "VD")

  // TODO: factor out
  private def smoothGet(conn: LogReader.Connector[Float], time: Double, index: Int): Double =
    val currV = conn.get(index)
    var nextIndex = index
    while nextIndex < conn.size && conn.get(nextIndex) == currV do
      nextIndex += 1
    val tMin = xkfTiming.get(index)
    val tNext = xkfTiming.get(nextIndex)
    if index == nextIndex then
      currV
    else
      (tNext - time) / (tNext - tMin) * currV + (time - tMin) / (tNext - tMin) * conn.get(nextIndex)

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val t = time + timeOffset
    val index = xkfTiming.indexForTime(t)
    if index >= 0 then
      val value = -smoothGet(velD, t, index)
      val formatted = "V/sp: %+.01f m/s".formatLocal(Locale.US, value).replace('-', '−')
      TextMessage(formatted, colorFont, xFun, yFun, hAlignment, vAlignment).consume(img, g, time, frameNo)
