package com.github.mbuzdalov.wtf.widgets

import java.awt.Graphics2D
import java.awt.image.BufferedImage

import com.github.mbuzdalov.wtf.widgets.FlightMode.copterFlightModes
import com.github.mbuzdalov.wtf.{GraphicsConsumer, LogReader, Numbers}

class FlightMode(logReader: LogReader, timeOffset: Double,
                 colorFont: TextMessage.ColorFont,
                 xFun: Double => Double, yFun: Double => Double,
                 hAlignment: TextMessage.HorizontalAlignment,
                 vAlignment: TextMessage.VerticalAlignment) extends GraphicsConsumer:
  private val timing = logReader.timingConnect("MODE")
  private val mode = logReader.connect[Numbers.UInt8]("MODE", "ModeNum")
  private val allFlightModeTexts = copterFlightModes map { mode =>
    TextMessage(s"Mode: ${mode.fullName}", colorFont, xFun, yFun, hAlignment, vAlignment)
  }

  def this(logReader: LogReader, timeOffset: Double,
           colorFont: TextMessage.ColorFont, x: Double, y: Double,
           hAlignment: TextMessage.HorizontalAlignment, vAlignment: TextMessage.VerticalAlignment) =
    this(logReader, timeOffset, colorFont, _ => x, _ => y, hAlignment, vAlignment)

  override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
    val index = timing.indexForTime(time + timeOffset)
    if index >= 0 then
      val currMode = mode.get(index).asInt
      if currMode >= 0 && currMode < allFlightModeTexts.length then
        allFlightModeTexts(currMode).consume(img, g, time, frameNo)

object FlightMode:
  private case class FlightMode(number: Int, fourLetter: String, fullName: String)
  private val copterFlightModes = IArray(
    FlightMode(0, "STAB", "Stabilize"),
    FlightMode(1, "ACRO", "Acro"),
    FlightMode(2, "ALTH", "AltHold"),
    FlightMode(3, "AUTO", "Auto"),
    FlightMode(4, "GUID", "Guided"),
    FlightMode(5, "LOIT", "Loiter"),
    FlightMode(6, "RTL", "RTL"),
    FlightMode(7, "CIRC", "Circle"),
    FlightMode(-1, "", ""),
    FlightMode(9, "LAND", "Land"),
    FlightMode(-1, "", ""),
    FlightMode(11, "DRFT", "Drift"),
    FlightMode(-1, "", ""),
    FlightMode(13, "SPRT", "Sport"),
    FlightMode(14, "FLIP", "Flip"),
    FlightMode(15, "ATUN", "AutoTune"),
    FlightMode(16, "PHLD", "PosHold"),
    FlightMode(17, "BRAK", "Brake"),
    FlightMode(18, "THRO", "Throw"),
    FlightMode(19, "AADS", "Avoid ADSB"),
    FlightMode(20, "GUIN", "Guided No GPS"),
    FlightMode(21, "SRTL", "Smart RTL"),
    FlightMode(22, "FHLD", "Flow Hold"),
    FlightMode(23, "FOLL", "Follow"),
    FlightMode(24, "ZIGZ", "ZigZag"),
    FlightMode(25, "SID", "SystemID"),
    FlightMode(26, "AROT", "Heli Autorotate"),
    FlightMode(27, "ARTL", "Auto RTL"),
    FlightMode(28, "TURT", "Turtle"),
  )
