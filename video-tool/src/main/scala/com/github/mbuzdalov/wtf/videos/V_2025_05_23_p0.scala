package com.github.mbuzdalov.wtf.videos

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_05_23_p0:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val file1 = ImageIO.read(new File(props.property("--img-1")))
    val file2 = ImageIO.read(new File(props.property("--img-2")))
    val file3 = ImageIO.read(new File(props.property("--img-3")))
    val file4 = ImageIO.read(new File(props.property("--img-4")))

    val initFontSize = 48f * width / 1280
    val initStep = initFontSize * 1.5f
    val initCF = TextMessage.ColorFont(initFontSize, new Color(200, 200, 60))

    val initCenter = width * 0.5
    val initTop = height * 0.2

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(250, 250, 0))

    val msgLeft = 0.03 * width
    val msgTop = 0.06 * height

    val allGraphics = GraphicsConsumer.compose(
      TextMessage("Sometimes you need to stop",
        initCF, initCenter, initTop, HA.Center, VA.Center)
        .enabledBetween(1, 10),
      TextMessage("and ask yourself: Do I do the thing right?",
        initCF, initCenter, initTop + initStep, HA.Center, VA.Center)
        .enabledBetween(2, 10),
      TextMessage("In my case, one of such questions is:",
        initCF, initCenter, initTop + 3 * initStep, HA.Center, VA.Center)
        .enabledBetween(4, 10),
      TextMessage("Do I design coaxial propulsion right?",
        initCF, initCenter, initTop + 4 * initStep, HA.Center, VA.Center)
        .enabledBetween(6, 10),

      ScaleRotateCropBack(what = Some(file1), scale = t => 1.0 + (t + 25) / 300, scaleCenterX = (w, t) => 0.0, scaleCenterY = (h, t) => h / 4, rotate = t => 0.0)
        .enabledBetween(10, 28),
      ColoredRectangle(0, 0, 0.55, 1, Color(0, 0, 0, 100))
        .enabledBetween(11, 28),
      TextMessage("Using CX7's details and a pair of load cells,",
        msgCF, msgLeft, msgTop, HA.Left, VA.Center)
        .enabledBetween(12, 28),
      TextMessage("I am going to measure thrust, electric power",
        msgCF, msgLeft, msgTop + 1.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(15, 28),
      TextMessage("and a bunch of other values to determine",
        msgCF, msgLeft, msgTop + 2.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(15, 28),
      TextMessage("the efficiency of different setups using:",
        msgCF, msgLeft, msgTop + 3.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(20, 28),
      TextMessage("plain ducts...",
        msgCF, msgLeft, msgTop + 4.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(23, 28),

      ScaleRotateCropBack(what = Some(file2), scale = t => 1.0 + (t - 5) / 200, scaleCenterX = (w, t) => 0.0, scaleCenterY = (h, t) => 0.0, rotate = t => 0.0)
        .enabledBetween(28, 35),
      ColoredRectangle(0, 0, 0.4, 1, Color(0, 0, 0, 100))
        .enabledBetween(28.5, 35),
      TextMessage("... ducts with rounded inlets...",
        msgCF, msgLeft, msgTop + 4.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(29, 35),

      ScaleRotateCropBack(what = Some(file3), scale = t => 1.0 + (t - 35) / 200, scaleCenterX = (w, t) => 0.0, rotate = t => 0.0)
        .enabledBetween(35, 44),
      ColoredRectangle(0, 0, 0.34, 1, Color(0, 0, 0, 100))
        .enabledBetween(35.5, 44),
      TextMessage("... or nothing, each with",
        msgCF, msgLeft, msgTop + 5.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(36, 44),
      TextMessage("distances between props",
        msgCF, msgLeft, msgTop + 6.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(36, 44),
      TextMessage("ranging from 20 mm...",
        msgCF, msgLeft, msgTop + 7.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(36, 44),

      ScaleRotateCropBack(what = Some(file4), scale = t => 1.0 + (t - 44) / 200, scaleCenterX = (w, t) => 0.0, scaleCenterY = (h, t) => h, rotate = t => 0.0)
        .enabledBetween(44, 55),
      ColoredRectangle(0, 0, 0.33, 1, Color(0, 0, 0, 100))
        .enabledBetween(44.5, 55),
      TextMessage("... to 60 mm.",
        msgCF, msgLeft, msgTop + 5.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(45, 55),
      TextMessage("What you will see next",
        msgCF, msgLeft, msgTop + 6.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(49, 55),
      TextMessage("is records of these tests",
        msgCF, msgLeft, msgTop + 7.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(49, 55),
      TextMessage("and some data analysis.",
        msgCF, msgLeft, msgTop + 8.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(49, 55),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 54.5, timeOff = 55),
    )

    props.runEmpty(55, allGraphics)
  end apply
  