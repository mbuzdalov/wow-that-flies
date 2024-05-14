package com.github.mbuzdalov.wtf

import java.awt.image.BufferedImage
import java.io.FileInputStream

import scala.util.Using

object IOUtils:
  def feedFileToConsumer(filename: String, width: Int, height: Int, fps: Double, target: FrameConsumer): Unit =
    val buf = new Array[Byte](32768)
    val img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

    Using.resource(if filename == "-" then System.in else new FileInputStream(filename)): ins =>
      var frameNo = -1L
      var x, y, pixI = 0
      var bufSize = 0
      var pixel = 0
      while
        bufSize = ins.read(buf)
        bufSize > 0
      do
        var i = 0
        while i < bufSize do
          pixel |= (buf(i) & 0xff) << (8 * pixI)
          i += 1
          pixI += 1
          if pixI == 3 then
            pixI = 0
            img.setRGB(x, y, pixel)
            pixel = 0
            x += 1
            if x == width then
              x = 0
              y += 1
              if y == height then
                y = 0
                frameNo += 1
                target.consume(img, frameNo / fps, frameNo)
      end while
      target.close()
  end feedFileToConsumer
end IOUtils
