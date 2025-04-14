package com.github.mbuzdalov.wtf.videos

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

import com.github.mbuzdalov.wtf.util.{ImageTransform, PiecewiseLinearFunction}
import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_04_12_p0:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val img1 = ImageTransform.clockwise(ImageIO.read(new File(props.property("--img-1"))))
    val img2 = ImageTransform.clockwise(ImageIO.read(new File(props.property("--img-2"))))
    val img3 = ImageTransform.clockwise(ImageIO.read(new File(props.property("--img-3"))))
    val img4 = ImageTransform.clockwise(ImageIO.read(new File(props.property("--img-4"))))

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, Color.YELLOW)

    val centerH = TextMessage.HorizontalAlignment.Center
    val centerV = TextMessage.VerticalAlignment.Center

    val ar2 = math.pow(1080.0 / 1920, 2)
    val centerL = width * ((1 - ar2) / 2)
    val centerR = width * (ar2 + (1 - ar2) / 2)

    val allGraphics = GraphicsConsumer.compose(
      ProfessionallyFitImage(ProfessionallyFitImage.ScalingProvider(img1, t => math.max(1, 1 + (t - 3) / 200)),
        minXFun = t => 0, maxXFun = PiecewiseLinearFunction(0, width) to (1, width) to (1.5, width * ar2) to (10.5, width * ar2) to (11, 0),
        minYFun = t => 0, maxYFun = t => height),

      TextMessage("The landing cage received a new upgrade.",
        msgCF, centerR, height * 0.3, centerH, centerV).enabledBetween(2.5, 10),
      TextMessage("Now it also features a retractable launch cap,",
        msgCF, centerR, height * 0.3 + msgStep, centerH, centerV).enabledBetween(4.5, 10),
      TextMessage("where SN1 would lift off from.",
        msgCF, centerR, height * 0.3 + 2 * msgStep, centerH, centerV).enabledBetween(4.5, 10),

      ProfessionallyFitImage(ProfessionallyFitImage.ScalingProvider(img2, t => math.max(1, 1 + (t - 11) / 200)),
        minXFun = PiecewiseLinearFunction(10.5, width) to (11, (1 - ar2) * width) to (20.5, (1 - ar2) * width) to (21, width),
        maxXFun = t => width,
        minYFun = t => 0, maxYFun = t => height),

      TextMessage("In the open position,",
        msgCF, centerL, height * 0.3, centerH, centerV).enabledBetween(12, 20),
      TextMessage("the launch cap becomes what it was:",
        msgCF, centerL, height * 0.3 + msgStep, centerH, centerV).enabledBetween(12, 20),
      TextMessage("the landing wall, which makes it easier",
        msgCF, centerL, height * 0.3 + 2 * msgStep, centerH, centerV).enabledBetween(15, 20),
      TextMessage("to land SN1 into the cage,",
        msgCF, centerL, height * 0.3 + 3 * msgStep, centerH, centerV).enabledBetween(15, 20),
      TextMessage("especially flying FPV.",
        msgCF, centerL, height * 0.3 + 4 * msgStep, centerH, centerV).enabledBetween(15, 20),

      ProfessionallyFitImage(ProfessionallyFitImage.ScalingProvider(img3, t => math.max(1, 1 + (t - 21) / 200)),
        minXFun = t => 0,
        maxXFun = PiecewiseLinearFunction(20.5, 0) to (21, ar2 * width) to (30.5, ar2 * width) to (31, 0),
        minYFun = t => 0, maxYFun = t => height),

      TextMessage("But now it is mounted to the cage",
        msgCF, centerR, height * 0.25, centerH, centerV).enabledBetween(22, 30),
      TextMessage("using the ultimate high-tech method:",
        msgCF, centerR, height * 0.25 + msgStep, centerH, centerV).enabledBetween(22, 30),
      TextMessage("the rubber bands!",
        msgCF, centerR, height * 0.25 + 2 * msgStep, centerH, centerV).enabledBetween(22, 30),
      TextMessage("The assembly features an assortment",
        msgCF, centerR, height * 0.25 + 3.5 * msgStep, centerH, centerV).enabledBetween(25, 30),
      TextMessage("of mount points, so that it can accommodate",
        msgCF, centerR, height * 0.25 + 4.5 * msgStep, centerH, centerV).enabledBetween(25, 30),
      TextMessage("different rubber bands. Future-proof!",
        msgCF, centerR, height * 0.25 + 5.5 * msgStep, centerH, centerV).enabledBetween(25, 30),

      ProfessionallyFitImage(ProfessionallyFitImage.ScalingProvider(img4, t => math.max(1, 1 + (t - 31) / 200)),
        minXFun = PiecewiseLinearFunction(30.5, width) to (31, (1 - ar2) * width),
        maxXFun = t => width,
        minYFun = t => 0, maxYFun = t => height),

      TextMessage("In the launch configuration,",
        msgCF, centerL, height * 0.25, centerH, centerV).enabledBetween(32, 41),
      TextMessage("the weight of the craft is bigger",
        msgCF, centerL, height * 0.25 + msgStep, centerH, centerV).enabledBetween(32, 41),
      TextMessage("than the tension of the rubber bands,",
        msgCF, centerL, height * 0.25 + 2 * msgStep, centerH, centerV).enabledBetween(32, 41),
      TextMessage("but once it lifts off, the cap closes.",
        msgCF, centerL, height * 0.25 + 3 * msgStep, centerH, centerV).enabledBetween(32, 41),
      TextMessage("The entire assembly is removable,",
        msgCF, centerL, height * 0.25 + 4.5 * msgStep, centerH, centerV).enabledBetween(36, 41),
      TextMessage("and the old landing wall configuration",
        msgCF, centerL, height * 0.25 + 5.5 * msgStep, centerH, centerV).enabledBetween(36, 41),
      TextMessage("can be used just as well.",
        msgCF, centerL, height * 0.25 + 6.5 * msgStep, centerH, centerV).enabledBetween(36, 41),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 41.0, timeOff = 42.0),
    )

    props.runEmpty(42, allGraphics)
  end apply
