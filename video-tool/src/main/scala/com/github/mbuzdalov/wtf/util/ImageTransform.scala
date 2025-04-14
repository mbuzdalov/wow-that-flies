package com.github.mbuzdalov.wtf.util

import java.awt.image.BufferedImage

trait ImageTransform:
  def apply(img: BufferedImage): BufferedImage

object ImageTransform:
  val clockwise: ImageTransform = img =>
    val rv = new BufferedImage(img.getHeight, img.getWidth, img.getType)
    for
      y <- 0 until rv.getHeight
      x <- 0 until rv.getWidth
    do
      rv.setRGB(x, y, img.getRGB(y, img.getHeight - 1 - x))
    rv

  val counterClockwise: ImageTransform = img =>
    val rv = new BufferedImage(img.getHeight, img.getWidth, img.getType)
    for
      y <- 0 until rv.getHeight
      x <- 0 until rv.getWidth
    do
      rv.setRGB(x, y, img.getRGB(img.getWidth - 1 - y, x))
    rv

  val upsideDown: ImageTransform = img =>
    val rv = new BufferedImage(img.getWidth, img.getHeight, img.getType)
    for
      y <- 0 until rv.getHeight
      x <- 0 until rv.getWidth
    do
      rv.setRGB(x, y, img.getRGB(img.getWidth - 1 - x, img.getHeight - 1 - y))
    rv
