package com.github.mbuzdalov.wtf

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.videos.*

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

  def main(args: Array[String]): Unit =
    val map = args.tail.grouped(2).map(a => a(0) -> a(1)).toMap
    val props = BasicProperties(
      input = map.getOrPrint("--input"),
      output = map.getOrPrint("--output"),
      log = map.getOrPrint("--log"),
      width = map.getOrPrint("--width").toInt,
      height = map.getOrPrint("--height").toInt,
      fps = map.getOrPrint("--frame-rate").toDouble,
      armTime = map.getOrPrint("--arm-time").toDouble,
    )

    args(0) match
      case "2024-03-28" => V_2024_03_28(props)
      case "2024-03-29-p1" => V_2024_03_29_p1(props)
      case "2024-03-29-p2" => V_2024_03_29_p2(props)
      case "2024-03-29-p3" => V_2024_03_29_p3(props)
  end main
end Main
