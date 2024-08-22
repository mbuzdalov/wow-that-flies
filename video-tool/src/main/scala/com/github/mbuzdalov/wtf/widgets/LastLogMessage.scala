package com.github.mbuzdalov.wtf.widgets

import java.awt.Graphics2D
import java.awt.image.BufferedImage

import scala.compiletime.uninitialized

import com.github.mbuzdalov.wtf.{GraphicsConsumer, LogReader}

class LastLogMessage(logReader: LogReader, logTimeOffset: Double,
                     colorFont: TextMessage.ColorFont, x: Float, y: Float) extends GraphicsConsumer:
  private val msgConnectT = logReader.timingConnect("MSG")
  private val msgConnectM = logReader.connect[String]("MSG", "Message")
  private var message: TextMessage = uninitialized

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val t0 = time + logTimeOffset
    val idx = msgConnectT.indexForTime(t0)
    if msgConnectT.get(idx) < t0 - 2 then
      message = null
    else if message == null || msgConnectM.get(idx) != message.text then
      message = TextMessage(msgConnectM.get(idx), colorFont, x, y,
            TextMessage.HorizontalAlignment.Left, TextMessage.VerticalAlignment.Bottom)
    end if
    if message != null then message.consume(img, g, time, frameNo)
