package com.github.mbuzdalov.wtf

import java.awt.image.BufferedImage
import java.io.{File, FileInputStream}
import javax.imageio.ImageIO

import scala.util.Using

class BasicProperties(args: Array[String]):
  private val map = args.grouped(2).map(a => a(0) -> a(1)).toMap
  private lazy val input: String = map("--input")
  private lazy val output: String = map("--output")
  private lazy val log: String = map("--log")
  lazy val width: Int = map("--width").toInt
  lazy val height: Int = map("--height").toInt
  private lazy val fps: Double = map("--frame-rate").toDouble
  lazy val armTime: Double = map("--arm-time").toDouble

  def createLogReader: LogReader = Using.resource(new FileInputStream(log))(is => new LogReader(new ByteStorage(is)))
  def runVideo(consumers: GraphicsConsumer*): Unit =
    val destination = if output == "player" then new Player else new VideoWriter(output)
    val target = FrameConsumer.compose(FrameConsumer(GraphicsConsumer.compose(consumers*)), destination)
    IOUtils.feedFileToConsumer(input, width, height, fps, target)

  private def runWithBaseImage(baseImage: BufferedImage, time: Double, consumers: GraphicsConsumer*): Unit =
    val destination = if output == "player" then new Player else new VideoWriter(output)
    val target = FrameConsumer.compose(FrameConsumer(GraphicsConsumer.compose(consumers *)), destination)
    val currImage = new BufferedImage(baseImage.getWidth, baseImage.getHeight, BufferedImage.TYPE_3BYTE_BGR)
    val maxFrameNo = (time / fps).toInt
    for frameNo <- 0 until maxFrameNo do
      baseImage.copyData(currImage.getRaster)
      target.consume(baseImage, frameNo / fps, frameNo)

  def runImage(time: Double, consumers: GraphicsConsumer*): Unit =
    runWithBaseImage(ImageIO.read(new File(input)), time, consumers*)

  def runEmpty(time: Double, consumers: GraphicsConsumer*): Unit =
    runWithBaseImage(new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR), time, consumers*)
