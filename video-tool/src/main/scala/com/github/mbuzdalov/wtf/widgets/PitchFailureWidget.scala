package com.github.mbuzdalov.wtf.widgets

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Graphics2D}

import com.github.mbuzdalov.wtf.util.Viridis
import com.github.mbuzdalov.wtf.widgets.TextMessage.{ColorFont, HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{GraphicsConsumer, LogReader}

class PitchFailureWidget(reader: LogReader, logTimeOffset: Double, minVideoTime: Double,
                         x: Double, y: Double, r: Double,
                         fontSize: Float,
                         maxPitch: Double) extends GraphicsConsumer:
  private case class RecordPoint(error: Double, x: Int, y: Int, r: Int, rgb: Int)

  private val veryThinStroke = new BasicStroke(fontSize / 26f)
  private val borderStroke = new BasicStroke(fontSize / 5f)

  private val ekfTimingConnect = reader.timingConnect("XKF1")
  private val northVelConnect = reader.connect[Float]("XKF1", "VN")
  private val eastVelConnect = reader.connect[Float]("XKF1", "VE")

  private val attTimingConnect = reader.timingConnect("ATT")
  private val desPitchConnect = reader.connect[Float]("ATT", "DesPitch")
  private val pitchConnect = reader.connect[Float]("ATT", "Pitch")

  private val labelCF = ColorFont(fontSize, Color.WHITE, Color.BLACK)
  private val labels = IArray(
    TextMessage("N", labelCF, x, y - r, HA.Center, VA.Center),
    TextMessage("E", labelCF, x + r, y, HA.Center, VA.Center),
    TextMessage("S", labelCF, x, y + r, HA.Center, VA.Center),
    TextMessage("W", labelCF, x - r, y, HA.Center, VA.Center),
  )

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val t = time + logTimeOffset
    g.setColor(Color.WHITE)
    g.setStroke(borderStroke)
    g.drawOval((x - r).toInt, (y - r).toInt, (2 * r).toInt, (2 * r).toInt)
    g.setStroke(veryThinStroke)
    for m <- IArray(1.0 / 4, 2.0 / 4, 3.0 / 4) do
      g.drawOval((x - r * m).toInt, (y - r * m).toInt, (2 * r * m).toInt, (2 * r * m).toInt)
    labels.foreach(_.consume(img, g, time, frameNo))

    for yPix <- (y - r).toInt to (y + r).toInt do
      val scale = (yPix - (y + r).toInt).toDouble / ((y - r).toInt - (y + r).toInt)
      g.setColor(new Color(Viridis(scale)))
      g.fillRect((x + r + fontSize / 2).toInt, yPix, (fontSize / 5).toInt, 1)
      g.fillRect((x - r - fontSize / 2 - fontSize / 5).toInt, yPix, (fontSize / 5).toInt, 1)

    // I HATE MYSELF IT'S O(N*T) BUT ENCODING 4K FRAMES IS SLOWER THAN THIS
    val lastAttT = attTimingConnect.indexForTime(t)
    val recordPoints = IndexedSeq.newBuilder[RecordPoint]
    for attT <- 0 to lastAttT do
      // Don't log startup and the first hit-the-ground deviation
      if attTimingConnect.get(attT) >= minVideoTime + logTimeOffset then
        val desPitch = desPitchConnect.get(attT)
        val pitch = pitchConnect.get(attT)
        val ekfT = ekfTimingConnect.indexForTime(attTimingConnect.get(attT))
        val northVelocity = northVelConnect.get(ekfT)
        val eastVelocity = eastVelConnect.get(ekfT)
        val angle = math.atan2(northVelocity, eastVelocity)
        val desPitchFwd = -desPitch / maxPitch
        if desPitchFwd >= 0 || desPitchFwd <= 1 then
          val cx = x + r * math.cos(angle) * desPitchFwd
          val cy = y - r * math.sin(angle) * desPitchFwd
          val pitchRelativeError = math.abs(pitch - desPitch) / 5   // less than 1 is "OK", more is "not OK"
          val pitchScaledError = math.min(1, math.log1p(pitchRelativeError) / math.log1p(10.0 / 5))
          val pixelR = fontSize / 3 / (1 + 2 * math.exp(-math.hypot(northVelocity, eastVelocity)))
          recordPoints += RecordPoint(
            error = pitchRelativeError,
            x = (cx - pixelR).toInt,
            y = (cy - pixelR).toInt,
            r = (2 * pixelR).toInt,
            rgb = Viridis(pitchScaledError))

    g.setStroke(veryThinStroke)
    for r <- recordPoints.result().sortBy(_.error) do
      g.setColor(new Color(r.rgb))
      g.fillOval(r.x, r.y, r.r, r.r)
      g.setColor(Color.BLACK)
      g.drawOval(r.x, r.y, r.r, r.r)
