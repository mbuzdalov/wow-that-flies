package com.github.mbuzdalov.wtf

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.FileInputStream

import scala.util.Using

object Main:
  private val usageStr =
    """Usage:
      |  Main --input <pipe>
      |       --width <video width>
      |       --height <video height>
      |       --frame-rate <video frame rate>
      |""".stripMargin

  private def usage(extraMsg: String): Nothing =
    System.err.println(if extraMsg.nonEmpty then s"$extraMsg\n$usageStr" else usageStr)
    sys.exit(1)

  extension (map: Map[String, String])
    private def getOrPrint(key: String): String = map.getOrElse(key, usage(s"No $key specified"))

  private def flush(pipe: String, width: Int, height: Int, fps: Double, target: FrameConsumer): Unit =
    val buf = new Array[Byte](32768)
    val img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

    Using.resource(if pipe == "-" then System.in else new FileInputStream(pipe)): ins =>
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
  end flush

  private def getAutoArmedTime(reader: LogReader): Double =
    val eventValues = reader.connect[Numbers.UInt8]("EV", "Id")
    val eventTimes = reader.connect[Numbers.UInt64]("EV", "TimeUS")

    val autoArmedEventIndex = (0 until eventValues.size).find(i => eventValues.get(i).toInt == 15).get
    eventTimes.get(autoArmedEventIndex).toDouble * 1e-6

  private def video_2024_03_28(args: Array[String]): Unit =
    val map = args.grouped(2).map(a => a(0) -> a(1)).toMap
    val pipe = map.getOrPrint("--input")
    val width = map.getOrPrint("--width").toInt
    val height = map.getOrPrint("--height").toInt
    val fps = map.getOrPrint("--frame-rate").toDouble
    val armTime = map.getOrPrint("--arm-time").toDouble
    val output = map.getOrPrint("--output")

    val log = map.getOrPrint("--log")
    val reader = Using.resource(new FileInputStream(log))(is => new LogReader(new ByteStorage(is)))

    val autoArmedEventTime = getAutoArmedTime(reader)
    val logTimeOffset = autoArmedEventTime - armTime

    System.err.println(s"Auto-armed event found at time $autoArmedEventTime, offset = $logTimeOffset")

    val hWidth = width / 2
    val stickSize = 101 * width / 1280
    val stickGap = 11 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      hWidth - stickSize - stickGap, hWidth + stickGap, stickGap)

    val rpWidth = width / 4
    val rpHeight = height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rollPlot = new Plot(reader, logTimeOffset, rpGap, rpGap, 
      rpWidth, rpHeight, fontSize, 2, -15, +15, "ATT",
      IndexedSeq("Roll" -> Color.RED, "DesRoll" -> Color.BLUE))
    val pitchPlot = new Plot(reader, logTimeOffset, width - rpWidth - rpGap, rpGap, 
      rpWidth, rpHeight, fontSize, 2, -15, +15, "ATT",
      IndexedSeq("Pitch" -> Color.RED, "DesPitch" -> Color.BLUE))

    val last = output match
      case "player" => new Player
      case _ => new VideoWriter(output)
    flush(pipe, width, height, fps, FrameConsumer.compose(sticks, rollPlot, pitchPlot, last))
  end video_2024_03_28

  def main(args: Array[String]): Unit =
    args(0) match
      case "2024-03-28" => video_2024_03_28(args.tail)
  end main
end Main
