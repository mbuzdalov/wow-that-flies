package com.github.mbuzdalov.wtf.videos

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Graphics2D}

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.util.Viridis
import com.github.mbuzdalov.wtf.widgets.TextMessage.{ColorFont, HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer, LogReader}

object V_2024_11_14_SN1_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val stickW = ((1280.0 / 1920) * width / 2).toInt
    val stickSize = 201 * stickW / 1280
    val stickGap = 21 * stickW / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      stickW - stickSize - stickGap, stickW + stickGap, stickGap)

    val rpGap = 11 * width / 1280
    val rpWidth = (width - 5 * rpGap) / 4
    val rpHeight = (height * (1 - 1280.0 / 1920) - 2 * rpGap).toInt
    val fontSize = 16f * width / 1280
    val rpBackground = Color.BLACK

    val bottomPlotH = height - rpHeight - rpGap

    val rollPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Roll", -1, 50,
      rpGap, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2, 4)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", -1, 50,
      rpGap * 2 + rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2, 4)
    val yawPlot = Plot.createYawPlot(reader, logTimeOffset, 3, 4,
      rpGap * 3 + 2 * rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val flapPlot = Plot.createXFlapPlot(reader, logTimeOffset,
      rpGap * 4 + 3 * rpWidth, bottomPlotH, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 26f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, Color.YELLOW)
    val msgLeft = (1280.0 / 1920) * width + 0.3 * msgFontSize
    val msgTop = msgFontSize


    val pitchFailR = width * (1920 - 1280) / 1920.0 / 3.5
    val pitchFail = new PitchFailureWidget(reader, logTimeOffset, 9,
      width * (1920 + 1280) / 1920.0 / 2, bottomPlotH - pitchFailR - msgFontSize, pitchFailR,
      fontSize, 40)

    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot, yawPlot, flapPlot,
      pitchFail.enabledBetween(10, 205),
      TextMessage("This flight immediately followed.",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(1, 10),
      TextMessage("the failed belly flop.",
        msgCF, msgLeft, msgTop + msgStep * 1, HA.Left, VA.Center)
        .enabledBetween(1, 10),
      TextMessage("I concentrated on getting more data",
        msgCF, msgLeft, msgTop + msgStep * 2.5, HA.Left, VA.Center)
        .enabledBetween(3, 10),
      TextMessage("in the Stabilize mode.",
        msgCF, msgLeft, msgTop + msgStep * 3.5, HA.Left, VA.Center)
        .enabledBetween(3, 10),

      TextMessage("Explanation:",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(11, 40),
      TextMessage("- direction: GPS direction",
        msgCF, msgLeft, msgTop + msgStep * 1, HA.Left, VA.Center)
        .enabledBetween(11, 40),
      TextMessage("- distance: desired pitch (max 40)",
        msgCF, msgLeft, msgTop + msgStep * 2, HA.Left, VA.Center)
        .enabledBetween(11, 40),
      TextMessage("- radius: grows with GPS speed",
        msgCF, msgLeft, msgTop + msgStep * 3, HA.Left, VA.Center)
        .enabledBetween(11, 40),
      TextMessage("- color: more yellow = worse pitch",
        msgCF, msgLeft, msgTop + msgStep * 4, HA.Left, VA.Center)
        .enabledBetween(11, 40),

      TextMessage("First yellow spots arrived where",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(64, 70),
      TextMessage("SN1 really struggled in pitch...",
        msgCF, msgLeft, msgTop + 1 * msgStep, HA.Left, VA.Center)
        .enabledBetween(64, 70),

      TextMessage("More of the struggling,",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(120, 130),
      TextMessage("notably in the same direction.",
        msgCF, msgLeft, msgTop + 1 * msgStep, HA.Left, VA.Center)
        .enabledBetween(120, 130),

      TextMessage("Struggling in the opposite",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(150, 165),
      TextMessage("direction too, but at higher",
        msgCF, msgLeft, msgTop + 1 * msgStep, HA.Left, VA.Center)
        .enabledBetween(150, 165),
      TextMessage("desired pitch values.",
        msgCF, msgLeft, msgTop + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(150, 165),

      TextMessage("The pattern seems to appear:",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(170, 185),
      TextMessage("SW: fails at pitch of 25",
        msgCF, msgLeft, msgTop + 1 * msgStep, HA.Left, VA.Center)
        .enabledBetween(172, 185),
      TextMessage("and bigger GPS speeds.",
        msgCF, msgLeft + 2 * msgFontSize, msgTop + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(172, 185),
      TextMessage("ESE: pitch 15, smaller speeds.",
        msgCF, msgLeft, msgTop + 3 * msgStep, HA.Left, VA.Center)
        .enabledBetween(174, 185),
      TextMessage("Wind at that moment was from E.",
        msgCF, msgLeft, msgTop + 4 * msgStep, HA.Left, VA.Center)
        .enabledBetween(178, 185),

      TextMessage("The inability to pitch hence",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(185, 194),
      TextMessage("depends on the forward air speed",
        msgCF, msgLeft, msgTop + 1 * msgStep, HA.Left, VA.Center)
        .enabledBetween(185, 194),
      TextMessage("seemingly more than on the pitch.",
        msgCF, msgLeft, msgTop + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(185, 194),

      TextMessage("Going home now.",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(194, 223),
      TextMessage("Last part of the test:",
        msgCF, msgLeft, msgTop + msgStep * 1.5, HA.Left, VA.Center)
        .enabledBetween(197, 223),
      TextMessage("don't miss the landing cage!",
        msgCF, msgLeft, msgTop + msgStep * 2.5, HA.Left, VA.Center)
        .enabledBetween(197, 223),
      TextMessage("Apparently this won't be easy...",
        msgCF, msgLeft, msgTop + msgStep * 4, HA.Left, VA.Center)
        .enabledBetween(210, 223),
      TextMessage("Landing failed successfully!",
        msgCF, msgLeft, msgTop + msgStep * 5.5, HA.Left, VA.Center)
        .enabledBetween(219, 223),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 223.5, timeOff = 224.0),
    )

    props.runVideo(allGraphics)
  end apply

  private class PitchFailureWidget(reader: LogReader, logTimeOffset: Double, minVideoTime: Double,
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
            val pitchRelativeError = math.abs(pitch - desPitch) / 5 // less than 1 is "OK", more is "not OK"
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
