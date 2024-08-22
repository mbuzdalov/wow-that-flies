package com.github.mbuzdalov.wtf.widgets

import java.awt.{Color, Graphics2D}
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import com.github.mbuzdalov.wtf.GraphicsConsumer

class ProfessionallyFitImage(image: BufferedImage,
                             minXFun: Double => Double, maxXFun: Double => Double,
                             minYFun: Double => Double, maxYFun: Double => Double)
  extends GraphicsConsumer:
  private val almostBlack = new Color(0, 0, 0, 200)

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val minX = minXFun(time)
    val maxX = maxXFun(time)
    val minY = minYFun(time)
    val maxY = maxYFun(time)
    val centerX = (minX + maxX) / 2
    val centerY = (minY + maxY) / 2

    if minX < maxX && minY < maxY then
      val tr = new AffineTransform()
      val xScale = (maxX - minX) / image.getWidth
      val yScale = (maxY - minY) / image.getHeight

      // draw the stretched image
      tr.setToTranslation(minX, minY)
      tr.scale(xScale, yScale)
      g.drawImage(image, tr, null)

      // shade that
      g.setColor(almostBlack)
      g.fillRect(0, 0, img.getWidth, img.getHeight)

      // draw the actual image by fitting it in the area preserving proportions
      val scale = math.min(xScale, yScale)
      tr.setToTranslation(centerX - image.getWidth * scale / 2, centerY - image.getHeight * scale / 2)
      tr.scale(scale, scale)
      g.drawImage(image, tr, null)
  end consume
