package com.github.mbuzdalov.wtf.videos

import java.awt.geom.{Ellipse2D, Line2D, Path2D}
import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Graphics2D, RenderingHints}

import scala.util.chaining.scalaUtilChainingOps

import com.github.mbuzdalov.wtf.misc.EfficiencyLogAnalysis
import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.HorizontalAlignment.{Center as HC, Left as HL, Right as HR}
import com.github.mbuzdalov.wtf.widgets.TextMessage.VerticalAlignment.{Bottom as VB, Center as VC, Top as VT}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_05_23_p1:
  private class TestDescriptor(val name: String, val index: Int, rootDir: String, color: Color,
                               val xOffset: Int, val yOffset: Int, val testType: String, val distance: String):
    private val darkerColor = color.darker().darker()
    def fillColor(time: Double): Color =
      if name == "single"
      then new Color(255, 255, 0, math.max(0, math.min(255, (time - 205) * 128)).toInt)
      else color
    def drawColor(time: Double): Color =
      if name == "single"
      then fillColor(time).darker().darker()
      else darkerColor
    val data: IndexedSeq[EfficiencyLogAnalysis.AlignedRecord] =
      val binLog = s"$rootDir/log-$name.bin"
      val csvLog = s"$rootDir/log-$name.csv"
      EfficiencyLogAnalysis.process(binLog, csvLog, verbose = false)

  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val rootDir = props.property("--log-root")

    val tests = IArray(
      TestDescriptor("single",    0, rootDir, Color.YELLOW, -1, -1, "Single", "0mm"),
      TestDescriptor("open-20mm", 1, rootDir, new Color(0, 255, 0), 0, 0, "open", "20mm"),
      TestDescriptor("open-30mm", 2, rootDir, new Color(30, 230, 0), 0, 1, "open", "30mm"),
      TestDescriptor("open-40mm", 3, rootDir, new Color(60, 200, 0), 0, 2, "open", "40mm"),
      TestDescriptor("open-50mm", 4, rootDir, new Color(90, 170, 0), 0, 3, "open", "50mm"),
      TestDescriptor("open-60mm", 5, rootDir, new Color(110, 130, 0), 0, 4, "open", "60mm"),
      TestDescriptor("duct-20mm", 6, rootDir, new Color(255, 0, 0), 1, 4, "ducted", "20mm"),
      TestDescriptor("duct-30mm", 7, rootDir, new Color(230, 0, 30), -1, -1, "ducted", "30mm"),
      TestDescriptor("duct-40mm", 8, rootDir, new Color(200, 0, 60), 2, 4, "ducted", "40mm"),
      TestDescriptor("duct-50mm", 9, rootDir, new Color(170, 0, 90), 3, 4, "ducted", "50mm"),
      TestDescriptor("duct-60mm", 10, rootDir, new Color(130, 0, 110), 4, 4, "ducted", "60mm"),
      TestDescriptor("dcnd-20mm", 11, rootDir, new Color(0, 0, 255), 5, 0, "inlet", "20mm"),
      TestDescriptor("dcnd-30mm", 12, rootDir, new Color(0, 30, 230), 5, 1, "inlet", "30mm"),
      TestDescriptor("dcnd-40mm", 13, rootDir, new Color(0, 60, 200), 5, 2, "inlet", "40mm"),
      TestDescriptor("dcnd-50mm", 14, rootDir, new Color(0, 90, 170), 5, 3, "inlet", "50mm"),
      TestDescriptor("dcnd-60mm", 15, rootDir, new Color(0, 110, 130), 5, 4, "inlet", "60mm"),
    )

    val plotFontSize = 23f * width / 1280
    val plotCF = TextMessage.ColorFont(plotFontSize, Color.WHITE)
    val thinStroke = new BasicStroke(plotFontSize / 26f)
    val borderStroke = new BasicStroke(plotFontSize / 13f)

    val infoFontSize = 8f * width / 1280
    val infoCF = TextMessage.ColorFont(infoFontSize, Color.BLACK)

    val plotMinX = width * (1.0 / 6 + 1.0 / 18)
    val plotMaxX = width * (5.0 / 6 - 1.0 / 18)
    val plotMinY = height * (3.0 / 5 - 1.0 / 18) // plotMinY is at the bottom
    val plotMaxY = height * (1.0 / 18) // plotMaxY is at the top
    val arrowSize = height * (1.0 / 100)
    val maxPower = 180.0
    val maxThrust = 0.6
    val pointHalfSize = height * 2e-3

    val commentY = height * 3.15 / 5

    val arrowRight = new Path2D.Double() tap: path =>
      path.moveTo(plotMaxX + arrowSize, plotMinY)
      path.lineTo(plotMaxX - arrowSize, plotMinY + arrowSize * 0.6)
      path.lineTo(plotMaxX - arrowSize, plotMinY - arrowSize * 0.6)
      path.closePath()

    val arrowUp = new Path2D.Double() tap: path =>
      path.moveTo(plotMinX, plotMaxY - arrowSize)
      path.lineTo(plotMinX + arrowSize * 0.6, plotMaxY + arrowSize)
      path.lineTo(plotMinX - arrowSize * 0.6, plotMaxY + arrowSize)
      path.closePath()

    val plotter = new GraphicsConsumer:
      private val myImage: BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
      for y <- 0 until height; x <- 0 until width do myImage.setRGB(x, y, 0)

      private val logIndices = Array.ofDim[Int](tests.size)

      override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
        val logTime = time + 2.0

        val defaultStroke = g.getStroke
        g.setStroke(borderStroke)
        // arrows
        g.setColor(plotCF.fontColor)
        g.draw(new Line2D.Double(plotMinX, plotMinY, plotMaxX, plotMinY))
        g.fill(arrowRight)
        g.draw(new Line2D.Double(plotMinX, plotMinY, plotMinX, plotMaxY))
        g.fill(arrowUp)

        TextMessage("kgf", plotCF, plotMinX, plotMaxY - arrowSize * 1.1, HC, VB)
          .consume(img, g, time, frameNo)
        TextMessage("W", plotCF, plotMaxX + arrowSize * 1.1, plotMinY, HL, VC)
          .consume(img, g, time, frameNo)
        TextMessage("0", plotCF, plotMinX, plotMinY, HR, VT)
          .consume(img, g, time, frameNo)

        g.setStroke(thinStroke)
        // horizontal lines
        for thrust <- Seq(0.1, 0.2, 0.3, 0.4, 0.5, 0.6) do
          val y = plotMinY + (plotMaxY - plotMinY) * thrust / maxThrust
          g.draw(new Line2D.Double(plotMinX, y, plotMaxX, y))
          TextMessage(s"$thrust", plotCF, plotMinX - arrowSize, y, HR, VC)
            .consume(img, g, time, frameNo)

        // vertical lines
        for power <- 30 to 180 by 30 do
          val x = plotMinX + (plotMaxX - plotMinX) * power / maxPower
          g.draw(new Line2D.Double(x, plotMinY, x, plotMaxY))
          TextMessage(s"$power", plotCF, x, plotMinY + arrowSize, HC, VT)
            .consume(img, g, time, frameNo)

        g.setStroke(defaultStroke)

        val dotShape = new Ellipse2D.Double() // (some) awt.geom shapes are mutable

        val myGraphics = myImage.createGraphics()
        myGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        myGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        for test <- tests do
          val thresholdTime = if test.name == "single" && time < 205 then -1 else logTime
          val color = test.fillColor(time)
          val perimeterColor = test.drawColor(time)
          val logSeq = test.data

          while logIndices(test.index) < logSeq.size && logSeq(logIndices(test.index)).time <= thresholdTime do
            val rec = logSeq(logIndices(test.index))
            logIndices(test.index) += 1
            val x = plotMinX + (plotMaxX - plotMinX) * (rec.electricPower / maxPower)
            val y = plotMinY + (plotMaxY - plotMinY) * (rec.thrust / maxThrust)
            dotShape.setFrame(x - pointHalfSize, y - pointHalfSize, 2 * pointHalfSize, 2 * pointHalfSize)
            myGraphics.setColor(color)
            myGraphics.fill(dotShape)
            myGraphics.setColor(perimeterColor)
            myGraphics.draw(dotShape)
          end while

          // now draw stats
          if test.xOffset >= 0 then
            val x0 = width / 6.0 * test.xOffset
            val y0 = height / 5.0 * test.yOffset
            ColoredRectangle(x = test.xOffset / 6.0 + 1.0 / 150,
                             y = test.yOffset / 5.0 + 1 / 5.0 - 1 / 40.0,
                             w = 1 / 100.0,
                             h = 1 / 80.0,
                             backgroundColor = test.fillColor(time))
              .enabledBetween(1, 230)
              .consume(img, g, time, frameNo)

            val logRef = logSeq(math.max(0, logIndices(test.index) - 1))
            val pwm = s"PWM: ${logRef.pwm}"
            val thrust = f"Thrust: ${logRef.thrust}%.02f kgf".replace(',', '.') // too lazy to set locale lol
            val power = f"Power: ${logRef.electricPower.toInt} watt"

            TextMessage(s"Test: ${test.testType}", infoCF, x0 + 0.5 * infoFontSize, y0 + 0.5 * infoFontSize, HL, VT)
              .consume(img, g, time, frameNo)
            TextMessage(s"Prop distance: ${test.distance}", infoCF, x0 + 0.5 * infoFontSize, y0 + 1.6 * infoFontSize, HL, VT)
              .consume(img, g, time, frameNo)
            TextMessage(pwm, infoCF, x0 + 0.5 * infoFontSize, y0 + 2.7 * infoFontSize, HL, VT)
              .consume(img, g, time, frameNo)
            TextMessage(power, infoCF, x0 + 0.5 * infoFontSize, y0 + 3.8 * infoFontSize, HL, VT)
              .consume(img, g, time, frameNo)
            TextMessage(thrust, infoCF, x0 + 0.5 * infoFontSize, y0 + 4.9 * infoFontSize, HL, VT)
              .consume(img, g, time, frameNo)
          end if

          if time < 207 then   // single's color changes in time [250;252], so we must reset this
            logIndices(0) = 0  // quite a dirty hack but it's probably most efficient
        end for

        myGraphics.dispose()
        g.drawImage(myImage, 0, 0, null)
      end consume

    val allGraphics = GraphicsConsumer.compose(
      ColoredRectangle(0, 1.0 / 5 - 3e-4, 1, 6e-4, Color.BLACK),
      ColoredRectangle(0, 2.0 / 5 - 3e-4, 1, 6e-4, Color.BLACK),
      ColoredRectangle(0, 3.0 / 5 - 3e-4, 1, 6e-4, Color.BLACK),
      ColoredRectangle(0, 4.0 / 5 - 3e-4, 1, 6e-4, Color.BLACK),
      ColoredRectangle(1.0 / 6 - 3e-4, 0, 6e-4, 1, Color.BLACK),
      ColoredRectangle(2.0 / 6 - 3e-4, 0, 6e-4, 1, Color.BLACK),
      ColoredRectangle(3.0 / 6 - 3e-4, 0, 6e-4, 1, Color.BLACK),
      ColoredRectangle(4.0 / 6 - 3e-4, 0, 6e-4, 1, Color.BLACK),
      ColoredRectangle(5.0 / 6 - 3e-4, 0, 6e-4, 1, Color.BLACK),

      plotter,

      TextMessage("The motors are driven by the Lua script on the flight controller.",
        plotCF, 0.5 * width, commentY, HC, VC).enabledBetween(10, 19),
      TextMessage("The script simply tells the ESCs the PWM values to use on the motors:",
        plotCF, 0.5 * width, commentY + 1.5 * plotFontSize, HC, VC).enabledBetween(10, 19),
      TextMessage("they smoothly increase from 1020 to 1600 and then go back.",
        plotCF, 0.5 * width, commentY + 3.0 * plotFontSize, HC, VC).enabledBetween(10, 19),

      TextMessage("10 times per second, this stand logs thrust measurements",
        plotCF, 0.5 * width, commentY + 0.7 * plotFontSize, HC, VC).enabledBetween(20, 29),
      TextMessage("using two load cells and an OrangePi talking to them.",
        plotCF, 0.5 * width, commentY + 2.2 * plotFontSize, HC, VC).enabledBetween(20, 29),

      TextMessage("At least 10 times per second, the flight controller",
        plotCF, 0.5 * width, commentY, HC, VC).enabledBetween(30, 39),
      TextMessage("also logs: battery voltage and current, PWM output commanded,",
        plotCF, 0.5 * width, commentY + 1.5 * plotFontSize, HC, VC).enabledBetween(30, 39),
      TextMessage("and rotation speed of each propeller via ESC telemetry.",
        plotCF, 0.5 * width, commentY + 3.0 * plotFontSize, HC, VC).enabledBetween(30, 39),

      TextMessage("Here, we plot measured thrust versus electric power:",
        plotCF, 0.5 * width, commentY, HC, VC).enabledBetween(40, 59),

      TextMessage("Green", TextMessage.ColorFont(plotCF.fontSize, new Color(60, 200, 0)),
        0.35 * width, commentY + 1.5 * plotFontSize, HR, VC).enabledBetween(43, 59),
      TextMessage("-shaded points are for open runs,",
        plotCF, 0.35 * width, commentY + 1.5 * plotFontSize, HL, VC).enabledBetween(44, 59),
      TextMessage("Red", TextMessage.ColorFont(plotCF.fontSize, new Color(200, 0, 60)),
        0.35 * width, commentY + 2.6 * plotFontSize, HR, VC).enabledBetween(46, 59),
      TextMessage("-shaded points are for ducted runs,",
        plotCF, 0.35 * width, commentY + 2.6 * plotFontSize, HL, VC).enabledBetween(47, 59),
      TextMessage("Blue", TextMessage.ColorFont(plotCF.fontSize, new Color(0, 60, 200)),
        0.35 * width, commentY + 3.7 * plotFontSize, HR, VC).enabledBetween(49, 59),
      TextMessage("-shaded points are for inlet runs.",
        plotCF, 0.35 * width, commentY + 3.7 * plotFontSize, HL, VC).enabledBetween(50, 59),

      TextMessage("One can see that inlet setups need same power",
        plotCF, 0.5 * width, commentY, HC, VC).enabledBetween(70, 84),
      TextMessage("as ducted setups at same PWM, but produce more thrust.",
        plotCF, 0.5 * width, commentY + 1.2 * plotFontSize, HC, VC).enabledBetween(70, 84),
      TextMessage("Open setups produce same thrust as inlets at same PWM,",
        plotCF, 0.5 * width, commentY + 2.7 * plotFontSize, HC, VC).enabledBetween(75, 84),
      TextMessage("but need more power for that. Overall, duct < open < inlet.",
        plotCF, 0.5 * width, commentY + 3.9 * plotFontSize, HC, VC).enabledBetween(75, 84),

      TextMessage("If we fix the output power at 90W,",
        plotCF, 0.5 * width, commentY, HC, VC).enabledBetween(90, 105),
      TextMessage("open setups (0.355 kgf) produce 10% more thrust than ducted (0.323 kgf),",
        plotCF, 0.5 * width, commentY + 1.5 * plotFontSize, HC, VC).enabledBetween(90, 105),
      TextMessage("but inlet setups (0.396 kgf) produce 11% more thrust than open.",
        plotCF, 0.5 * width, commentY + 3.0 * plotFontSize, HC, VC).enabledBetween(90, 105),
      GlowingCircle(92, 1, 103,
        plotMinX + (plotMaxX - plotMinX) * (90.0 / maxPower),
        plotMinY + (plotMaxY - plotMinY) * (0.355 / maxThrust),
        1e-2 * height, 1e-2 * height, 2e-3 * height, Color.YELLOW),
      GlowingCircle(92, 1, 103,
        plotMinX + (plotMaxX - plotMinX) * (90.0 / maxPower),
        plotMinY + (plotMaxY - plotMinY) * (0.323 / maxThrust),
        1e-2 * height, 1e-2 * height, 2e-3 * height, Color.YELLOW),
      GlowingCircle(92, 1, 103,
        plotMinX + (plotMaxX - plotMinX) * (90.0 / maxPower),
        plotMinY + (plotMaxY - plotMinY) * (0.396 / maxThrust),
        1e-2 * height, 1e-2 * height, 2e-3 * height, Color.YELLOW),

      TextMessage("If we fix the thrust at 0.4 kgf,",
        plotCF, 0.5 * width, commentY, HC, VC).enabledBetween(110, 125),
      TextMessage("open setups (107 W) require 13% less power than ducted (123 W),",
        plotCF, 0.5 * width, commentY + 1.5 * plotFontSize, HC, VC).enabledBetween(110, 125),
      TextMessage("but inlet setups (91 W) require 15% less power than open.",
        plotCF, 0.5 * width, commentY + 3.0 * plotFontSize, HC, VC).enabledBetween(110, 125),
      GlowingCircle(112, 1, 123,
        plotMinX + (plotMaxX - plotMinX) * (107.0 / maxPower),
        plotMinY + (plotMaxY - plotMinY) * (0.4 / maxThrust),
        1e-2 * height, 1e-2 * height, 2e-3 * height, Color.YELLOW),
      GlowingCircle(112, 1, 123,
        plotMinX + (plotMaxX - plotMinX) * (123.0 / maxPower),
        plotMinY + (plotMaxY - plotMinY) * (0.4 / maxThrust),
        1e-2 * height, 1e-2 * height, 2e-3 * height, Color.YELLOW),
      GlowingCircle(112, 1, 123,
        plotMinX + (plotMaxX - plotMinX) * (91.0 / maxPower),
        plotMinY + (plotMaxY - plotMinY) * (0.4 / maxThrust),
        1e-2 * height, 1e-2 * height, 2e-3 * height, Color.YELLOW),

      TextMessage("As for the distance between the propellers,",
        plotCF, 0.5 * width, commentY, HC, VC).enabledBetween(130, 145),
      TextMessage("the difference is mostly negligible, but for open and ducted setups",
        plotCF, 0.5 * width, commentY + 1.5 * plotFontSize, HC, VC).enabledBetween(130, 145),
      TextMessage("smaller distance is marginally more efficient (check colors around 150W).",
        plotCF, 0.5 * width, commentY + 3.0 * plotFontSize, HC, VC).enabledBetween(130, 145),
      GlowingCircle(130, 1, 145,
        plotMinX + (plotMaxX - plotMinX) * (150.0 / maxPower),
        plotMinY + (plotMaxY - plotMinY) * (0.45 / maxThrust),
        1.2e-2 * height, 1.2e-2 * height, 2e-3 * height, Color.YELLOW),
      GlowingCircle(130, 1, 145,
        plotMinX + (plotMaxX - plotMinX) * (150.0 / maxPower),
        plotMinY + (plotMaxY - plotMinY) * (0.505 / maxThrust),
        1.2e-2 * height, 1.2e-2 * height, 2e-3 * height, Color.YELLOW),

      TextMessage("Overall, the message from this data seems clear:",
        plotCF, 0.5 * width, commentY, HC, VC).enabledBetween(160, 180),
      TextMessage("at least with these propellers, everything is better than simple flat ducts.",
        plotCF, 0.5 * width, commentY + 1.2 * plotFontSize, HC, VC).enabledBetween(160, 180),
      TextMessage("If you want something very lightweight, choose an open setup.",
        plotCF, 0.5 * width, commentY + 2.7 * plotFontSize, HC, VC).enabledBetween(164, 180),
      TextMessage("For max efficiency, at the cost of more weight, use an inlet.",
        plotCF, 0.5 * width, commentY + 3.9 * plotFontSize, HC, VC).enabledBetween(168, 180),

      TextMessage("Big inlets induce a sort of Coandă effect though,",
        plotCF, 0.5 * width, commentY, HC, VC).enabledBetween(185, 195),
      TextMessage("which induces attitude control problems when flying into the wind.",
        plotCF, 0.5 * width, commentY + 1.5 * plotFontSize, HC, VC).enabledBetween(185, 195),
      TextMessage("This does not happen with open setups, so choose wisely.",
        plotCF, 0.5 * width, commentY + 3.0 * plotFontSize, HC, VC).enabledBetween(188, 195),

      TextMessage("The new yellow plot is for the inlet setup with only one propeller.",
        plotCF, 0.5 * width, commentY, HC, VC).enabledBetween(205, 225),
      TextMessage("This way we can measure the direct penalty of using coaxial powertrain.",
        plotCF, 0.5 * width, commentY + 1.5 * plotFontSize, HC, VC).enabledBetween(208, 225),
      TextMessage("It appears to be within 5-7%. Noticeable but definitely not detrimental.",
        plotCF, 0.5 * width, commentY + 3.0 * plotFontSize, HC, VC).enabledBetween(212, 225),

      TextMessage("Hope you found this data useful, but remember that these conclusions",
        plotCF, 0.5 * width, commentY, HC, VC).enabledBetween(240, 259),
      TextMessage("drawn for these specific propellers (HQProp Toothpick Durable, 5030),",
        plotCF, 0.5 * width, commentY + 1.5 * plotFontSize, HC, VC).enabledBetween(240, 259),
      TextMessage("so your mileage may vary.",
        plotCF, 0.5 * width, commentY + 3.0 * plotFontSize, HC, VC).enabledBetween(240, 259),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 259.5, timeOff = 260.0),
    )

    props.runVideo(allGraphics)
  end apply
