package com.github.mbuzdalov.wtf.videos

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

import com.github.mbuzdalov.wtf.util.PiecewiseLinearFunction
import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_01_p7:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val image = ImageIO.read(new File(props.property("--input")))
    val subImageWidth = height.toDouble * height / width

    val subImageRelWidth = subImageWidth / width

    val allGraphics = GraphicsConsumer.compose(
      ProfessionallyFitImage(
        imageProvider = ProfessionallyFitImage.ConstantProvider(image),
        minXFun = PiecewiseLinearFunction(0, 0) to (20, 0),
        maxXFun = PiecewiseLinearFunction(2.0, width) to (3.0, subImageWidth),
        minYFun = _ => 0.0, maxYFun = _ => height),

      RightBlanket(1 - subImageRelWidth, new Color(255, 255, 255, 200), 2, 3, 100, 100.1),
      TextMessage("To ease landings, especially by FPV,",
        msgCF, width * (subImageRelWidth + 0.02), height * 0.3, HA.Left, VA.Center)
        .enabledBetween(3, 10.5),
      TextMessage("there is now a pluggable cardboard surface",
        msgCF, width * (subImageRelWidth + 0.02), height * 0.3 + msgStep, HA.Left, VA.Center)
        .enabledBetween(3, 10.5),
      TextMessage("which you can hit safely and drop the throttle,",
        msgCF, width * (subImageRelWidth + 0.02), height * 0.3 + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3, 10.5),
      TextMessage("then the vehicle falls straight into the cage",
        msgCF, width * (subImageRelWidth + 0.02), height * 0.3 + 3 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3, 10.5),
      TextMessage("as seen in the beginning of the video.",
        msgCF, width * (subImageRelWidth + 0.02), height * 0.3 + 4 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3, 10.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 10.5, timeOff = 11),
    )

    props.runEmpty(11, allGraphics)
  end apply
  