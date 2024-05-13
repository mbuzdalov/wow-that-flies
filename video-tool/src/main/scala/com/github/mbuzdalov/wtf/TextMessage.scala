package com.github.mbuzdalov.wtf
import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage

import scala.compiletime.uninitialized

class TextMessage(text: String, fontSize: Float, fontColor: Color, x: Float, y: Float,
                  hAlignment: TextMessage.HorizontalAlignment,
                  vAlignment: TextMessage.VerticalAlignment,
                  timeOn: TextMessage.Alpha, timeOff: TextMessage.Alpha) extends GraphicsConsumer:
  private val font = new Font(Font.SANS_SERIF, Font.PLAIN, (fontSize + 0.5).toInt)
  private var realBounds: (Float, Float) = uninitialized

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    if time >= timeOn.time && time <= timeOff.time then
      g.setFont(font)
      val alpha = math.min(timeOn.asOn(time), timeOff.asOff(time))
      g.setColor(new Color(fontColor.getRed, fontColor.getGreen, fontColor.getBlue, (alpha * 255).toInt))
      if realBounds == null then computeBounds(g)
      g.drawString(text, realBounds._1, realBounds._2)

  private def computeBounds(g: Graphics2D): Unit =
    val stringBounds = font.getStringBounds(text, g.getFontRenderContext)
    val realX = hAlignment match
      case TextMessage.HorizontalAlignment.Left => x - stringBounds.getMinX
      case TextMessage.HorizontalAlignment.Center => x - stringBounds.getCenterX
      case TextMessage.HorizontalAlignment.Right => x - stringBounds.getMaxX
    val realY = vAlignment match
      case TextMessage.VerticalAlignment.Top => y - stringBounds.getMinY
      case TextMessage.VerticalAlignment.Center => y - stringBounds.getCenterY
      case TextMessage.VerticalAlignment.Bottom => y - stringBounds.getMaxY
    realBounds = (realX.toFloat, realY.toFloat)

object TextMessage:
  enum VerticalAlignment:
    case Top, Center, Bottom

  enum HorizontalAlignment:
    case Left, Center, Right

  case class Alpha(time: Double, speed: Double):
    def asOn(t: Double): Double = math.max(0, math.min(1, (t - time) * speed))
    def asOff(t: Double): Double = math.max(0, math.min(1, (time - t) * speed))

  given Conversion[Int, Alpha] with
    override def apply(time: Int): Alpha = Alpha(time, 2.0)
    
  given Conversion[Double, Alpha] with
    override def apply(time: Double): Alpha = Alpha(time, 2.0)
  
  extension (time: Double)
    infix def withSpeed(speed: Double): Alpha = Alpha(time, speed)

  extension (time: Int)
    infix def withSpeed(speed: Double): Alpha = Alpha(time, speed)
