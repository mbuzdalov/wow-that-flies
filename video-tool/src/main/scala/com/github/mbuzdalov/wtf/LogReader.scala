package com.github.mbuzdalov.wtf

import scala.language.implicitConversions

import scala.collection.mutable.ArrayBuffer

import com.github.mbuzdalov.wtf.Numbers.given

class LogReader(storage: ByteStorage):
  import LogReader.*

  private val formats = new Array[Format](256)
  private val records = IArray.fill(256)(ArrayBuffer[Long]())

  private lazy val paramName = connect[String]("PARM", "Name")
  private lazy val paramValue = connect[Float]("PARM", "Value")

  private def addFormat(format: Format): Unit =
    if formats(format.id) == null then
      formats(format.id) = format
    else if formats(format.id) != format then
      println(formats(format.id))
      println(format)
      throw new IllegalStateException(s"Redefinition of format (id = ${format.id}, old name ${formats(format.id).name})")

  private def processFormatRecord(offset: Long): Unit =
    val id = storage.uint8(offset + 1).toInt
    val length = storage.uint8(offset + 2).toInt
    val name = storage.string(offset + 3, 4)
    val format = storage.string(offset + 7, 16)
    val labels = storage.string(offset + 23, 64)
    addFormat(Format(id, length, name, format, labels))

  private def earlyProcessRecord(id: Int, offset: Long): Unit =
    if id == 0x80 then processFormatRecord(offset)

  private def scanStorage(): Unit =
    var offset = 0L
    while offset + 2 < storage.size do
      if storage.uint8(offset).toInt != 0xA3 || storage.uint8(offset + 1).toInt != 0x95 then
        System.err.println(s"Skipping byte ${storage.uint8(offset).toInt} at offset $offset as it is not a header")
        offset += 1
      else
        offset += 2
        val id = storage.uint8(offset).toInt
        if formats(id) == null then
          throw new IllegalArgumentException(s"Missing format description for format $id at offset $offset, skipping")
        else
          records(id) += offset
          earlyProcessRecord(id, offset)
          offset += formats(id).length - 2

  private def recordIdByName(name: String): Int = formats.indexWhere(r => r != null && r.name == name)

  addFormat(Format(0x80, 0x59, "FMT", "BBnNZ", "Type,Length,Name,Format,Columns"))
  scanStorage()

  def timingConnect(formatName: String): TimingConnector =
    val id = recordIdByName(formatName)
    if id < 0 then throw new IllegalArgumentException(s"No format named $formatName found")
    val myRecords = records(id)
    val (offsetT, charT) = formats(id).getFieldInfo("TimeUS")
    assert(charT == 'Q')
    new TimingConnector:
      override def size: Int = myRecords.size
      override def get(index: Int): Double = readByCharCT(storage, 'Q', myRecords(index) + offsetT).toDouble * 1e-6
      override def indexForTime(time: Double): Int =
        if myRecords.isEmpty || get(0) > time then -1 else
          var left = 0
          var right = size
          while right - left > 1 do
            val mid = (left + right) >>> 1
            if get(mid) > time then
              right = mid
            else 
              left = mid
          left    


  def connect[T](formatName: String, fieldName: String): Connector[T] =
    val id = recordIdByName(formatName)
    if id < 0 then throw new IllegalArgumentException(s"No format named $formatName found")
    val myRecords = records(id)
    val (offset, foundChar) = formats(id).getFieldInfo(fieldName)

    new Connector[T]:
      def size: Int = myRecords.size
      def get(index: Int): T = readByChar(storage, foundChar, myRecords(index) + offset).asInstanceOf[T]

  def getParameter(name: String): Float =
    val index = (0 until paramName.size).indexWhere(i => paramName.get(i) == name)
    if index == -1 then throw new IllegalArgumentException(s"Parameter $name not found")
    paramValue.get(index)

  lazy val autoArmedTimes: IndexedSeq[Double] =
    val eventValues = connect[Numbers.UInt8]("EV", "Id")
    val eventTimes = connect[Numbers.UInt64]("EV", "TimeUS")
    (0 until eventValues.size).filter(i => eventValues.get(i).toInt == 15).map(i => eventTimes.get(i).toDouble * 1e-6)

end LogReader

object LogReader:
  private case class Format(id: Int, length: Int, name: String, format: String, labels: String):
    private val offsets = format.map(sizeByChar).scanLeft(1)(_ + _)
    private val labelList = labels.split(',').toIndexedSeq
    assert(labelList.size == format.length)
    def getFieldInfo(name: String): (Int, Char) =
      val index = labelList.indexOf(name)
      (offsets(index), format(index))

  trait Connector[+T]:
    def size: Int
    def get(index: Int): T

  trait TimingConnector:
    def size: Int
    def get(index: Int): Double
    def indexForTime(time: Double): Int
  
  private def sizeByChar(ch: Char): Int = ch match
    case 'a' | 'Z' => 64
    case 'b' | 'B' | 'M' => 1
    case 'h' | 'H' | 'c' | 'C' => 2
    case 'i' | 'I' | 'f' | 'n' | 'e' | 'E' | 'L' => 4
    case 'd' | 'q' | 'Q' => 8
    case 'N' => 16

  private def readByChar(storage: ByteStorage, ch: Char, offset: Long): Any = ch match
    case 'a' => Array.tabulate(32)(i => storage.int16(offset + 2 * i))
    case 'b' => storage.int8(offset)
    case 'B' => storage.uint8(offset)
    case 'h' => storage.int16(offset)
    case 'H' => storage.uint16(offset)
    case 'i' => storage.int32(offset)
    case 'I' => storage.uint32(offset)
    case 'f' => storage.float32(offset)
    case 'd' => storage.float64(offset)
    case 'q' => storage.int64(offset)
    case 'Q' => storage.uint64(offset)
    case 'n' => storage.string(offset, 4)
    case 'N' => storage.string(offset, 16)
    case 'Z' => storage.string(offset, 64)
    case 'c' => storage.int16(offset) * 0.01f
    case 'C' => storage.uint16(offset) * 0.01f
    case 'e' => storage.int32(offset) * 0.01
    case 'E' => storage.uint32(offset).toLong * 0.01
    case 'L' => storage.int32(offset) // convert to lat/lon
    case 'M' => storage.uint8(offset) // convert to flight mode somehow

  transparent inline def readByCharCT(storage: ByteStorage, ch: Char, offset: Long): Any = inline ch match
    case 'a' => Array.tabulate(32)(i => storage.int16(offset + 2 * i))
    case 'b' => storage.int8(offset)
    case 'B' => storage.uint8(offset)
    case 'h' => storage.int16(offset)
    case 'H' => storage.uint16(offset)
    case 'i' => storage.int32(offset)
    case 'I' => storage.uint32(offset)
    case 'f' => storage.float32(offset)
    case 'd' => storage.float64(offset)
    case 'q' => storage.int64(offset)
    case 'Q' => storage.uint64(offset)
    case 'n' => storage.string(offset, 4)
    case 'N' => storage.string(offset, 16)
    case 'Z' => storage.string(offset, 64)
    case 'c' => storage.int16(offset) * 0.01f
    case 'C' => storage.uint16(offset) * 0.01f
    case 'e' => storage.int32(offset) * 0.01
    case 'E' => storage.uint32(offset).toLong * 0.01
    case 'L' => storage.int32(offset) // convert to lat/lon
    case 'M' => storage.uint8(offset) // convert to flight mode somehow
end LogReader
