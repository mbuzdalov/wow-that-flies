package com.github.mbuzdalov.wtf.videos

import java.awt.{BasicStroke, Color, Font, Graphics2D}
import java.awt.image.BufferedImage

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer, LogReader}

object V_2024_06_23_p3:
  private class Background(width: Int, height: Int, centerX: Int) extends GraphicsConsumer:
    private val thinStroke = new BasicStroke(0.8f * width / 1280)
    private val centerStroke = new BasicStroke(1.6f * width / 1280)

    override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
      g.setColor(new Color(255, 244, 233))
      g.fillRect(0, 0, width, height)
      g.setColor(Color.BLACK)
      for mul <- Seq(0.5, 0.75, 1); masterRadius = (height * 0.4 * mul).toInt do
        g.setStroke(if mul == 0.75 then centerStroke else thinStroke)
        g.drawOval(centerX - masterRadius, height / 2 - masterRadius, 2 * masterRadius + 1, 2 * masterRadius + 1)

      g.setStroke(centerStroke)
      g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20 * width / 1280))

      for (angle, text) <- Seq((0.0, "Level"), (20.0, "Into the wind")) do
        val endX = centerX + height * 0.45 * math.cos(math.toRadians(angle))
        val endY = height / 2 + height * 0.45 * math.sin(math.toRadians(angle))

        val endXText = centerX + height * 0.45 * 1.02 * math.cos(math.toRadians(angle))
        val endYText = height / 2  + height * 0.45 * 1.02 * math.sin(math.toRadians(angle))

        g.drawLine(centerX, height / 2, endX.toInt, endY.toInt)
        g.drawString(text, endXText.toFloat, endYText.toFloat)

  private class Histogram(width: Int, height: Int, centerX: Int, log: LogReader, color: Color,
                          minFlightTime: Double, maxFlightTime: Double) extends GraphicsConsumer:
    private val data = locally:
      val attTiming = log.timingConnect("ATT")
      val pitch = log.connect[Float]("ATT", "Pitch")
      val roll = log.connect[Float]("ATT", "Roll")

      val servoTiming = log.timingConnect("RCOU")
      val pitchServo = log.connect[Short]("RCOU", "C4")
      val pitchMin = log.getParameter("SERVO4_MIN")
      val pitchTrim = log.getParameter("SERVO4_TRIM")
      val pitchMax = log.getParameter("SERVO4_MAX")

      val rateTiming = log.timingConnect("RATE")
      val pitchRate = log.connect[Float]("RATE", "P")

      val minAttIndex = attTiming.indexForTime(minFlightTime)
      val maxAttIndex = attTiming.indexForTime(maxFlightTime)

      val builder = IndexedSeq.newBuilder[(Double, Double, Double)]

      for attIndex <- minAttIndex to maxAttIndex do
        val time = attTiming.get(attIndex)
        val myRate = pitchRate.get(rateTiming.indexForTime(time))
        if math.abs(myRate) < 1.0 then
          val pitch0 = pitch.get(attIndex)
          val pitchV = math.toRadians(if math.abs(roll.get(attIndex)) < 30 then pitch0 else 180 - pitch0)
          val servoRaw = pitchServo.get(servoTiming.indexForTime(time))
          val servoVal = if servoRaw >= pitchTrim then (servoRaw - pitchTrim) / (pitchMax - pitchTrim) else (servoRaw - pitchTrim) / (pitchTrim - pitchMin)
          builder += ((math.cos(pitchV), math.sin(pitchV), servoVal))

      builder.result()

    override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
      g.setColor(color)
      val dotSize = (height * 0.01).toInt
      for (cos, sin, delta) <- data do
        val radius = (0.75 + delta * 0.25) * height * 0.4
        val x = (centerX + cos * radius).toInt
        val y = (height / 2 - sin * radius).toInt
        g.fillOval(x - dotSize, y - dotSize, 2 * dotSize + 1, 2 * dotSize + 1)

  end Histogram

  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val log1 = props.createLogReader("log-1")
    val log2 = props.createLogReader("log-2")

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))
    val msgCF1 = TextMessage.ColorFont(msgFontSize, new Color(100, 155, 0), new Color(255, 255, 255, 50))
    val msgCF2 = TextMessage.ColorFont(msgFontSize, new Color(0, 100, 255), new Color(255, 255, 255, 50))

    val allGraphics = GraphicsConsumer.compose(
      Background(width, height, height / 2),
      Histogram(width, height, height / 2, log1, new Color(100, 155, 0, 10), 83, 169).enabledBetween(3, 25),
      Histogram(width, height, height / 2, log2, new Color(0, 100, 255, 10), 80, 178).enabledBetween(7, 25),

      TextMessage("Servo output vs pitch:",
        msgCF, width * 0.78f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(1, 25),
      TextMessage("green: with wind off",
        msgCF1, width * 0.78f, height * 0.06f + msgStep * 1, HA.Center, VA.Center)
        .enabledBetween(3, 25),
      TextMessage("blue: with wind on",
        msgCF2, width * 0.78f, height * 0.06f + msgStep * 2, HA.Center, VA.Center)
        .enabledBetween(7, 25),

      TextMessage("Even when craft is level,",
        msgCF, width * 0.78f, height * 0.06f + msgStep * 3.5f, HA.Center, VA.Center)
        .enabledBetween(10, 25),
      TextMessage("the wind adds force",
        msgCF, width * 0.78f, height * 0.06f + msgStep * 4.5f, HA.Center, VA.Center)
        .enabledBetween(10, 25),
      TextMessage("rotating the top",
        msgCF, width * 0.78f, height * 0.06f + msgStep * 5.5f, HA.Center, VA.Center)
        .enabledBetween(10, 25),
      TextMessage("in the direction of the wind.",
        msgCF, width * 0.78f, height * 0.06f + msgStep * 6.5f, HA.Center, VA.Center)
        .enabledBetween(10, 25),

      TextMessage("This looks a lot like",
        msgCF, width * 0.78f, height * 0.06f + msgStep * 8f, HA.Center, VA.Center)
        .enabledBetween(15, 25),
      TextMessage("a special case of",
        msgCF, width * 0.78f, height * 0.06f + msgStep * 9f, HA.Center, VA.Center)
        .enabledBetween(15, 25),
      TextMessage("the Coandă effect that killed",
        msgCF, width * 0.78f, height * 0.06f + msgStep * 10f, HA.Center, VA.Center)
        .enabledBetween(15, 25),
      TextMessage("the Hiller Flying Platform.",
        msgCF, width * 0.78f, height * 0.06f + msgStep * 11f, HA.Center, VA.Center)
        .enabledBetween(15, 25),
      TextMessage("More investigation is needed.",
        msgCF, width * 0.78f, height * 0.06f + msgStep * 12.5f, HA.Center, VA.Center)
        .enabledBetween(20, 25),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 24.5, timeOff = 25),
    )

    props.runEmpty(25, allGraphics)
  end apply
  
