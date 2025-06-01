package com.github.mbuzdalov.wtf.misc

import java.io.{BufferedReader, FileInputStream, FileReader, PrintWriter}
import java.nio.file.{Files, Paths}

import scala.annotation.tailrec
import scala.util.Using

import com.github.mbuzdalov.wtf.{ByteStorage, LogReader, Numbers}

object EfficiencyLogAnalysis:
  private case class ForceRecord(time: Double, f1: Double, f2: Double, force: Double)

  private def parseForceRecord(line: String): ForceRecord =
    val tok = new java.util.StringTokenizer(line, ",")
    val t, f1, f2, fs = tok.nextToken()
    ForceRecord(t.toInt * 1e-3, f1.toDouble, f2.toDouble, fs.toDouble)

  private def parseForceCSV(filename: String): IndexedSeq[ForceRecord] =
    Using.resource(new FileReader(filename)): fr =>
      Using.resource(new BufferedReader(fr)): br =>
        br.readLine() // skip first line
        LazyList.continually(br.readLine()).takeWhile(_ ne null).map(parseForceRecord).toIndexedSeq
  end parseForceCSV

  private def isLikelyJerk(v1: Double, v2: Double): Boolean =
    val diff = math.abs(v1 - v2)
    val scale = math.min(math.abs(v1), math.abs(v2))
    !(diff < 0.0012 || diff < 0.1 * scale)

  private def excludeJerks(seq: IndexedSeq[ForceRecord]): IndexedSeq[ForceRecord] =
    val builder = IndexedSeq.newBuilder[ForceRecord]
    builder += seq(0)
    var isBad1, isBad2 = false

    for i <- 1 until seq.size do
      val jerk1 = isLikelyJerk(seq(i - 1).f1, seq(i).f1)
      val jerk2 = isLikelyJerk(seq(i - 1).f2, seq(i).f2)
      if jerk1 || jerk2 then
        println(s"    jerks at index $i: f1=$jerk1, f2=$jerk2")
      isBad1 ^= jerk1
      isBad2 ^= jerk2
      if !isBad1 && !isBad2 then builder += seq(i)

    builder.result()

  private def equateForceDrifts(seq: IndexedSeq[ForceRecord]): IndexedSeq[ForceRecord] =
    val force1Start = seq.head.f1
    val force2Start = seq.head.f2
    val force1End = seq.last.f1
    val force2End = seq.last.f2
    val tStart = seq.head.time
    val tEnd = seq.last.time
    seq.map: elem =>
      val f1 = (elem.f1 - force1Start) + (force1End - force1Start) * (elem.time - tStart) / (tEnd - tStart)
      val f2 = (elem.f2 - force2Start) + (force2End - force2Start) * (elem.time - tStart) / (tEnd - tStart)
      ForceRecord(elem.time, f1, f2, f1 + f2)

  private def parseCleanForceCSV(filename: String): IndexedSeq[ForceRecord] =
    val raw = parseForceCSV(filename)
    val firstZero = raw.indexWhere(_.force == 0)
    if firstZero < 0 then throw new IllegalArgumentException("All known valid force files have a zero region")
    val firstNonZero = raw.indexWhere(_.force != 0, firstZero)
    if firstNonZero < 0 then throw new IllegalArgumentException("All known valid force files have a nonzero region following a zero region")
    equateForceDrifts(excludeJerks(raw.drop(firstNonZero).filter(r => r.f1 >= -0.1 && r.f2 >= -0.1)))

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
    val thrustStart = (0 until rcOutC1.size).find(i => rcOutC1.get(i).asInt > 1001).get
    val thrustEnd = (thrustStart until rcOutC1.size).find(i => rcOutC1.get(i).asInt < 1001).get

    val thrustStartT = rcOutT.get(thrustStart)
    val thrustEndT = rcOutT.get(thrustEnd)
    println(s"  CSV starts ${csv.head.time} ends ${csv.last.time}")
    println(s"  Log starts $thrustStartT ends $thrustEndT")

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
      var area = 0.0
      // this is oriented area of the thrust vs electric power plot
      // this makes sense because we are slowly increasing power/thrust then slowly decreasing them,
      // and they are in a noisy functional dependency, so any misalignment causes the oriented area to increase
      var firstF, lastF, firstP, lastP = -1.0
      if forOffsetDo(offset): (time, force) =>
        val batIdx = batT.indexForTime(time)
        val volt = batVolt.get(batIdx)
        val curr = batCurr.get(batIdx)
        val pow = volt * curr
        if firstF == -1 then
          firstF = force
          firstP = pow
        else
          area += lastF * pow - lastP * force
        lastF = force
        lastP = pow
      then
        area + lastF * firstP - lastP * firstF
      else Double.PositiveInfinity

    assert(batT.size == batVolt.size, s"battery timing is ${batT.size}, battery voltage is ${batVolt.size}")
    assert(batT.size == batCurr.size, s"battery timing is ${batT.size}, battery current is ${batCurr.size}")

    // Find power spent after disarming
    //val batteryThrustStart = batT.size - 1
    //val avgIdlePower = 0.1 * (1 to 10).map(i => batVolt.get(batteryThrustStart - i) * batCurr.get(batteryThrustStart - i)).sum
    //println(s"  Idle power detected: $avgIdlePower")
    val avgIdlePower = 1.9

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
        // this is measured in parrots, really, we don't have any actual device for that
        val airSpeed = if esc1 > 0 && esc2 > 0 then
          0.5 * (esc1 + esc2) / 60 * 0.0762
        else
          (esc1 + esc2) / 60 * 0.0762 // single-prop testing
        val outputEffectivePower = airSpeed * force * 9.8
        val electricPower = volt * curr - avgIdlePower
        val motorImbalance = (esc1 - esc2) / (esc1 + esc2)
        //println(s"RCOU: $rcou, Force (kg): $force, Volt: $volt, Curr: $curr, Electric Power: $electricPower, RPM1: $esc1, RPM2: $esc2, airspeed: $airSpeed, output power: ${airSpeed * force * 9.8}")
        out.println(s"$rcou,$force,$electricPower,$outputEffectivePower,${outputEffectivePower / electricPower},$motorImbalance")

    // Find the best offset in a very straightforward way
    val offsetQualities = Array.tabulate(csv.size)(offsetQuality)
    val bestQuality = offsetQualities.minBy(math.abs)
    if bestQuality.isInfinite then
      throw new AssertionError("Jerks jerked")
    val bestOffset = offsetQualities.indexOf(bestQuality)

    println(s"  Best quality: $bestQuality at offset $bestOffset (CSV time ${csv(bestOffset).time}...${csv(bestOffset).time + thrustEndT - thrustStartT})")

    out.println("RCOU,Thrust,Electric power,Output power,Efficiency,Imbalance")
    offsetDemo(bestOffset)
  end process

  def main(args: Array[String]): Unit =
    val tests = "single" +: (for
      tp <- Seq("open", "duct", "dcnd", "dcn2")
      size <- Seq(20, 30, 40, 50, 60, 61)
    yield s"$tp-${size}mm")

    for test <- tests do
      println(s"Test $test:")
      val binLog = s"${args(0)}/log-$test.bin"
      val csvLog = s"${args(0)}/log-$test.csv"
      if Files.exists(Paths.get(binLog)) && Files.exists(Paths.get(csvLog)) then
        process(Array(binLog, csvLog, s"${args(0)}/efficiency-$test.csv"))
      else
        println(s"  Warning: One of '$binLog', '$csvLog' are missing")
  end main
end EfficiencyLogAnalysis
