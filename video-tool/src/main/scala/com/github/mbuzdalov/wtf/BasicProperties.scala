package com.github.mbuzdalov.wtf

import java.io.FileInputStream

import scala.util.Using

case class BasicProperties(input: String, output: String, log: String,
                           width: Int, height: Int, fps: Double, armTime: Double):
  def createLogReader: LogReader = Using.resource(new FileInputStream(log))(is => new LogReader(new ByteStorage(is)))
  def run(consumers: GraphicsConsumer*): Unit =
    val last = output match
      case "player" => new Player
      case _ => new VideoWriter(output)

    IOUtils.feedFileToConsumer(input, width, height, fps, 
      FrameConsumer.compose(FrameConsumer(GraphicsConsumer.compose(consumers*)), last))
