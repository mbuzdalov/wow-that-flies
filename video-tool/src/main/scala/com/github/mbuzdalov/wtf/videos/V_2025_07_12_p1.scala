package com.github.mbuzdalov.wtf.videos

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

import com.github.mbuzdalov.wtf.util.PiecewiseLinearFunction
import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_07_12_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val file1 = ImageIO.read(new File(props.property("--img-1")))
    val file2 = ImageIO.read(new File(props.property("--img-2")))
    val file3 = ImageIO.read(new File(props.property("--img-3")))
    val file4 = ImageIO.read(new File(props.property("--img-4")))
    val file5 = ImageIO.read(new File(props.property("--img-5")))

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(90, 0, 50))

    val bigFontSize = 54f * width / 1280
    val bigStep = bigFontSize * 1.5f
    val bigCF = TextMessage.ColorFont(bigFontSize, new Color(90, 0, 50))

    val msgLeft = 0.011 * width
    val msgTop = 0.06 * height

    val blanketBackground = new Color(255, 255, 255, 200)

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(what = Some(file1), scale = t => 1.0 + (t - 0) / 150, scaleCenterX = (w, t) => 0.0, rotate = t => 0.0)
        .enabledBetween(0, 10),
      LeftBlanket(0.28, blanketBackground, 0.5, 0.8, 9.2, 9.5),
      TextMessage("T3C: The Tilt-Tricopter!",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(1, 9),
      TextMessage("Three motors,",
        msgCF, msgLeft, msgTop + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(3, 9),
      TextMessage("each with its own",
        msgCF, msgLeft, msgTop + 3 * msgStep, HA.Left, VA.Center)
        .enabledBetween(4.5, 9),
      TextMessage("gimbal servo,",
        msgCF, msgLeft, msgTop + 4 * msgStep, HA.Left, VA.Center)
        .enabledBetween(4.5, 9),
      TextMessage("all controlled",
        msgCF, msgLeft, msgTop + 5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(6, 9),
      TextMessage("independently.",
        msgCF, msgLeft, msgTop + 6 * msgStep, HA.Left, VA.Center)
        .enabledBetween(6, 9),

      ScaleRotateCropBack(what = Some(file2), scale = t => 1.0 + (t - 10) / 150, scaleCenterX = (w, t) => 0.0, scaleCenterY = (h, t) => 0.0, rotate = t => 0.0)
        .enabledBetween(10, 20),
      TopBlanket(0.25, blanketBackground, 10.5, 10.8, 19.2, 19.5, 0.0, 0.54),
      TextMessage("This is a project one of my subscribers wants:",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(11, 19),
      TextMessage("a tricopter with 5 degrees of freedom, where",
        msgCF, msgLeft, msgTop + msgStep, HA.Left, VA.Center)
        .enabledBetween(13.5, 19),
      TextMessage("one controls pitch and speed independently.",
        msgCF, msgLeft, msgTop + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(13.5, 19),

      ScaleRotateCropBack(what = Some(file3), scale = t => 1.0 + (t - 20) / 150, scaleCenterX = (w, t) => 0.0, scaleCenterY = (h, t) => h, rotate = t => 0.0)
        .enabledBetween(20, 30),
      BottomBlanket(0.19, blanketBackground, 20.5, 20.8, 29.2, 29.5, 0.38, 1.0),
      TextMessage("ArduPilot does not yet support this kind of machine,",
        msgCF, 0.41 * width, height - msgTop - msgStep, HA.Left, VA.Center)
        .enabledBetween(21, 29),
      TextMessage("so I have to implement at least the frame code.",
        msgCF, 0.41 * width, height - msgTop, HA.Left, VA.Center)
        .enabledBetween(23.5, 29),

      ScaleRotateCropBack(what = Some(file4), scale = t => 1.0 + (t - 30) / 150, scaleCenterX = (w, t) => w * 0.2, scaleCenterY = (h, t) => h * 0.2, rotate = t => 0.0)
        .enabledBetween(30, 40),
      TopBlanket(0.25, blanketBackground, 30.5, 30.8, 39.2, 39.5, 0.0, 0.4),
      TextMessage("The design...",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(31, 39),
      TextMessage("Well, it's known to be flawed,",
        msgCF, msgLeft, msgTop + msgStep, HA.Left, VA.Center)
        .enabledBetween(33.5, 39),
      TextMessage("but will still serve the purpose.",
        msgCF, msgLeft, msgTop + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(35, 39),

      ScaleRotateCropBack(what = Some(file5), scale = t => 1.0 + (t - 40) / 300, scaleCenterX = (w, t) => w * 0.65, rotate = t => 0.0)
        .enabledBetween(40, 56),
      ColoredRectangle(0, 0, 1, 1, blanketBackground).withAlpha(PiecewiseLinearFunction(42, 0) to (44, 1) to (52, 1) to (53, 0)),
      TextMessage("This is the first video",
        bigCF, width * 0.5, height * 0.3, HA.Center, VA.Center)
        .enabledBetween(43.5, 52.5),
      TextMessage("of the series, which is dedicated to",
        bigCF, width * 0.5, height * 0.3 + bigStep, HA.Center, VA.Center)
        .enabledBetween(43.5, 52.5),
      TextMessage("making this thing Basically Flyable.",
        bigCF, width * 0.5, height * 0.3 + 2 * bigStep, HA.Center, VA.Center)
        .enabledBetween(43.5, 52.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 55.5, timeOff = 56),
    )

    props.runEmpty(56, allGraphics)
  end apply
  