package com.github.mbuzdalov.wtf

import scala.language.implicitConversions

object Main:
  def main(args: Array[String]): Unit =
    val props = BasicProperties(args.tail)
    val className = s"com.github.mbuzdalov.wtf.videos.V_${args(0).replace('-', '_')}"
    val method = Class.forName(className).getDeclaredMethod("apply", classOf[BasicProperties])
    method.invoke(null, props)
  end main
end Main
