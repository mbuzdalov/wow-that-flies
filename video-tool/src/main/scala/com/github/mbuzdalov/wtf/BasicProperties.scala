package com.github.mbuzdalov.wtf

import java.io.FileInputStream

import scala.util.Using

class BasicProperties(args: Array[String]):
  private val map = args.tail.grouped(2).map(a => a(0) -> a(1)).toMap
  private lazy val input: String = map("--input")
  private lazy val output: String = map("--output")
  private lazy val log: String = map("--log")
  lazy val width: Int = map("--width").toInt
  lazy val height: Int = map("--height").toInt
  private lazy val fps: Double = map("--frame-rate").toDouble
  lazy val armTime: Double = map("--arm-time").toDouble

  def createLogReader: LogReader = Using.resource(new FileInputStream(log))(is => new LogReader(new ByteStorage(is)))
  def run(consumers: GraphicsConsumer*): Unit =
    val destination = if output == "player" then new Player else new VideoWriter(output)
    IOUtils.feedFileToConsumer(input, width, height, fps,
      FrameConsumer.compose(FrameConsumer(GraphicsConsumer.compose(consumers*)), destination))
