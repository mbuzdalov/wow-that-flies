package com.github.mbuzdalov.wtf

import java.io.ByteArrayInputStream

import com.github.mbuzdalov.wtf.Numbers.*

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class LogReaderTests extends AnyFlatSpec, Matchers:
  private val storage0 = new ByteStorage(new ByteArrayInputStream(new Array[Byte](239)))
  private val storage1 = new ByteStorage(new ByteArrayInputStream(Array.fill(239)('1'.toByte)))

  "Single-character reading" should "read zero byte (b)" in:
    val value: Byte = LogReader.readByCharCT(storage0, 'b', 0)
    value shouldBe 0

  it should "read zero unsigned byte (B)" in:
    val value: UInt8 = LogReader.readByCharCT(storage0, 'B', 0)
    value.asInt shouldBe 0

  it should "read zero int16 (h)" in:
    val value: Short = LogReader.readByCharCT(storage0, 'h', 0)
    (value: Int) shouldBe 0

  it should "read zero uint16 (H)" in:
    val value: UInt16 = LogReader.readByCharCT(storage0, 'H', 0)
    value.asInt shouldBe 0

  it should "read zero int32 (i)" in:
    val value: Int = LogReader.readByCharCT(storage0, 'i', 0)
    value shouldBe 0

  it should "read zero uint64 (Q)" in :
    val value: UInt64 = LogReader.readByCharCT(storage0, 'Q', 0)
    value.signum shouldBe 0

  it should "read zero float (f)" in:
    val value: Float = LogReader.readByCharCT(storage0, 'f', 0)
    value shouldBe 0.0f

  it should "read zero double (d)" in:
    val value: Double = LogReader.readByCharCT(storage0, 'd', 0)
    value shouldBe 0.0

  it should "read zero uint32 (I)" in:
    val value: UInt32 = LogReader.readByCharCT(storage0, 'I', 0)
    value.asLong shouldBe 0

  it should "read zero short array (a)" in:
    val value: Array[Short] = LogReader.readByCharCT(storage0, 'a', 0)
    value.length shouldBe 32
    value.foreach(_ shouldBe 0)

  it should "read all-'1' string (n)" in:
    val value: String = LogReader.readByCharCT(storage1, 'n', 0)
    value shouldEqual ("1" * 4)

  it should "read all-'1' string (N)" in:
    val value: String = LogReader.readByCharCT(storage1, 'N', 0)
    value shouldEqual ("1" * 16)

  it should "read all-'1' string (Z)" in:
    val value: String = LogReader.readByCharCT(storage1, 'Z', 0)
    value shouldEqual ("1" * 64)

  it should "trim all-zero string (n)" in:
    val value: String = LogReader.readByCharCT(storage0, 'n', 0)
    value shouldEqual ""

  it should "trim all-zero string (N)" in:
    val value: String = LogReader.readByCharCT(storage0, 'N', 0)
    value shouldEqual ""

  it should "trim all-zero  string (Z)" in:
    val value: String = LogReader.readByCharCT(storage0, 'Z', 0)
    value shouldEqual ""
end LogReaderTests
