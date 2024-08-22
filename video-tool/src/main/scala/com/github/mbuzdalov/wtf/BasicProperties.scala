package com.github.mbuzdalov.wtf

import java.awt.image.BufferedImage
import java.io.{File, FileInputStream}
import javax.imageio.ImageIO

import scala.util.Using

class BasicProperties(args: Array[String]):
  private val map = args.grouped(2).map(a => a(0) -> a(1)).toMap
  private lazy val input: String = map("--input")
  private lazy val output: String = map("--output")
  lazy val width: Int = map("--width").toInt
  lazy val height: Int = map("--height").toInt
  private lazy val fps: Double = map("--frame-rate").toDouble
  lazy val armTime: Double = map("--arm-time").toDouble

  def property(name: String): String = map(name)
  
  def createLogReader(propName: String = "log"): LogReader = 
    Using.resource(new FileInputStream(map(s"--$propName")))(is => new LogReader(new ByteStorage(is)))
  
  def runVideo(consumer: GraphicsConsumer): Unit =
    val destination = if output == "player" then new Player else new VideoWriter(output)
    val target = FrameConsumer.compose(FrameConsumer(consumer), destination)
    IOUtils.feedFileToConsumer(input, width, height, fps, target)

  private def runWithBaseImage(baseImage: BufferedImage, time: Double, consumer: GraphicsConsumer): Unit =
    val destination = if output == "player" then new Player else new VideoWriter(output)
    val target = FrameConsumer.compose(FrameConsumer(consumer), destination)
    val currImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
    val maxFrameNo = (time * fps).toInt
    for frameNo <- 0 until maxFrameNo do
      val g = currImage.createGraphics()
      g.drawImage(baseImage, 0, 0, width, height, null)
      target.consume(currImage, frameNo / fps, frameNo)
    target.close()

  def runImage(time: Double, consumer: GraphicsConsumer): Unit =
    runWithBaseImage(ImageIO.read(new File(input)), time, consumer)

  def runEmpty(time: Double, consumer: GraphicsConsumer): Unit =
    runWithBaseImage(new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR), time, consumer)
