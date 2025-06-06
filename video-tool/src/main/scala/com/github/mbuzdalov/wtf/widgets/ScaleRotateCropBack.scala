package com.github.mbuzdalov.wtf.widgets

import java.awt.Graphics2D
import java.awt.image.BufferedImage

import com.github.mbuzdalov.wtf.GraphicsConsumer

class ScaleRotateCropBack(what: Option[BufferedImage],
                          scale: Double => Double, rotate: Double => Double,
                          scaleCenterX: (Double, Double) => Double = (w, t) => w / 2, 
                          scaleCenterY: (Double, Double) => Double = (h, t) => h / 2) extends GraphicsConsumer:
  private var tempImage: BufferedImage = what.orNull
  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    if tempImage == null then tempImage = new BufferedImage(img.getWidth, img.getHeight, img.getType)
    if what.isEmpty then img.copyData(tempImage.getRaster)
    val angle = rotate(time)
    val scl = scale(time)
    val origTransform = g.getTransform
    g.rotate(angle, img.getWidth / 2.0, img.getHeight / 2.0)
    g.scale(scl, scl)
    g.translate(-(scl - 1) * scaleCenterX(img.getWidth, time), -(scl - 1) * scaleCenterY(img.getHeight, time))
    g.drawImage(tempImage, 0, 0, img.getWidth, img.getHeight, null)
    g.setTransform(origTransform)

end ScaleRotateCropBack
