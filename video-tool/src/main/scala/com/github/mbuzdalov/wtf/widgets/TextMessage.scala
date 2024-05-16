package com.github.mbuzdalov.wtf.widgets

import java.awt.image.BufferedImage
import java.awt.{Color, Font, Graphics2D}

import scala.compiletime.uninitialized

import com.github.mbuzdalov.wtf.GraphicsConsumer

class TextMessage(text: String, colorFont: TextMessage.ColorFont, x: Float, y: Float,
                  hAlignment: TextMessage.HorizontalAlignment,
                  vAlignment: TextMessage.VerticalAlignment,
                  timeOn: TextMessage.Alpha, timeOff: TextMessage.Alpha) extends GraphicsConsumer:
  private val font = new Font(Font.SANS_SERIF, Font.PLAIN, (colorFont.fontSize + 0.5).toInt)
  private var rectOffset: (Int, Int, Int) = uninitialized
  private var textOffset: (Float, Float) = uninitialized
  private var backgroundImg: BufferedImage = uninitialized

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    if time >= timeOn.time && time <= timeOff.time then
      g.setFont(font)
      val alpha = math.min(timeOn.asOn(time), timeOff.asOff(time))
      val fontColor = colorFont.fontColor
      val backColor = colorFont.backgroundColor
      if textOffset == null then computeBounds(g)
      fillBackgroundImage(alpha)
      g.drawImage(backgroundImg, rectOffset._1, rectOffset._2, null)
      g.setColor(new Color(fontColor.getRed, fontColor.getGreen, fontColor.getBlue, (alpha * 255).toInt))
      g.drawString(text, textOffset._1, textOffset._2)

  private def fillBackgroundImage(currentAlpha: Double): Unit =
    val offset = rectOffset._3
    val w = backgroundImg.getWidth
    val h = backgroundImg.getHeight
    val color = colorFont.backgroundColor
    var y = 0
    while y < h do
      val dy = math.max(0, math.max(offset - y, y - (h - 1 - offset)))
      var x = 0
      while x < w do
        val dx = math.max(0, math.max(offset - x, x - (w - 1 - offset)))
        val dist = math.min(offset, math.hypot(dx, dy))
        val alpha = (color.getAlpha * currentAlpha * (offset - dist) / offset).toInt
        val colorToWrite = color.getRed | (color.getGreen << 8) | (color.getBlue << 16) | (alpha << 24)
        backgroundImg.setRGB(x, y, colorToWrite)
        x += 1
      y += 1

  private def computeBounds(g: Graphics2D): Unit =
    val stringBounds = font.getStringBounds(text, g.getFontRenderContext)
    val metrics = font.getLineMetrics(text, g.getFontRenderContext)

    val realX = hAlignment match
      case TextMessage.HorizontalAlignment.Left => x - stringBounds.getMinX
      case TextMessage.HorizontalAlignment.Center => x - stringBounds.getCenterX
      case TextMessage.HorizontalAlignment.Right => x - stringBounds.getMaxX
    val realY = vAlignment match
      case TextMessage.VerticalAlignment.Top => y - stringBounds.getMinY
      case TextMessage.VerticalAlignment.Center => y - stringBounds.getCenterY
      case TextMessage.VerticalAlignment.Bottom => y - stringBounds.getMaxY

    val offset = (colorFont.fontSize / 3).toInt
    rectOffset = ((realX - offset).toInt, (realY - offset - metrics.getAscent).toInt, offset)
    textOffset = (realX.toFloat, realY.toFloat)
    backgroundImg = new BufferedImage((stringBounds.getWidth + 2 * offset).toInt,
      (stringBounds.getHeight + 2 * offset).toInt, BufferedImage.TYPE_INT_ARGB)

object TextMessage:
  case class ColorFont(fontSize: Double, fontColor: Color, backgroundColor: Color = new Color(0, 0, 0, 0))

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
