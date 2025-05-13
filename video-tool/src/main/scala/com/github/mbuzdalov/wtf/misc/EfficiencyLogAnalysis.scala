package com.github.mbuzdalov.wtf.misc

import java.io.{BufferedReader, FileInputStream, FileReader, PrintWriter}

import scala.annotation.tailrec
import scala.util.Using

import com.github.mbuzdalov.wtf.{ByteStorage, LogReader, Numbers}

object EfficiencyLogAnalysis:
  private case class ForceRecord(time: Double, force: Double)

  private def parseForceRecord(line: String): ForceRecord =
    val c1 = line.indexOf(',')
    val c2 = line.lastIndexOf(',')
    ForceRecord(line.substring(0, c1).toInt * 1e-3, line.substring(c2 + 1).toDouble)

  private def parseForceCSV(filename: String): IndexedSeq[ForceRecord] =
    Using.resource(new FileReader(filename)): fr =>
      Using.resource(new BufferedReader(fr)): br =>
        br.readLine() // skip first line
        LazyList.continually(br.readLine()).takeWhile(_ ne null).map(parseForceRecord).toIndexedSeq
  end parseForceCSV

  private def parseCleanForceCSV(filename: String): IndexedSeq[ForceRecord] =
    val raw = parseForceCSV(filename)
    val firstZero = raw.indexWhere(_.force == 0)
    if firstZero < 0 then throw new IllegalArgumentException("All known valid force files have a zero region")
    val firstNonZero = raw.indexWhere(_.force != 0, firstZero)
    if firstNonZero < 0 then throw new IllegalArgumentException("All known valid force files have a nonzero region following a zero region")
    raw.drop(firstNonZero).filter(r => r.force >= -1.0 && r.force <= +1.0)

  private def process(args: Array[String]): Unit =
    val log = Using.resource(new FileInputStream(args(0)))(in => new LogReader(new ByteStorage(in)))
    val csv = parseCleanForceCSV(args(1))
    val out = new PrintWriter(args(2))

    val rcOutT = log.timingConnect("RCOU")
    val rcOutC1 = log.connect[Numbers.UInt16]("RCOU", "C1")

    val batT = log.timingConnect("BAT")
    val batCurr = log.connect[Float]("BAT", "Curr")
    val batVolt = log.connect[Float]("BAT", "Volt")

    val escT = log.timingConnect("ESC")
    val escInst = log.connect[Numbers.UInt8]("ESC", "Instance")
    val escRPM = log.connect[Float]("ESC", "RawRPM")

    // Find the endpoints. RCOU.C1 would say when outputs were not zero.
    // NB: the useful segments are always the first ones
    val thrustStart = (0 until rcOutC1.size).find(i => rcOutC1.get(i).asInt > 1000).get
    val thrustEnd = (thrustStart until rcOutC1.size).find(i => rcOutC1.get(i).asInt < 1000).get

    val thrustStartT = rcOutT.get(thrustStart)
    val thrustEndT = rcOutT.get(thrustEnd)
    println(s"CSV starts ${csv.head.time} ends ${csv.last.time}")
    println(s"Log starts $thrustStartT ends $thrustEndT")

    def forOffsetDo(offset: Int)(fun: (Double, Double) => Unit): Boolean =
      val beginT = csv(offset).time
      val csvMaxTime = beginT + thrustEndT - thrustStartT
      if csv.last.time <= csvMaxTime then false else
        var count = 0
        while csv(offset + count).time <= csvMaxTime do
          val logTime = thrustStartT + csv(offset + count).time - beginT
          fun(logTime, csv(offset + count).force)
          count += 1
        true

    def offsetQuality(offset: Int): Double =
      var sum = 0.0
      var count = 0
      if forOffsetDo(offset): (time, force) =>
        count += 1
        sum += force * rcOutC1.get(rcOutT.indexForTime(time)).asInt
      then sum / count
      else -1.0

    // Find power spent before armin
    val batteryThrustStart = batT.indexForTime(thrustStartT)
    val avgIdlePower = 0.1 * (1 to 10).map(i => batVolt.get(batteryThrustStart - i) * batCurr.get(batteryThrustStart - i)).sum
    println(s"Idle power detected: $avgIdlePower")

    @tailrec
    def lookupEscInstance(idx: Int, inst: Int): Double =
      if escInst.get(idx).asInt == inst
      then escRPM.get(idx)
      else lookupEscInstance(idx - 1, inst)

    def offsetDemo(offset: Int): Unit =
      forOffsetDo(offset): (time, force) =>
        val rcou = rcOutC1.get(rcOutT.indexForTime(time)).asInt
        val batIdx = batT.indexForTime(time)
        val volt = batVolt.get(batIdx)
        val curr = batCurr.get(batIdx)
        val escIdx = escT.indexForTime(time)
        val esc1 = lookupEscInstance(escIdx, 0)
        val esc2 = lookupEscInstance(escIdx, 1)
        // the props are 5030, 0.0762 m/rev
        val airSpeed = 0.5 * (esc1 + esc2) / 60 * 0.0762
        val outputEffectivePower = airSpeed * force * 9.8
        val electricPower = volt * curr - avgIdlePower
        val motorImbalance = (esc1 - esc2) / (esc1 + esc2)
        //println(s"RCOU: $rcou, Force (kg): $force, Volt: $volt, Curr: $curr, Electric Power: $electricPower, RPM1: $esc1, RPM2: $esc2, airspeed: $airSpeed, output power: ${airSpeed * force * 9.8}")
        out.println(s"$rcou,$force,$electricPower,$outputEffectivePower,${outputEffectivePower / electricPower},$motorImbalance")

    // Find the best offset in a very straightforward way
    val offsetQualities = Array.tabulate(csv.size)(offsetQuality)
    val bestQuality = offsetQualities.max
    val bestOffset = offsetQualities.indexOf(bestQuality)

    println(s"Best quality: $bestQuality at offset $bestOffset (CSV time ${csv(bestOffset).time}...${csv(bestOffset).time + thrustEndT - thrustStartT})")

    out.println("RCOU,Thrust,Electric power,Output power,Efficiency,Imbalance")
    offsetDemo(bestOffset)
  end process

  def main(args: Array[String]): Unit =
    val tests = for
      tp <- Seq("open", "duct")
      size <- Seq(30, 40, 50)
    yield s"$tp-${size}mm"

    for test <- tests do
      process(Array(s"${args(0)}/log-$test.bin", s"${args(0)}/log-$test.csv", s"${args(0)}/efficiency-$test.csv"))
  end main
end EfficiencyLogAnalysis
