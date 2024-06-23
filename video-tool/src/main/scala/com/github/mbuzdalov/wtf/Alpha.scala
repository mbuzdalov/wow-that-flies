package com.github.mbuzdalov.wtf

case class Alpha(time: Double, speed: Double):
  def asOn(t: Double): Double = math.max(0, math.min(1, (t - time) * speed))

  def asOff(t: Double): Double = math.max(0, math.min(1, (time - t) * speed))

object Alpha:
  given Conversion[Int, Alpha] with
    override def apply(time: Int): Alpha = Alpha(time, 2.0)
  
  given Conversion[Double, Alpha] with
    override def apply(time: Double): Alpha = Alpha(time, 2.0)
  
  extension (time: Double)
    infix def withSpeed(speed: Double): Alpha = Alpha(time, speed)
  
  extension (time: Int)
    infix def withSpeed(speed: Double): Alpha = Alpha(time, speed)
