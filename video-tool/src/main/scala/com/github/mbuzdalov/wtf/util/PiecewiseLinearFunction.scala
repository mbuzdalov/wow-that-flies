package com.github.mbuzdalov.wtf.util

class PiecewiseLinearFunction(seq: IndexedSeq[(Double, Double)]) extends (Double => Double):
  override def apply(time: Double): Double =
    if time <= seq.head._1 then seq.head._2
    else if time >= seq.last._1 then seq.last._2
    else
      val t1 = seq.indexWhere(_._1 >= time)
      val t0 = t1 - 1
      val (x0, y0) = seq(t0)
      val (x1, y1) = seq(t1)
      (y0 * (x1 - time) + y1 * (time - x0)) / (x1 - x0)

  infix def to(time: Double, value: Double): PiecewiseLinearFunction = 
    if seq.nonEmpty then
      require(time > seq.last._1, s"New time $time is not greater than last time ${seq.last._1}")
    PiecewiseLinearFunction(seq :+ (time -> value))

object PiecewiseLinearFunction:
  given Conversion[(Double, Double), PiecewiseLinearFunction] = p => PiecewiseLinearFunction(IndexedSeq(p))
  