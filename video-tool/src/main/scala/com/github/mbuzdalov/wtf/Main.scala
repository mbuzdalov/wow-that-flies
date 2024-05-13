package com.github.mbuzdalov.wtf

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.FileInputStream

import scala.util.Using

import com.github.mbuzdalov.wtf.TextMessage.*

object Main:
  private val usageStr =
    """Usage:
      |  Main <video-id>
      |       --input <filename>
      |       --width <video width>
      |       --height <video height>
      |       --frame-rate <video frame rate>
      |       --arm-time <arm time in video>
      |       --log <ArduPilot binary log>
      |       --output <filename | player>
      |  <video-id> is the internal video id to choose the particular script to run
      |  <filename> can be - for standard input or output
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

    val rollFlapMin = reader.getParameter("SERVO3_MIN")
    val rollFlapMax = reader.getParameter("SERVO3_MAX")
    val pitchFlapMin = reader.getParameter("SERVO4_MIN")
    val pitchFlapMax = reader.getParameter("SERVO4_MAX")

    val rpWidth = width / 4
    val rpHeight = height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = new Plot(reader, logTimeOffset, rpGap, rpGap,
      rpWidth, rpHeight, fontSize, rpBackground, 2,
      IndexedSeq(
        Plot.Source[Numbers.UInt16]("RCOU", "C3", "Roll Flap", v => v.toDouble, rollFlapMin, rollFlapMax, Color.BLACK),
        Plot.Source[Float]("ATT", "DesRoll", "Desired Roll", v => v, -15, +15, Color.BLUE),
        Plot.Source[Float]("ATT", "Roll", "Actual Roll", v => v, -15, +15, Color.RED),
      ))
    val pitchPlot = new Plot(reader, logTimeOffset, width - rpWidth - rpGap, rpGap,
      rpWidth, rpHeight, fontSize, rpBackground, 2,
      IndexedSeq(
        Plot.Source[Numbers.UInt16]("RCOU", "C4", "Pitch Flap", v => v.toDouble, pitchFlapMin, pitchFlapMax, Color.BLACK),
        Plot.Source[Float]("ATT", "DesPitch", "Desired Pitch", v => v, -15, +15, Color.BLUE),
        Plot.Source[Float]("ATT", "Pitch", "Actual Pitch", v => v, -15, +15, Color.RED),
      ))

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(10, 10, 50)
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("March 28, 2024",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f,
        TextMessage.HorizontalAlignment.Center, TextMessage.VerticalAlignment.Center, 1, 10),
      TextMessage("To aid investigations, detailed logging was enabled:",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 1f,
        TextMessage.HorizontalAlignment.Center, TextMessage.VerticalAlignment.Center, 3, 10),
      TextMessage("INS_RAW_LOG_OPT = 9",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 2.2f,
        TextMessage.HorizontalAlignment.Center, TextMessage.VerticalAlignment.Center, 3, 10),
      TextMessage("LOG_BITMASK = 180223",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 3f,
        TextMessage.HorizontalAlignment.Center, TextMessage.VerticalAlignment.Center, 3, 10),
      TextMessage("This is a test run.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 4.5f,
        TextMessage.HorizontalAlignment.Center, TextMessage.VerticalAlignment.Center, 3, 10),

      TextMessage("Well, CX7 did not crash this time,",
        msgFontSize, msgColor, width * 0.5f, height * 0.33f,
        TextMessage.HorizontalAlignment.Center, TextMessage.VerticalAlignment.Center, 101, 110),
      TextMessage("but the discrepancy between desired and actual roll and pitch,",
        msgFontSize, msgColor, width * 0.5f, height * 0.33f + msgStep * 1f,
        TextMessage.HorizontalAlignment.Center, TextMessage.VerticalAlignment.Center, 101, 110),
      TextMessage("is quite large even in a gentle flight.",
        msgFontSize, msgColor, width * 0.5f, height * 0.33f + msgStep * 2f,
        TextMessage.HorizontalAlignment.Center, TextMessage.VerticalAlignment.Center, 101, 110),

      TextMessage("Stay tuned! (Pun intended)",
        msgFontSize, msgColor, width * 0.5f, height * 0.33f + msgStep * 3.5f,
        TextMessage.HorizontalAlignment.Center, TextMessage.VerticalAlignment.Center, 107, 110),
    )

    val last = output match
      case "player" => new Player
      case _ => new VideoWriter(output)

    flush(pipe, width, height, fps, FrameConsumer.compose(FrameConsumer(allGraphics), last))
  end video_2024_03_28

  private def video_2024_03_29_p1(args: Array[String]): Unit =
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

    val hWidth = width / 2
    val stickSize = 71 * width / 1280
    val stickGap = 7 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      width - 2 * stickSize - 2 * stickGap, width - stickSize - stickGap, height - stickSize - stickGap)

    val rollFlapMin = reader.getParameter("SERVO3_MIN")
    val rollFlapMax = reader.getParameter("SERVO3_MAX")
    val pitchFlapMin = reader.getParameter("SERVO4_MIN")
    val pitchFlapMax = reader.getParameter("SERVO4_MAX")

    val rpWidth = width / 4
    val rpHeight = height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = new Plot(reader, logTimeOffset, rpGap, rpGap,
      rpWidth, rpHeight, fontSize, rpBackground, 2,
      IndexedSeq(
        Plot.Source[Numbers.UInt16]("RCOU", "C3", "Roll Flap", v => v.toDouble, rollFlapMin, rollFlapMax, Color.BLACK),
        Plot.Source[Float]("ATT", "DesRoll", "Desired Roll", v => v, -20, +20, Color.BLUE),
        Plot.Source[Float]("ATT", "Roll", "Actual Roll", v => v, -20, +20, Color.RED),
      ))
    val pitchPlot = new Plot(reader, logTimeOffset, width - rpWidth - rpGap, rpGap,
      rpWidth, rpHeight, fontSize, rpBackground, 2,
      IndexedSeq(
        Plot.Source[Numbers.UInt16]("RCOU", "C4", "Pitch Flap", v => v.toDouble, pitchFlapMin, pitchFlapMax, Color.BLACK),
        Plot.Source[Float]("ATT", "DesPitch", "Desired Pitch", v => v, -20, +20, Color.BLUE),
        Plot.Source[Float]("ATT", "Pitch", "Actual Pitch", v => v, -20, +20, Color.RED),
      ))

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(10, 10, 50)
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("March 29, 2024, test 1 (actually 3)",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 1, 10),
      TextMessage("I am still hunting overshoots via tuning adjustment.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 1.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 3, 10),
      TextMessage("Since last test I have configured harmonic notch",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 2.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 3, 10),
      TextMessage("with ESC telemetry as a source, and did two tests behind the scene.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 3.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 3, 10),
      TextMessage("For this test, I increased pitch and roll D-terms from 0.002 to 0.01",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 5, 10),
      TextMessage("in the hope that CX7 will react quicker and avoid overshoots.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 6f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 5, 10),
      TextMessage("I am performing increasingly hard twitches on pitch and roll",
        msgFontSize, msgColor, width * 0.5f, height * 0.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 14, 18),
      TextMessage("and observing the behavior of the copter",
        msgFontSize, msgColor, width * 0.5f, height * 0.5f + msgStep * 1f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 14, 18),
      TextMessage("Watch for an example of overshoot on the pitch axis...",
        msgFontSize, msgColor, width * 0.5f, height * 0.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 22, 27),
      TextMessage("NOW!",
        msgFontSize, msgColor, width * 0.5f, height * 0.5f + msgStep * 1f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 25.3 withSpeed 100, 27),
      TextMessage("A strong one will happen",
        msgFontSize, msgColor, width * 0.5f, height * 0.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 35, 41),
      TextMessage("in 3",
        msgFontSize, msgColor, width * 0.5f - msgFontSize * 2.5f, height * 0.5f + msgStep * 1f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 36 withSpeed 100, 41),
      TextMessage("2",
        msgFontSize, msgColor, width * 0.5f - msgFontSize * 1f, height * 0.5f + msgStep * 1f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 37 withSpeed 100, 41),
      TextMessage("1",
        msgFontSize, msgColor, width * 0.5f - msgFontSize * 0f, height * 0.5f + msgStep * 1f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 38 withSpeed 100, 41),
      TextMessage("NOW!",
        msgFontSize, msgColor, width * 0.5f + msgFontSize * 2f, height * 0.5f + msgStep * 1f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 39 withSpeed 100, 41),
      TextMessage("Yet another big one",
        msgFontSize, msgColor, width * 0.5f, height * 0.2f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 56, 63),
      TextMessage("in 3",
        msgFontSize, msgColor, width * 0.5f - msgFontSize * 2.5f, height * 0.2f + msgStep * 1f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 57 withSpeed 100, 63),
      TextMessage("2",
        msgFontSize, msgColor, width * 0.5f - msgFontSize * 1f, height * 0.2f + msgStep * 1f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 58 withSpeed 100, 63),
      TextMessage("1",
        msgFontSize, msgColor, width * 0.5f - msgFontSize * 0f, height * 0.2f + msgStep * 1f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 59 withSpeed 100, 63),
      TextMessage("NOW!",
        msgFontSize, msgColor, width * 0.5f + msgFontSize * 2f, height * 0.2f + msgStep * 1f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 60 withSpeed 100, 63),
      TextMessage("Increasing D-term cannot make it alone.",
        msgFontSize, msgColor, width * 0.5f, height * 0.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 67, 74),
      TextMessage("When returning to level, required angle rates change too quickly.",
        msgFontSize, msgColor, width * 0.5f, height * 0.5f + msgStep * 1.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 68.5, 74),
      TextMessage("Maybe the maximum angular accelerations need to be reduced?",
        msgFontSize, msgColor, width * 0.5f, height * 0.5f + msgStep * 2.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 68.5, 74),
      new Fade(74.3, 74.8),
    )

    val last = output match
      case "player" => new Player
      case _ => new VideoWriter(output)

    flush(pipe, width, height, fps, FrameConsumer.compose(FrameConsumer(allGraphics), last))
  end video_2024_03_29_p1

  def main(args: Array[String]): Unit =
    args(0) match
      case "2024-03-28" => video_2024_03_28(args.tail)
      case "2024-03-29-p1" => video_2024_03_29_p1(args.tail)
  end main
end Main
