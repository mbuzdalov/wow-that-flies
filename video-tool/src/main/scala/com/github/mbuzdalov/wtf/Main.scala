package com.github.mbuzdalov.wtf

import java.awt.image.BufferedImage
import java.net.Socket

import scala.util.Using

object Main:
  private val usageStr =
    """Usage:
      |  Main --input-port <port>
      |       --width <video width>
      |       --height <video height>
      |       --frame-rate <video frame rate>
      |""".stripMargin

  private def usage(extraMsg: String): Nothing =
    System.err.println(if extraMsg.nonEmpty then s"$extraMsg\n$usageStr" else usageStr)
    sys.exit(1)

  extension (map: Map[String, String])
    private def getOrPrint(key: String): String = map.getOrElse(key, usage(s"No $key specified"))

  private def flush(port: Int, width: Int, height: Int, fps: Double, target: FrameConsumer): Unit =
    val buf = new Array[Byte](width * height * 3)
    val img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

    Using.resource(new Socket("localhost", port)): socket =>
      Using.resource(socket.getInputStream): ins =>
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
                  println(s"submitting frame $frameNo")
                  target.consume(img, frameNo / fps, frameNo)
        end while
  end flush

  def main(args: Array[String]): Unit =
    val map = args.grouped(2).map(a => a(0) -> a(1)).toMap
    val port = map.getOrPrint("--input").toInt
    val width = map.getOrPrint("--width").toInt
    val height = map.getOrPrint("--height").toInt
    val fps = map.getOrPrint("--frame-rate").toDouble

    val fc: FrameConsumer = new Player
    flush(port, width, height, fps, fc)
  end main
end Main
