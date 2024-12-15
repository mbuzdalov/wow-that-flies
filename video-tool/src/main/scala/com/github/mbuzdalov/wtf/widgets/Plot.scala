package com.github.mbuzdalov.wtf.widgets

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Graphics2D}

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.ColorFont
import com.github.mbuzdalov.wtf.{GraphicsConsumer, LogReader, Numbers}

class Plot private (logReader: LogReader, timeOffset: Double,
                    x: Int, y: Int, width: Int, height: Int, fontSize: Float, background: Color, timeWidth: Double,
                    nAdditionalHorizontalLines: Int,
                    sources: Seq[Plot.Source[?]]) extends GraphicsConsumer:
  private val timingConnections = sources.map(_.connectTiming(logReader))
  private val plotConnections = sources.map(_.connectValue(logReader))
  private val thinStroke = new BasicStroke(fontSize / 13f)
  private val veryThinStroke = new BasicStroke(fontSize / 26f)
  private val borderStroke = new BasicStroke(fontSize / 5f)
  private val plotStroke = new BasicStroke(fontSize / 7f)
  private var backgroundColor = background

  // lazy because of background color
  private lazy val legendLabels = sources.indices.map: i =>
    val s = sources(i)
    TextMessage(s.displayName, ColorFont(fontSize, s.color, backgroundColor),
      x + 0.5f * fontSize, y + 1.5f * fontSize * (1 + i),
      TextMessage.HorizontalAlignment.Left, TextMessage.VerticalAlignment.Bottom)

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

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    if backgroundColor == null then initBackgroundFromBackground(img)
    val lineColor = Plot.lineColorFromBackgroundColor(backgroundColor)

    g.setColor(backgroundColor)
    g.fillRect(x, y, width, height)
    val t = time + timeOffset
    g.setColor(lineColor)
    g.setStroke(borderStroke)
    g.drawRect(x, y, width, height)
    g.setStroke(thinStroke)
    g.drawLine(x + width / 2, y, x + width / 2, y + height)
    g.drawLine(x, y + height / 2, x + width, y + height / 2)
    g.setStroke(veryThinStroke)
    for h <- 1 to nAdditionalHorizontalLines do
      val dY = (height * 0.5 * h / (nAdditionalHorizontalLines + 1)).toInt
      g.drawLine(x, y + height / 2 - dY, x + width, y + height / 2 - dY)
      g.drawLine(x, y + height / 2 + dY, x + width, y + height / 2 + dY)
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

    legendLabels.foreach(_.consume(img, g, time, frameNo))
    g.setClip(0, 0, img.getWidth, img.getHeight)
end Plot

object Plot:
  private def backgroundIsLight(color: Color): Boolean =
    color == null || (color.getRed | color.getGreen | color.getBlue) >= 128

  private def lineColorFromBackgroundColor(color: Color): Color =
    if backgroundIsLight(color) then Color.BLACK else Color.WHITE

  private class Source[+T](val recordName: String, val fieldName: String, val displayName: String,
                   extractor: T => Double, val minValue: Double, val maxValue: Double, val color: Color):
    private[Plot] def connectTiming(reader: LogReader): LogReader.TimingConnector = reader.timingConnect(recordName)
    private[Plot] def connectValue(reader: LogReader): LogReader.Connector[Double] =
      val original = reader.connect[T](recordName, fieldName)
      new LogReader.Connector[Double]:
        override def size: Int = original.size
        override def get(index: Int): Double = extractor(original.get(index))

  def createRollPitchPlot(logReader: LogReader, timeOffset: Double, name: "Roll" | "Pitch", channel: Int, maxAngle: Double,
                          x: Int, y: Int, width: Int, height: Int, fontSize: Float, background: Color, timeWidth: Double,
                          moreHorizontalLines: Int = 0): Plot =
    val builder = IndexedSeq.newBuilder[Source[Any]]
    if channel > 0 then
      val flapMin = logReader.getParameter(s"SERVO${channel}_MIN")
      val flapMax = logReader.getParameter(s"SERVO${channel}_MAX")
      builder += Source[Numbers.UInt16]("RCOU", s"C$channel", s"$name Flap", v => v.toDouble, flapMin, flapMax, lineColorFromBackgroundColor(background))
    end if
    builder += Source[Float]("ATT", s"Des$name", s"Desired $name", v => v, -maxAngle, +maxAngle,
      if backgroundIsLight(background) then Color.BLUE else Color.CYAN)
    builder += Source[Float]("ATT", name, s"Actual $name", v => v, -maxAngle, +maxAngle,
      if backgroundIsLight(background) then Color.RED.darker() else Color.RED.brighter().brighter())
    new Plot(
      logReader, timeOffset, x, y,
      width, height, fontSize, background, 2, moreHorizontalLines,
      builder.result()
    )

  def createXFlapPlot(logReader: LogReader, timeOffset: Double,
                      x: Int, y: Int, width: Int, height: Int, fontSize: Float, background: Color, timeWidth: Double,
                      moreHorizontalLines: Int = 0): Plot =
    new Plot(
      logReader, timeOffset, x, y,
      width, height, fontSize, background, 2, moreHorizontalLines,
      IndexedSeq(
        Source[Numbers.UInt16]("RCOU", "C5", "Flap ╱", v => v.toDouble, logReader.getParameter("SERVO5_MIN"), logReader.getParameter("SERVO5_MAX"),
          lineColorFromBackgroundColor(background)),
        Source[Numbers.UInt16]("RCOU", "C6", "Flap ╲", v => v.toDouble, logReader.getParameter("SERVO6_MIN"), logReader.getParameter("SERVO6_MAX"),
          if backgroundIsLight(background) then Color.MAGENTA.darker() else Color.MAGENTA.brighter()),
      )
    )

  def createYawPlot(logReader: LogReader, timeOffset: Double, ccwChannel: Int, cwChannel: Int,
                    x: Int, y: Int, width: Int, height: Int, fontSize: Float, background: Color, timeWidth: Double,
                    moreHorizontalLines: Int = 0): Plot =
    new Plot(
      logReader, timeOffset, x, y,
      width, height, fontSize, background, 2, moreHorizontalLines,
      IndexedSeq(
        Source[Numbers.UInt16]("RCOU", s"C$ccwChannel", s"CCW Motor", v => v.toDouble, 1000, 2000,
          lineColorFromBackgroundColor(background)),
        Source[Numbers.UInt16]("RCOU", s"C$cwChannel",  s"CW Motor",  v => v.toDouble, 1000, 2000,
          if backgroundIsLight(background) then Color.MAGENTA.darker() else Color.MAGENTA.brighter()),
        Source[Float]("ATT", s"DesYaw", s"Desired Yaw", v => (v + 180) % 360, 0, 360,
          if backgroundIsLight(background) then Color.BLUE else Color.CYAN),
        Source[Float]("ATT", "Yaw", s"Actual Yaw", v => (v + 180) % 360, 0, 360,
          if backgroundIsLight(background) then Color.RED.darker() else Color.RED.brighter().brighter()),
      )
    )
