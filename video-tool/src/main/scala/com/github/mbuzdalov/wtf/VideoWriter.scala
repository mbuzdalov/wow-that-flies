package com.github.mbuzdalov.wtf
import java.awt.image.BufferedImage
import java.io.FileOutputStream

class VideoWriter(target: String) extends FrameConsumer:
  private val output = if target == "-" then System.out else new FileOutputStream(target)
  private val buffer = new Array[Byte](3 * 2048)
  
  override def consume(img: BufferedImage, time: Double, frameNo: Long): Unit =
    if img == null then output.close() else
      var nWritten = 0
      var row = 0
      while row < img.getHeight do
        var col = 0
        while col < img.getWidth do
          val pixel = img.getRGB(col, row)
          buffer(nWritten) = pixel.toByte
          buffer(nWritten + 1) = (pixel >>> 8).toByte
          buffer(nWritten + 2) = (pixel >>> 16).toByte
          nWritten += 3
          if nWritten == buffer.length then 
            output.write(buffer)
            nWritten = 0
          col += 1
        row += 1  
      if nWritten > 0 then output.write(buffer, 0, nWritten)  
      output.flush()
end VideoWriter
