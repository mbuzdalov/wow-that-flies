package com.github.mbuzdalov.wtf
import java.awt.{BasicStroke, Color, Font}
import java.awt.image.BufferedImage

import scala.compiletime.uninitialized

class Plot(logReader: LogReader, timeOffset: Double,
           x: Int, y: Int, width: Int, height: Int, fontSize: Float, background: Color, timeWidth: Double,
           sources: Seq[Plot.Source[?]]) extends FrameConsumer:
  private val timingConnections = sources.map(_.connectTiming(logReader))
  private val plotConnections = sources.map(_.connectValue(logReader))
  private val thinStroke = new BasicStroke(fontSize / 13f)
  private val borderStroke = new BasicStroke(fontSize / 5f)
  private val plotStroke = new BasicStroke(fontSize / 7f)
  private var backgroundColor = background
  private val legendFont = new Font(Font.SANS_SERIF, Font.PLAIN, (fontSize + 0.5f).toInt)

  private def initBackgroundFromBackground(img: BufferedImage): Unit =
    var sumRed, sumGreen, sumBlue = 0L
    var col = 0
    while col < height do
      var row = 0
      while row < width do
        val rgb = img.getRGB(x + row, y + col)
        sumRed += (rgb >>> 16) & 0xFF
        sumGreen += (rgb >>> 8) & 0xFF
        sumBlue += rgb & 0xFF
        row += 1
      col += 1
    val area = width * height * 255L
    backgroundColor = new Color(1 - (1 - sumRed.toFloat / area) * 0.6f,
      1 - (1 - sumGreen.toFloat / area) * 0.6f,
      1 - (1 - sumBlue.toFloat / area) * 0.6f)

  override def consume(img: BufferedImage, time: Double, frameNo: Long): Unit =
    if backgroundColor == null then initBackgroundFromBackground(img)

    val g = img.createGraphics()
    GraphicsUtils.setHints(g)
    g.setColor(backgroundColor)
    g.fillRect(x, y, width, height)
    val t = time + timeOffset
    g.setColor(Color.BLACK)
    g.setStroke(borderStroke)
    g.drawRect(x, y, width, height)
    g.setStroke(thinStroke)
    g.drawLine(x + width / 2, y, x + width / 2, y + height)
    g.drawLine(x, y + height / 2, x + width, y + height / 2)
    val minViewTime = t - timeWidth / 2
    val maxViewTime = t + timeWidth / 2


    g.setClip(x, y, width, height)

    g.setStroke(plotStroke)
    for plot <- sources.indices do
      val maxValue = sources(plot).maxValue
      val minValue = sources(plot).minValue
      g.setColor(sources(plot).color)
      val timing = timingConnections(plot)
      val conn = plotConnections(plot)
      val minIndex = math.max(0, timing.indexForTime(minViewTime) - 1)
      val maxIndex = math.min(timing.size - 1, timing.indexForTime(maxViewTime) + 1)
      var prev = -1.0
      var prevX, prevY = -1
      var i = minIndex
      while i <= maxIndex do
        val curr = conn.get(i)
        val currX = (x + width / timeWidth * (timing.get(i) - minViewTime)).toInt
        val currY = (y + height * (maxValue - conn.get(i)) / (maxValue - minValue)).toInt
        if i > minIndex then
          g.drawLine(prevX, prevY, currX, currY)
        prev = curr
        prevX = currX
        prevY = currY
        i += 1

    g.setFont(legendFont)
    for plot <- sources.indices do
      g.setColor(sources(plot).color)
      g.drawString(sources(plot).displayName, x + 0.5f * fontSize, y + 1.5f * fontSize * (1 + plot))

  override def close(): Unit = ()

object Plot:
  class Source[+T](val recordName: String, val fieldName: String, val displayName: String, 
                   extractor: T => Double, val minValue: Double, val maxValue: Double, val color: Color):
    private[Plot] def connectTiming(reader: LogReader): LogReader.TimingConnector = reader.timingConnect(recordName)
    private[Plot] def connectValue(reader: LogReader): LogReader.Connector[Double] =
      val original = reader.connect[T](recordName, fieldName)
      new LogReader.Connector[Double]:
        override def size: Int = original.size
        override def get(index: Int): Double = extractor(original.get(index))
