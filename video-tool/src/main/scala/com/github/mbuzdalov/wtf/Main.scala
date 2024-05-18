package com.github.mbuzdalov.wtf

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.videos.*

object Main:
  def main(args: Array[String]): Unit =
    val props = BasicProperties(args.tail)

    args(0) match
      case "2024-03-28" => V_2024_03_28(props)
      case "2024-03-29-p1" => V_2024_03_29_p1(props)
      case "2024-03-29-p2" => V_2024_03_29_p2(props)
      case "2024-03-29-p3" => V_2024_03_29_p3(props)
      case "2024-04-08-p1" => V_2024_04_08_p1(props)
      case "2024-04-08-p2" => V_2024_04_08_p2(props)
      case "2024-04-08-p3" => V_2024_04_08_p3(props)
      case "2024-04-08-p4" => V_2024_04_08_p4(props)
      case "2024-04-09-p1" => V_2024_04_09_p1(props)
      case "2024-04-09-p2" => V_2024_04_09_p2(props)
  end main
end Main
