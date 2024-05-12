package com.github.mbuzdalov.wtf

import java.io.InputStream

class ByteStorage(stream: InputStream):
  private val bufSize = 1 << 16
  private val array = locally:
    val builder = IArray.newBuilder[IArray[Byte]]
    val buffer = new Array[Byte](bufSize)
    var readBytes: Int = 0
    while
      readBytes = stream.read(buffer)
      readBytes == bufSize
    do
      builder += IArray(buffer *)
    if readBytes > 0 then builder += IArray.tabulate(readBytes)(buffer)
    builder.result()

  def size: Long = if array.length == 0 then 0 else
    (array.length - 1L) * bufSize + array.last.length

  def int8(index: Long): Byte = array((index >>> 16).toInt)((index & 0xFFFF).toInt)
  def uint8(index: Long): Numbers.UInt8 = Numbers.uint8(int8(index))
  def uint16(index: Long): Numbers.UInt16 = Numbers.uint16(uint8(index) | (uint8(index + 1) << 8))
  def int16(index: Long): Short = (uint8(index) | (uint8(index + 1) << 8)).toShort
  def int32(index: Long): Int = uint16(index).toInt | (uint16(index + 2).toInt << 16)
  def uint32(index: Long): Numbers.UInt32 = Numbers.uint32(int32(index))
  def int64(index: Long): Long = uint32(index).toLong | (uint32(index + 4).toLong << 32)
  def uint64(index: Long): Numbers.UInt64 = Numbers.uint64(int64(index))
  
  def float32(index: Long): Float = java.lang.Float.intBitsToFloat(int32(index))
  def float64(index: Long): Double = java.lang.Double.longBitsToDouble(int64(index))
  def string(index: Long, length: Int): String = new String(Array.tabulate(length)(i => int8(index + i))).trim
end ByteStorage
