package com.github.mbuzdalov.wtf

import scala.annotation.targetName

object Numbers:
  opaque type UInt8 = Byte
  opaque type UInt16 = Short
  opaque type UInt32 = Int
  opaque type UInt64 = Long

  def uint8(raw: Byte): UInt8 = raw
  def uint16(raw: Short): UInt16 = raw
  def uint32(raw: Int): UInt32 = raw
  def uint64(raw: Long): UInt64 = raw

  def uint16(value: Int): UInt16 = value.toShort

  given Conversion[UInt8, Int] with
    def apply(value: UInt8): Int = value & 0xFF

  given Conversion[UInt16, Int] with
    def apply(value: UInt16): Int = value & 0xFFFF

  given Conversion[UInt32, Long] with
    def apply(value: UInt32): Long = value & 0xFFFFFFFFL

  extension (value: UInt8)
    def bitVal(index: Int): Int = value & (1 << index)
    def bitSet(index: Int): Boolean = if bitVal(index) == 0 then false else true
    def toString: String = (value & 0xFF).toString

  extension (value: UInt16)
    def bitVal(index: Int): Int = value & (1 << index)
    def bitSet(index: Int): Boolean = if bitVal(index) == 0 then false else true
    def toString: String = (value & 0xFFFF).toString

  extension (value: UInt32)
    def bitVal(index: Int): Int = value & (1 << index)
    def bitSet(index: Int): Boolean = if bitVal(index) == 0 then false else true
    def toString: String = java.lang.Integer.toUnsignedString(value)

  extension (value: UInt64)
    def bitVal(index: Int): Long = value & (1L << index)
    def bitSet(index: Int): Boolean = if bitVal(index) == 0 then false else true
    def toString: String = java.lang.Long.toUnsignedString(value)
    def toDouble: Double = if value >= 0 then value.toDouble else value + math.pow(2, 63)

    @targetName("less")
    def <(that: Long): Boolean =
      if value < 0 then false
      else if that < 0 then false
      else value < that

    @targetName("greater")
    def >(that: Long): Boolean =
      if that < 0 then true
      else if value < 0 then true
      else value > that
    def signum: Int = if value == 0 then 0 else 1

end Numbers
