package com.github.mbuzdalov.wtf.widgets

import java.awt.{Color, Graphics2D}
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage

import com.github.mbuzdalov.wtf.GraphicsConsumer

class ProfessionallyFitImage(imageProvider: ProfessionallyFitImage.ImageProvider,
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
    val image = imageProvider.image(img)
    
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

object ProfessionallyFitImage:
  trait ImageProvider:
    def image(videoImage: BufferedImage): BufferedImage

  class ConstantProvider(img: BufferedImage) extends ImageProvider:
    override def image(videoImage: BufferedImage): BufferedImage = img

  class ClippingProvider(x: Int, y: Int, w: Int, h: Int) extends ImageProvider:
    private val img = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR)
    override def image(videoImage: BufferedImage): BufferedImage =
      var yy = 0
      while yy < h do
        var xx = 0
        while xx < w do
          img.setRGB(xx, yy, videoImage.getRGB(xx + x, yy + y))
          xx += 1
        yy += 1  
      img
      