package com.github.mbuzdalov.wtf.widgets

import java.awt.geom.Path2D
import java.awt.{BasicStroke, Color, Graphics2D}
import java.awt.image.BufferedImage

import com.github.mbuzdalov.wtf.{GraphicsConsumer, LogReader}

class NEDWidget(logReader: LogReader, timeOffset: Double,
                minN: Double, maxN: Double, minE: Double, maxE: Double, minAlt: Double, maxAlt: Double,
                visMinX: Double, visMaxX: Double, visMinY: Double, visMaxY: Double,
                yawOverride: Option[Double => Double]) extends GraphicsConsumer:
  private val xkfTiming = logReader.timingConnect("XKF1")
  private val posN = logReader.connect[Float]("XKF1", "PN")
  private val posE = logReader.connect[Float]("XKF1", "PE")
  private val posD = logReader.connect[Float]("XKF1", "PD")
  private val velD = logReader.connect[Float]("XKF1", "VD")
  private val xkfPitch = logReader.connect[Float]("XKF1", "Pitch")
  private val xkfRoll = logReader.connect[Float]("XKF1", "Roll")
  private val xkfYaw = logReader.connect[Float]("XKF1", "Yaw")

  // TODO: factor out
  private def smoothGet(conn: LogReader.Connector[Float], time: Double, index: Int): Double =
    val currV = conn.get(index)
    var minIndex = index
    while minIndex > 0 && conn.get(minIndex - 1) == currV do
      minIndex -= 1
    var nextIndex = index
    while nextIndex < conn.size && conn.get(nextIndex) == currV do
      nextIndex += 1
    val tMin = xkfTiming.get(minIndex)
    val tNext = xkfTiming.get(nextIndex)
    if minIndex == nextIndex then
      currV
    else
      (tNext - time) / (tNext - tMin) * currV + (time - tMin) / (tNext - tMin) * conn.get(nextIndex)

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val xkfIndex = xkfTiming.indexForTime(time + timeOffset)
    if xkfIndex >= 0 then
      val screenMinX = (visMinX * img.getWidth).toInt
      val screenMaxX = (visMaxX * img.getWidth).toInt
      val screenMinY = (visMinY * img.getHeight).toInt
      val screenMaxY = (visMaxY * img.getHeight).toInt

      val northMaxX = ((visMinX * 0.55 + visMaxX * 0.45) * img.getWidth).toInt
      val eastMinX = ((visMinX * 0.45 + visMaxX * 0.55) * img.getWidth).toInt

      g.setColor(new Color(0, 0, 0, 150))
      g.fillRect(screenMinX, screenMinY, northMaxX - screenMinX, screenMaxY - screenMinY)
      g.fillRect(eastMinX, screenMinY, screenMaxX - eastMinX, screenMaxY - screenMinY)

      g.setColor(new Color(255, 255, 255, 150))
      // draw the zero horizontal line and captions
      if minAlt <= 0 && 0 <= maxAlt then
        g.setStroke(new BasicStroke(img.getWidth / 1280f * 2))
        val zeroRelative = -minAlt / (maxAlt - minAlt)
        val zeroScreen = (screenMinY * zeroRelative + screenMaxY * (1 - zeroRelative)).toInt
        g.drawLine(screenMinX, zeroScreen, northMaxX, zeroScreen)
        g.drawLine(eastMinX, zeroScreen, screenMaxX, zeroScreen)
        val msgCF = TextMessage.ColorFont(24f * img.getWidth / 1280, Color.WHITE)
        TextMessage("North", msgCF, northMaxX - msgCF.fontSize * 0.2, zeroScreen,
          TextMessage.HorizontalAlignment.Right, TextMessage.VerticalAlignment.Bottom).consume(img, g, time, frameNo)
        TextMessage("East", msgCF, screenMaxX - msgCF.fontSize * 0.2, zeroScreen,
          TextMessage.HorizontalAlignment.Right, TextMessage.VerticalAlignment.Bottom).consume(img, g, time, frameNo)

      g.setStroke(new BasicStroke(img.getWidth / 1280f / 3))
      // draw other horizontal lines
      for h <- (minAlt - 2).toInt to (maxAlt + 2).toInt if h != 0 && minAlt <= h && h <= maxAlt do
        val hRelative = (h - minAlt) / (maxAlt - minAlt)
        val hScreen = (screenMinY * hRelative + screenMaxY * (1 - hRelative)).toInt
        g.drawLine(screenMinX, hScreen, northMaxX, hScreen)
        g.drawLine(eastMinX, hScreen, screenMaxX, hScreen)

      // draw north's vertical lines
      for x <- (minN - 2).toInt to (maxN + 2).toInt if minN <= x && x <= maxN do
        val xRelative = (x - minN) / (maxN - minN)
        val xScreen = (screenMinX * (1 - xRelative) + northMaxX * xRelative).toInt
        g.drawLine(xScreen, screenMinY, xScreen, screenMaxY)

      // draw east's vertical lines
      for x <- (minE - 2).toInt to (maxE + 2).toInt if minE <= x && x <= maxE do
        val xRelative = (x - minE) / (maxE - minE)
        val xScreen = (eastMinX * (1 - xRelative) + screenMaxX * xRelative).toInt
        g.drawLine(xScreen, screenMinY, xScreen, screenMaxY)

      val nScreen = (smoothGet(posN, time + timeOffset, xkfIndex) - minN) / (maxN - minN)
      val eScreen = (smoothGet(posE, time + timeOffset, xkfIndex) - minE) / (maxE - minE)
      val altScreen = (-smoothGet(posD, time + timeOffset, xkfIndex) - minAlt) / (maxAlt - minAlt)

      val northPoint = screenMinX * (1 - nScreen) + northMaxX * nScreen
      val eastPoint = eastMinX * (1 - eScreen) + screenMaxX * eScreen
      val altPoint = altScreen * screenMinY + (1 - altScreen) * screenMaxY

      val yaw = yawOverride match
        case Some(fun) => math.toRadians(fun(time)) // or time + offset? No idea
        case None => math.toRadians(smoothGet(xkfYaw, time + timeOffset, xkfIndex))
      val pitch = math.toRadians(smoothGet(xkfPitch, time + timeOffset, xkfIndex))
      val roll = math.toRadians(smoothGet(xkfRoll, time + timeOffset, xkfIndex))

      // positive pitch goes to (-cos yaw, -sin yaw, 0)
      // positive roll goes to  (-sin yaw, +cos yaw, 0)
      // yaw is clockwise in this perspective
      val z = math.cos(roll) * math.cos(pitch)
      val x0 = -math.sin(pitch)
      val y0 = math.sin(roll) * math.cos(pitch)
      val x = x0 * math.cos(yaw) + y0 * math.sin(yaw)
      val y = y0 * math.cos(yaw) - x0 * math.sin(yaw)

      val vHalfPointSizeInPx = 0.28 / (maxAlt - minAlt) * (screenMaxY - screenMinY)
      val hHalfPointSizeInPx = 0.14 / (maxN - minN) * (northMaxX - screenMinX) // assuming N and E are scaled similarly
      val pointSize = 0.01 * img.getHeight
      g.setColor(new Color(140, 30, 140))
      val northShape = new Path2D.Double()
      northShape.moveTo(northPoint - hHalfPointSizeInPx * z + vHalfPointSizeInPx * x, altPoint - vHalfPointSizeInPx * z - hHalfPointSizeInPx * x)
      northShape.lineTo(northPoint - hHalfPointSizeInPx * z - vHalfPointSizeInPx * x, altPoint + vHalfPointSizeInPx * z - hHalfPointSizeInPx * x)
      northShape.lineTo(northPoint + hHalfPointSizeInPx * z - vHalfPointSizeInPx * x, altPoint + vHalfPointSizeInPx * z + hHalfPointSizeInPx * x)
      northShape.lineTo(northPoint + hHalfPointSizeInPx * z + vHalfPointSizeInPx * x, altPoint - vHalfPointSizeInPx * z + hHalfPointSizeInPx * x)
      northShape.closePath()
      g.fill(northShape)

      val eastShape = new Path2D.Double()
      eastShape.moveTo(eastPoint - hHalfPointSizeInPx * z + vHalfPointSizeInPx * y, altPoint - vHalfPointSizeInPx * z - hHalfPointSizeInPx * y)
      eastShape.lineTo(eastPoint - hHalfPointSizeInPx * z - vHalfPointSizeInPx * y, altPoint + vHalfPointSizeInPx * z - hHalfPointSizeInPx * y)
      eastShape.lineTo(eastPoint + hHalfPointSizeInPx * z - vHalfPointSizeInPx * y, altPoint + vHalfPointSizeInPx * z + hHalfPointSizeInPx * y)
      eastShape.lineTo(eastPoint + hHalfPointSizeInPx * z + vHalfPointSizeInPx * y, altPoint - vHalfPointSizeInPx * z + hHalfPointSizeInPx * y)
      eastShape.closePath()
      g.fill(eastShape)
    end if
  end consume
