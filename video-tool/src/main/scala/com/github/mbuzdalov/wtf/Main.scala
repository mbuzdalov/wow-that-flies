package com.github.mbuzdalov.wtf

import java.awt.Color
import java.io.FileInputStream

import scala.language.implicitConversions
import scala.util.Using

import com.github.mbuzdalov.wtf.TextMessage.*
import com.github.mbuzdalov.wtf.widgets.RollPitchPlots

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

  private def getAutoArmedTime(reader: LogReader): Double =
    val eventValues = reader.connect[Numbers.UInt8]("EV", "Id")
    val eventTimes = reader.connect[Numbers.UInt64]("EV", "TimeUS")

    val autoArmedEventIndex = (0 until eventValues.size).find(i => eventValues.get(i).toInt == 15).get
    eventTimes.get(autoArmedEventIndex).toDouble * 1e-6

  private def video_2024_03_28(args: Array[String]): Unit =
    val map = args.grouped(2).map(a => a(0) -> a(1)).toMap
    val input = map.getOrPrint("--input")
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
    val stickSize = 101 * width / 1280
    val stickGap = 11 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      hWidth - stickSize - stickGap, hWidth + stickGap, stickGap)

    val rpWidth = width / 4
    val rpHeight = height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = RollPitchPlots.create(reader, logTimeOffset, "Roll", 3, 15,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = RollPitchPlots.create(reader, logTimeOffset, "Pitch", 4, 15,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

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

    IOUtils.feedFileToConsumer(input, width, height, fps, FrameConsumer.compose(FrameConsumer(allGraphics), last))
  end video_2024_03_28

  private def video_2024_03_29_p1(args: Array[String]): Unit =
    val map = args.grouped(2).map(a => a(0) -> a(1)).toMap
    val input = map.getOrPrint("--input")
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

    val rpWidth = width / 4
    val rpHeight = height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = RollPitchPlots.create(reader, logTimeOffset, "Roll", 3, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = RollPitchPlots.create(reader, logTimeOffset, "Pitch", 4, 20,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

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
      new Fade(timeOn = 74.3, timeOff = 74.8),
    )

    val last = output match
      case "player" => new Player
      case _ => new VideoWriter(output)

    IOUtils.feedFileToConsumer(input, width, height, fps, FrameConsumer.compose(FrameConsumer(allGraphics), last))
  end video_2024_03_29_p1

  private def video_2024_03_29_p2(args: Array[String]): Unit =
    val map = args.grouped(2).map(a => a(0) -> a(1)).toMap
    val input = map.getOrPrint("--input")
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

    val rpWidth = width / 4
    val rpHeight = height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = RollPitchPlots.create(reader, logTimeOffset, "Roll", 3, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = RollPitchPlots.create(reader, logTimeOffset, "Pitch", 4, 20,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(10, 10, 50)
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("March 29, 2024, test 2 (actually 4)",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 1, 10),
      TextMessage("To reduce maximum angle accelerations, I changed",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 1.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 3, 10),
      TextMessage("ATC_ACCEL_{R,P}_MAX from 220000 to 40000.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 2.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 3, 10),
      TextMessage("The previous values were recommended defaults for a 5-inch quadcopter.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 4.0f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 5, 10),
      TextMessage("40000 = 400 degrees / s^2 were chosen by meditating on the logs.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 5.0f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 5, 10),
      TextMessage("Still, noticeable overshoots happen in pitch and roll",
        msgFontSize, msgColor, width * 0.5f, height * 0.6f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 15, 30),
      TextMessage("with flaps forced to move to their maximum angles...",
        msgFontSize, msgColor, width * 0.5f, height * 0.6f + msgStep * 1.0f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 15, 30),
      new Fade(timeOn = 0.5, timeOff = 0),
      new Fade(timeOn = 61.7, timeOff = 62.2),
    )

    val last = output match
      case "player" => new Player
      case _ => new VideoWriter(output)

    IOUtils.feedFileToConsumer(input, width, height, fps, FrameConsumer.compose(FrameConsumer(allGraphics), last))
  end video_2024_03_29_p2

  private def video_2024_03_29_p3(args: Array[String]): Unit =
    val map = args.grouped(2).map(a => a(0) -> a(1)).toMap
    val input = map.getOrPrint("--input")
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

    val rpWidth = width / 4
    val rpHeight = height / 4
    val rpGap = 11 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val rollPlot = RollPitchPlots.create(reader, logTimeOffset, "Roll", 3, 20,
      rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)
    val pitchPlot = RollPitchPlots.create(reader, logTimeOffset, "Pitch", 4, 20,
      width - rpWidth - rpGap, rpGap, rpWidth, rpHeight, fontSize, rpBackground, 2)

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgColor = new Color(10, 10, 50)
    val allGraphics = GraphicsConsumer.compose(sticks, rollPlot, pitchPlot,
      TextMessage("March 29, 2024, test 3 (actually 5)",
        msgFontSize, msgColor, width * 0.5f, height * 0.11f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 1, 10),
      TextMessage("Maybe the reduction was not enough?",
        msgFontSize, msgColor, width * 0.5f, height * 0.11f + msgStep * 1.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 3, 10),
      TextMessage("Now ATC_ACCEL_{R,P}_MAX changed from 40000 further down to 30000.",
        msgFontSize, msgColor, width * 0.5f, height * 0.11f + msgStep * 2.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 3, 10),
      TextMessage("Also, plots of D-terms showed some high-freq vibrations.",
        msgFontSize, msgColor, width * 0.5f, height * 0.11f + msgStep * 4.0f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 5, 10),
      TextMessage("To counteract that, D-terms changed to be more aggressive:",
        msgFontSize, msgColor, width * 0.5f, height * 0.11f + msgStep * 5.0f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 5, 10),
      TextMessage("ATC_RAT_{PIT,RLL}_FLTD from 40 to 20.",
        msgFontSize, msgColor, width * 0.5f, height * 0.11f + msgStep * 6.0f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 5, 10),
      TextMessage("Sorry, but the shooting angle is really bad...",
        msgFontSize, msgColor, width * 0.5f, height * 0.5f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 19, 24),
      TextMessage("Well, overshoots are still there.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 81, 87),
      TextMessage("But they appear to be less severe now, and don't turn to oscillations.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 1.0f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 83, 87),
      TextMessage("Probably CX7 is now able to survive an autotune session.",
        msgFontSize, msgColor, width * 0.5f, height * 0.25f + msgStep * 2.0f,
        HorizontalAlignment.Center, VerticalAlignment.Center, 83, 87),
      new Fade(timeOn = 0.5, timeOff = 0),
    )

    val last = output match
      case "player" => new Player
      case _ => new VideoWriter(output)

    IOUtils.feedFileToConsumer(input, width, height, fps, FrameConsumer.compose(FrameConsumer(allGraphics), last))
  end video_2024_03_29_p3

  def main(args: Array[String]): Unit =
    args(0) match
      case "2024-03-28" => video_2024_03_28(args.tail)
      case "2024-03-29-p1" => video_2024_03_29_p1(args.tail)
      case "2024-03-29-p2" => video_2024_03_29_p2(args.tail)
      case "2024-03-29-p3" => video_2024_03_29_p3(args.tail)
  end main
end Main
