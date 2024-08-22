package com.github.mbuzdalov.wtf.widgets

import java.awt.image.BufferedImage
import java.awt.{Color, Font, Graphics2D}

import scala.compiletime.uninitialized

import com.github.mbuzdalov.wtf.GraphicsConsumer

class TextMessage(val text: String, colorFont: TextMessage.ColorFont,
                  xFun: Double => Double, yFun: Double => Double,
                  hAlignment: TextMessage.HorizontalAlignment,
                  vAlignment: TextMessage.VerticalAlignment) extends GraphicsConsumer:
  def this(text: String, colorFont: TextMessage.ColorFont, x: Double, y: Double,
           hAlignment: TextMessage.HorizontalAlignment, vAlignment: TextMessage.VerticalAlignment) =
    this(text, colorFont, _ => x, _ => y, hAlignment, vAlignment)

  private val font = new Font(Font.SANS_SERIF, Font.PLAIN, (colorFont.fontSize + 0.5).toInt)
  private var rectOffset: (Double, Double, Double) = uninitialized
  private var textOffset: (Double, Double) = uninitialized
  private var backgroundImg: BufferedImage = uninitialized

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    g.setFont(font)
    val fontColor = colorFont.fontColor
    val backColor = colorFont.backgroundColor
    if textOffset == null then 
      computeBounds(g)
      fillBackgroundImage()
    val x = xFun(time)
    val y = yFun(time)
    g.drawImage(backgroundImg, (x - rectOffset._1).toInt, (y - rectOffset._2).toInt, null)
    g.setColor(new Color(fontColor.getRed, fontColor.getGreen, fontColor.getBlue))
    g.drawString(text, (x - textOffset._1).toFloat, (y - textOffset._2).toFloat)

  private def fillBackgroundImage(): Unit =
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
        val alpha = (color.getAlpha * (offset - dist) / offset).toInt
        val colorToWrite = color.getRed | (color.getGreen << 8) | (color.getBlue << 16) | (alpha << 24)
        backgroundImg.setRGB(x, y, colorToWrite)
        x += 1
      y += 1

  private def computeBounds(g: Graphics2D): Unit =
    val stringBounds = font.getStringBounds(text, g.getFontRenderContext)
    val metrics = font.getLineMetrics(text, g.getFontRenderContext)

    val realX = hAlignment match
      case TextMessage.HorizontalAlignment.Left => stringBounds.getMinX
      case TextMessage.HorizontalAlignment.Center => stringBounds.getCenterX
      case TextMessage.HorizontalAlignment.Right => stringBounds.getMaxX
    val realY = vAlignment match
      case TextMessage.VerticalAlignment.Top => stringBounds.getMinY
      case TextMessage.VerticalAlignment.Center => stringBounds.getCenterY
      case TextMessage.VerticalAlignment.Bottom => stringBounds.getMaxY

    val offset = colorFont.fontSize / 3
    rectOffset = (offset, offset + metrics.getAscent, offset)
    textOffset = (realX, realY)
    backgroundImg = new BufferedImage((stringBounds.getWidth + 2 * offset).toInt,
      (stringBounds.getHeight + 2 * offset).toInt, BufferedImage.TYPE_INT_ARGB)

object TextMessage:
  case class ColorFont(fontSize: Double, fontColor: Color, backgroundColor: Color = new Color(0, 0, 0, 0))

  enum VerticalAlignment:
    case Top, Center, Bottom

  enum HorizontalAlignment:
    case Left, Center, Right
