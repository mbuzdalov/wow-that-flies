package com.github.mbuzdalov.wtf.videos

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}
import com.github.mbuzdalov.wtf.util.PiecewiseLinearFunction as PLW
import com.github.mbuzdalov.wtf.widgets.{Fade, GlowingCircle, ProfessionallyFitImage, TextMessage}
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}

object V_2024_07_26_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val firstImage = ImageIO.read(new File(props.property("--image1")))
    val secondImage = ImageIO.read(new File(props.property("--image2")))
    val thirdImage = ImageIO.read(new File(props.property("--image3")))

    val msgFontSize = 42f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(200, 50, 170))

    val msgCFL = TextMessage.ColorFont(msgFontSize, Color.ORANGE)

    val img1Width = height.toDouble / firstImage.getHeight * firstImage.getWidth
    val img2Width = height.toDouble / secondImage.getHeight * secondImage.getWidth
    val img3Width = height.toDouble / thirdImage.getHeight * thirdImage.getWidth

    val textBaseX1 = img1Width + 0.02 * width
    val textBaseX3 = img3Width + 0.02 * width

    val allGraphics = GraphicsConsumer.compose(
      ProfessionallyFitImage(
        image = firstImage,
        minXFun = PLW(0, 0) to (79, 0) to (80, -img1Width),
        maxXFun = PLW(2.0, width) to (3.0, img1Width) to (79, img1Width) to (80, 0),
        minYFun = _ => 0.0, maxYFun = _ => height)
        .withAlpha(PLW(0, 0) to (0.001, 1) to (79, 1) to (80, 0)),

      TextMessage("SN1 has a new look!",
        msgCF, textBaseX1, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(4, 13.5),
      TextMessage("It is now one section higher,",
        msgCF, textBaseX1, height * 0.06 + 1.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(6, 13.5),
      TextMessage("and all plastic details are printed with PETG.",
        msgCF, textBaseX1, height * 0.06 + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(6, 13.5),
      TextMessage("All drawings will be open-sourced soon.",
        msgCF, textBaseX1, height * 0.06 + 4.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(10, 13.5),

      GlowingCircle(14, 2, 28, img1Width * 0.5, height * 0.6, img1Width * 0.23, height * 0.04, height * 0.01, msgCFL.fontColor),
      GlowingCircle(14, 2, 28, img1Width * 0.5, height * 0.12, img1Width * 0.23, height * 0.04, height * 0.01, msgCFL.fontColor),
      TextMessage("Motors and Propellers",
        msgCFL, textBaseX1, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(14, 28),
      TextMessage("Coax copters have two contra-rotating motors,",
        msgCF, textBaseX1, height * 0.06 + 1.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(15, 28),
      TextMessage("difference of rotation speeds controls yaw.",
        msgCF, textBaseX1, height * 0.06 + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(15, 28),
      TextMessage("Motors: RCinPower Smoox Plus 1507 2680KV.",
        msgCF, textBaseX1, height * 0.06 + 4.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(19, 28),
      TextMessage("Props: HQProp Duct 89mm 8-blade.",
        msgCF, textBaseX1, height * 0.06 + 5.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(19, 28),
      TextMessage("Not super efficient, but can “punch”.",
        msgCF, textBaseX1, height * 0.06 + 6.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(23, 28),

      GlowingCircle(29, 2, 43, img1Width * 0.48, height * 0.77, img1Width * 0.23, height * 0.1, height * 0.01, msgCFL.fontColor),
      TextMessage("Flaps",
        msgCFL, textBaseX1, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(29, 43),
      TextMessage("Flaps control pitch and roll by deflecting air.",
        msgCF, textBaseX1, height * 0.06 + 1.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(31, 43),
      TextMessage("Here, flaps are arranged in an X,",
        msgCF, textBaseX1, height * 0.06 + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(31, 43),
      TextMessage("so one flap controls “pitch plus roll”,",
        msgCF, textBaseX1, height * 0.06 + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(31, 43),
      TextMessage("another one controls “pitch minus roll”.",
        msgCF, textBaseX1, height * 0.06 + 4.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(31, 43),
      TextMessage("Why? To have control in belly-flop descent.",
        msgCF, textBaseX1, height * 0.06 + 6.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(37, 43),
      TextMessage("This is not supported yet in Ardupilot,",
        msgCF, textBaseX1, height * 0.06 + 7.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(39, 43),
      TextMessage("so I am improving the code to enable it.",
        msgCF, textBaseX1, height * 0.06 + 8.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(39, 43),

      GlowingCircle(44, 2, 53, img1Width * 0.4, height * 0.88, height * 0.05, height * 0.05, height * 0.01, msgCFL.fontColor),
      GlowingCircle(44, 2, 53, img1Width * 0.8, height * 0.80, height * 0.05, height * 0.05, height * 0.01, msgCFL.fontColor),
      TextMessage("Servos",
        msgCFL, textBaseX1, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(44, 53),
      TextMessage("2x EMAX ES9052MD Metal Gear",
        msgCF, textBaseX1, height * 0.06 + 1 * msgStep, HA.Left, VA.Center)
        .enabledBetween(46, 53),
      TextMessage("digital servos.",
        msgCF, textBaseX1, height * 0.06 + 2 * msgStep, HA.Left, VA.Center)
        .enabledBetween(46, 53),
      TextMessage("They are mounted to two of the legs",
        msgCF, textBaseX1, height * 0.06 + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(48, 53),
      TextMessage("and drive flaps directly to reduce slack.",
        msgCF, textBaseX1, height * 0.06 + 4.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(48, 53),

      GlowingCircle(54, 2, 64, img1Width * 0.67, height * 0.45, height * 0.06, height * 0.06, height * 0.01, msgCFL.fontColor),
      TextMessage("Flight Controller",
        msgCFL, textBaseX1, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(54, 64),
      TextMessage("FlywooF745 running Ardupilot.",
        msgCF, textBaseX1, height * 0.06 + 1.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(54, 64),
      TextMessage("One of the motor channels was burned,",
        msgCF, textBaseX1, height * 0.06 + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(56, 64),
      TextMessage("but for a coax, all four are not needed :)",
        msgCF, textBaseX1, height * 0.06 + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(56, 64),
      TextMessage("Also, hwdef adjustments were needed",
        msgCF, textBaseX1, height * 0.06 + 5.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(58, 64),
      TextMessage("to make servo outputs possible.",
        msgCF, textBaseX1, height * 0.06 + 6.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(58, 64),

      GlowingCircle(65, 2, 79, img1Width * 0.74, height * 0.28, height * 0.06, height * 0.06, height * 0.01, msgCFL.fontColor),
      TextMessage("GPS and Compass",
        msgCFL, textBaseX1, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(65, 79),
      TextMessage("Beitian BN-880.",
        msgCF, textBaseX1, height * 0.06 + 1.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(66, 79),
      TextMessage("Compass is quite close both to",
        msgCF, textBaseX1, height * 0.06 + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(69, 79),
      TextMessage("power wires and motors, so is not used",
        msgCF, textBaseX1, height * 0.06 + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(69, 79),
      TextMessage("currently and needs better configuring.",
        msgCF, textBaseX1, height * 0.06 + 4.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(69, 79),
      TextMessage("Hopefully this all will once perform",
        msgCF, textBaseX1, height * 0.06 + 6 * msgStep, HA.Left, VA.Center)
        .enabledBetween(74, 79),
      TextMessage("autonomous flights...",
        msgCF, textBaseX1, height * 0.06 + 7 * msgStep, HA.Left, VA.Center)
        .enabledBetween(74, 79),

      ProfessionallyFitImage(
        image = secondImage,
        minXFun = PLW(79, width) to (80, width - img2Width) to (104, width - img2Width) to (105, width),
        maxXFun = PLW(79, width + img2Width) to (80, width) to (104, width) to (105, width + img2Width),
        minYFun = _ => 0.0, maxYFun = _ => height,
      ).withAlpha(PLW(79, 0) to (80, 1) to (104, 1) to (105, 0)),

      GlowingCircle(81, 2, 91, width - img2Width * 0.405, height * 0.54, height * 0.03, height * 0.03, height * 0.01, msgCFL.fontColor),
      TextMessage("Remote Control",
        msgCFL, 0.02 * width, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(81, 91),
      TextMessage("ELRS, an old Happymodel EP1.",
        msgCF, 0.02 * width, height * 0.06 + 1.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(82, 91),
      TextMessage("Antenna orientation is hopefully good both",
        msgCF, 0.02 * width, height * 0.06 + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(85, 91),
      TextMessage("both for hover and for belly-flop landing.",
        msgCF, 0.02 * width, height * 0.06 + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(85, 91),

      GlowingCircle(92, 2, 104, width - img2Width * 0.77, height * 0.3, height * 0.04, height * 0.2, height * 0.01, msgCFL.fontColor),
      GlowingCircle(92, 2, 104, width - img2Width * 0.2, height * 0.3, height * 0.04, height * 0.2, height * 0.01, msgCFL.fontColor),
      TextMessage("Batteries",
        msgCFL, 0.02 * width, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(92, 104),
      TextMessage("2x NuPROL 2S 1450 mAh 30C batteries.",
        msgCF, 0.02 * width, height * 0.06 + 1.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(93, 104),
      TextMessage("Their shape makes them a very good choice",
        msgCF, 0.02 * width, height * 0.06 + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(96, 104),
      TextMessage("for SN1. Internal resistance is a bit high,",
        msgCF, 0.02 * width, height * 0.06 + 3.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(96, 104),
      TextMessage("but better than whoop batteries used before.",
        msgCF, 0.02 * width, height * 0.06 + 4.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(96, 104),
      TextMessage("Generally, at least two batteries are needed",
        msgCF, 0.02 * width, height * 0.06 + 6 * msgStep, HA.Left, VA.Center)
        .enabledBetween(101, 104),
      TextMessage("to perform weight balancing.",
        msgCF, 0.02 * width, height * 0.06 + 7 * msgStep, HA.Left, VA.Center)
        .enabledBetween(101, 104),

      ProfessionallyFitImage(
        image = thirdImage,
        minXFun = PLW(104, -img3Width) to (105, 0),
        maxXFun = PLW(104, 0) to (105, img3Width),
        minYFun = _ => 0.0, maxYFun = _ => height,
      ).withAlpha(PLW(104, 0) to (105, 1)),

      GlowingCircle(106, 2, 112, img3Width * 0.63, height * 0.5, height * 0.1, height * 0.15, height * 0.01, msgCFL.fontColor),
      TextMessage("Video system",
        msgCFL, textBaseX3, height * 0.06, HA.Left, VA.Center)
        .enabledBetween(106, 112),
      TextMessage("HDZero Whoop Lite.",
        msgCF, textBaseX3, height * 0.06 + 1.0 * msgStep, HA.Left, VA.Center)
        .enabledBetween(107, 112),
      TextMessage("Not yet connected, but soon will be.",
        msgCF, textBaseX3, height * 0.06 + 2.5 * msgStep, HA.Left, VA.Center)
        .enabledBetween(109, 112),

      Fade(timeOn = 1.0, timeOff = 0),
      Fade(timeOn = 112, timeOff = 113),
    )

    props.runEmpty(113, allGraphics)
  end apply
  