package com.github.mbuzdalov.wtf.videos

import java.awt.geom.Path2D
import java.awt.{Color, Graphics2D}
import java.awt.image.BufferedImage

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, ScaleRotateCropBack, TextMessage}
import com.github.mbuzdalov.wtf.{Alpha, BasicProperties, GraphicsConsumer}

object V_2024_05_09_p5:
  private class Arrow(baseX: Double, baseY: Double, length: Double, halfWidth: Double,
                      timeOn: Alpha, timeOff: Alpha) extends GraphicsConsumer:
    private val shape = new Path2D.Double()
    shape.moveTo(baseX - halfWidth, baseY - halfWidth)
    shape.lineTo(baseX + length, baseY - halfWidth)
    shape.lineTo(baseX + length, baseY - 3 * halfWidth)
    shape.lineTo(baseX + length + 3 * halfWidth, baseY)
    shape.lineTo(baseX + length, baseY + 3 * halfWidth)
    shape.lineTo(baseX + length, baseY + halfWidth)
    shape.lineTo(baseX - halfWidth, baseY + halfWidth)
    shape.closePath()

    override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
      if time >= timeOn.time && time <= timeOff.time then
        val alpha = math.min(timeOn.asOn(time), timeOff.asOff(time))
        g.setColor(new Color(10, 10, 50, (alpha * 255).toInt))
        g.fill(shape)

  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 30f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(t => 1.0 + t / 100, t => t / 5 / 360 * math.Pi),
      TextMessage("Why did yaw",
        msgCF, width * 0.035f, height * 0.1f,
        HA.Left, VA.Center, 1, 14.5),
      TextMessage("fail this time?",
        msgCF, width * 0.035f, height * 0.1f + msgStep * 1f,
        HA.Left, VA.Center, 1, 14.5),
      TextMessage("The most likely",
        msgCF, width * 0.035f, height * 0.1f + msgStep * 2.5f,
        HA.Left, VA.Center, 3, 14.5),
      TextMessage("cause was the",
        msgCF, width * 0.035f, height * 0.1f + msgStep * 3.5f,
        HA.Left, VA.Center, 3, 14.5),
      TextMessage("windage of",
        msgCF, width * 0.035f, height * 0.1f + msgStep * 4.5f,
        HA.Left, VA.Center, 3, 14.5),
      TextMessage("the flight",
        msgCF, width * 0.035f, height * 0.1f + msgStep * 5.5f,
        HA.Left, VA.Center, 3, 14.5),
      TextMessage("controller!",
        msgCF, width * 0.035f, height * 0.1f + msgStep * 6.5f,
        HA.Left, VA.Center, 3, 14.5),
      TextMessage("The wind",
        msgCF, width * 0.035f, height * 0.68f,
        HA.Left, VA.Center, 7, 14.5),
      TextMessage("came this way",
        msgCF, width * 0.035f, height * 0.68f + msgStep * 1f,
        HA.Left, VA.Center, 7, 14.5),
      TextMessage("and applied",
        msgCF, width * 0.035f, height * 0.68f + msgStep * 2f,
        HA.Left, VA.Center, 7, 14.5),
      TextMessage("some strong torque.",
        msgCF, width * 0.035f, height * 0.68f + msgStep * 3f,
        HA.Left, VA.Center, 7, 14.5),
      Arrow(width * 0.035, height * 0.95, width * 0.41, height * 0.005, 6, 14.5),
      Fade(timeOff = 15.0, timeOn = 14.2),
    )
    props.runImage(15, allGraphics)
  end apply
