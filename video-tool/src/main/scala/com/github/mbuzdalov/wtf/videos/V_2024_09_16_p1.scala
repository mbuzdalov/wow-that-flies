package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.util.PiecewiseLinearFunction as PLW
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, ProfessionallyFitImage, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_09_16_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 42f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(200, 50, 170))
    val msgCF2 = TextMessage.ColorFont(msgFontSize, Color.ORANGE)

    val subImageWidth = height.toDouble * height / width

    val textBaseX = subImageWidth + 0.02 * width

    val allGraphics = GraphicsConsumer.compose(
      ProfessionallyFitImage(
        imageProvider = ProfessionallyFitImage.ClippingProvider(0, 0, subImageWidth.toInt, height),
        minXFun = PLW(0, 0) to (20, 0),
        maxXFun = PLW(2.0, width) to (3.0, subImageWidth),
        minYFun = _ => 0.0, maxYFun = _ => height),

      TextMessage("Over the recent months, I attempted",
        msgCF, textBaseX, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(3, 19),
      TextMessage("multiple tuning attempts for SN1,",
        msgCF, textBaseX, height * 0.06 + 1.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3, 19),
      TextMessage("some successful, some not so much.",
        msgCF, textBaseX, height * 0.06 + 2.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3, 19),

      TextMessage("Over the course of this period,",
        msgCF, textBaseX, height * 0.06 + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(6, 19),
      TextMessage("SN1 got its official name:",
        msgCF, textBaseX, height * 0.06 + 4.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(6, 19),
      TextMessage("The Shouting Star!",
        msgCF2, textBaseX, height * 0.06 + 5.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(6, 19),

      TextMessage("Although the tuning was not finished,",
        msgCF, textBaseX, height * 0.06 + 7 * msgStep, HA.Left, VA.Center)
        .enabledBetween(12, 19),
      TextMessage("I decided that I give it a go and try FPV.",
        msgCF, textBaseX, height * 0.06 + 8 * msgStep, HA.Left, VA.Center)
        .enabledBetween(12, 19),

      TextMessage("This is the footage from that test.",
        msgCF, textBaseX, height * 0.06 + 9.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(15, 19),

      Fade(timeOn = 1, timeOff = 0),
      Fade(timeOn = 19, timeOff = 20),
    )

    props.runVideo(allGraphics)
  end apply
  