package com.github.mbuzdalov.wtf.videos

import java.awt.geom.Path2D
import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Font, Graphics2D}

import scala.compiletime.uninitialized
import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}


object V_2024_04_15_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))
    val h0 = height * 0.05f
    val w0 = width * 0.01f

    val allGraphics = GraphicsConsumer.compose(
      coaxPainter(width, height),
      TextMessage("Let's see how flaps work.",
        msgCF, w0, h0, HA.Left, VA.Center)
        .enabledBetween(1, 20),
      TextMessage("The roll torque (same for pitch) is equal",
        msgCF, w0, h0 + msgStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(4, 20),
      TextMessage("to the product of the projection of",
        msgCF, w0, h0 + msgStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(4, 20),
      TextMessage("the force on the flap and the moment arm.",
        msgCF, w0, h0 + msgStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(4, 20),
      TextMessage("The bigger the angle, the bigger the force.",
        msgCF, w0, h0 + msgStep * 5f, HA.Left, VA.Center)
        .enabledBetween(8, 20),
      TextMessage("But the angle has the limit, so there is",
        msgCF, w0, h0 + msgStep * 6f, HA.Left, VA.Center)
        .enabledBetween(8, 20),
      TextMessage("the maximum force one can get from a flap.",
        msgCF, w0, h0 + msgStep * 7f, HA.Left, VA.Center)
        .enabledBetween(8, 20),
      TextMessage("If the force is not enough, we can increase the flap!",
        msgCF, w0, h0 + msgStep * 8.5f, HA.Left, VA.Center)
        .enabledBetween(12, 20),
      TextMessage("The bigger the flap, the bigger the force at the same angle,",
        msgCF, w0, h0 + msgStep * 9.5f, HA.Left, VA.Center)
        .enabledBetween(12, 20),
      TextMessage("and the arm also increases. So the attitude control can be better!",
        msgCF, w0, h0 + msgStep * 10.5f, HA.Left, VA.Center)
        .enabledBetween(12, 20),

      Fade(timeOff = 20.0, timeOn = 19.5),
    )
    props.runEmpty(20, allGraphics)
  end apply

  private def bound(min: Double, max: Double, value: Double): Double = math.min(max, math.max(min, value))

  private def coaxPainter(width: Int, height: Int): GraphicsConsumer = new GraphicsConsumer:
    private val timeRotationStarts = 2
    private val timeAngleChangeStarts = 7
    private val timeEnlargementStarts = 12

    private val droneXLeft = 5 * width / 8
    private val droneXRight = 15 * width / 16
    private val droneXMid = (droneXLeft + droneXRight) / 2
    private val droneYTop = height / 8
    private val droneYDuctBot = 3 * height / 8
    private val droneYLegBot = 9 * height / 16
    private val motorWidth = (droneXRight - droneXLeft) / 5
    private val legW = (droneXRight - droneXLeft) / 20
    private val flapW = legW / 2
    private val motorHeight = (droneYDuctBot - droneYTop - 2 * legW) / 4
    private val propHeight = 3 * motorHeight / 4
    private val droneYFlapTop = droneYDuctBot + 2 * flapW
    private val droneYFlapSpace = droneYLegBot - droneYFlapTop
    private val propR = (droneXRight - droneXLeft) / 2 - legW
    private val topPropY = droneYTop + legW + motorHeight
    private val botPropY = droneYDuctBot - legW - motorHeight

    private val background = new Color(120, 110, 100)
    private val legColor = new Color(20, 20, 150)
    private val ductColor = new Color(20, 20, 150, 100)
    private val flapColor = new Color(240, 220, 200)
    private val forceColor = new Color(80, 10, 5)
    private val armColor = new Color(100, 20, 90)
    private val motorColor = new Color(150, 170, 40)
    private val propColor = new Color(150, 150, 150)
    private val propOutlineColor = new Color(75, 75, 75)
    private var lastMainFont, lastSubFont: Font = uninitialized
    private var propAngleTop = -math.Pi / 9
    private var propAngleBot = -math.Pi / 9

    private val thinStroke = new BasicStroke(width * 1e-3f)
    private val propOutlineStroke = new BasicStroke(width * 2e-3f)
    private val dashedStroke = new BasicStroke(width * 1.5e-3f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, Array(width * 1e-2f), 0.0f)

    private def line(x1: Double, y1: Double, x2: Double, y2: Double): Path2D.Double =
      val shape = new Path2D.Double()
      shape.moveTo(x1, y1)
      shape.lineTo(x2, y2)
      shape

    private def rightArrow(baseX: Double, baseY: Double, length: Double, halfWidth: Double): Path2D.Double =
      val shape = new Path2D.Double()
      shape.moveTo(baseX - halfWidth, baseY - halfWidth)
      shape.lineTo(baseX + length, baseY - halfWidth)
      shape.lineTo(baseX + length, baseY - 3 * halfWidth)
      shape.lineTo(baseX + length + 3 * halfWidth, baseY)
      shape.lineTo(baseX + length, baseY + 3 * halfWidth)
      shape.lineTo(baseX + length, baseY + halfWidth)
      shape.lineTo(baseX - halfWidth, baseY + halfWidth)
      shape.closePath()
      shape

    private def bdArrow(x1: Double, y1: Double, x2: Double, y2: Double, width: Double): Path2D.Double =
      val hp = math.hypot(x1 - x2, y1 - y2)
      val dx = (x2 - x1) / hp
      val dy = (y2 - y1) / hp
      val shape = new Path2D.Double()
      shape.moveTo(x1, y1)
      val lx = 3
      val ly = 2
      shape.lineTo(x1 + lx * dx * width + ly * dy * width, y1 + lx * dy * width - ly * dx * width)
      shape.lineTo(x1 + lx * dx * width + 0.5 * dy * width, y1 + lx * dy * width - 0.5 * dx * width)
      shape.lineTo(x2 - lx * dx * width + 0.5 * dy * width, y2 - lx * dy * width - 0.5 * dx * width)
      shape.lineTo(x2 - lx * dx * width + ly * dy * width, y2 - lx * dy * width - ly * dx * width)
      shape.lineTo(x2, y2)
      shape.lineTo(x2 - lx * dx * width - ly * dy * width, y2 - lx * dy * width + ly * dx * width)
      shape.lineTo(x2 - lx * dx * width - 0.5 * dy * width, y2 - lx * dy * width + 0.5 * dx * width)
      shape.lineTo(x1 + lx * dx * width - 0.5 * dy * width, y1 + lx * dy * width + 0.5 * dx * width)
      shape.lineTo(x1 + lx * dx * width - ly * dy * width, y1 + lx * dy * width + ly * dx * width)
      shape.closePath()
      shape

    private def drawFRoll(g: Graphics2D, fontSize: Float, baseX: Double, baseY: Double): Unit =
      if lastMainFont == null then lastMainFont = new Font(Font.SANS_SERIF, Font.PLAIN, (fontSize + 0.5).toInt)
      if lastSubFont == null then lastSubFont = new Font(Font.SANS_SERIF, Font.PLAIN, (fontSize * 0.7 + 0.5).toInt)
      g.setFont(lastMainFont)
      g.drawString("F", baseX.toFloat, baseY.toFloat)
      g.setFont(lastSubFont)
      g.drawString("roll", (baseX + fontSize / 2).toFloat, (baseY + fontSize / 5).toFloat)

    private def drawLArm(g: Graphics2D, fontSize: Float, baseX: Double, baseY: Double): Unit =
      if lastMainFont == null then lastMainFont = new Font(Font.SANS_SERIF, Font.PLAIN, (fontSize + 0.5).toInt)
      if lastSubFont == null then lastSubFont = new Font(Font.SANS_SERIF, Font.PLAIN, (fontSize * 0.7 + 0.5).toInt)
      g.setFont(lastMainFont)
      g.drawString("L", baseX.toFloat, baseY.toFloat)
      g.setFont(lastSubFont)
      g.drawString("arm", (baseX + fontSize / 2).toFloat, (baseY + fontSize / 5).toFloat)

    private def drawPropeller(g: Graphics2D, x: Int, topY: Int, botY: Int, r: Double, sin1: Double, sin2: Double, increasing: Boolean): Unit =
      val innerR = (botY - topY) / 2
      g.setColor(propColor)
      g.fillRect(x - innerR, topY, 2 * innerR + 1, 2 * innerR)
      val x11 = x + sin1 * r
      val x12 = x + sin2 * r
      val pyLow = botY * 0.8 + topY * 0.2
      val pyHi = botY * 0.2 + topY * 0.8
      val shape12 = new Path2D.Double()
      shape12.moveTo(x11, pyLow)
      shape12.curveTo(x11, pyLow * 0.5 + pyHi * 0.5, x11 * 0.5 + x12 * 0.5, pyHi, x12, pyHi)
      val shape12x = new Path2D.Double(shape12)
      shape12x.lineTo(x, topY)
      shape12x.lineTo(x, botY)
      shape12x.closePath()

      val x21 = x - sin1 * r
      val x22 = x - sin2 * r
      val shape21 = new Path2D.Double()
      shape21.moveTo(x21, pyLow)
      shape21.curveTo(x21, pyLow * 0.5 + pyHi * 0.5, x21 * 0.5 + x22 * 0.5, pyHi, x22, pyHi)
      val shape21x = new Path2D.Double(shape21)
      shape21x.lineTo(x, topY)
      shape21x.lineTo(x, botY)
      shape21x.closePath()

      g.fill(shape12x)
      g.fill(shape21x)

      g.setStroke(propOutlineStroke)
      g.setColor(propOutlineColor)
      if increasing == (sin1 < sin2) then g.draw(shape12) else g.draw(shape21)

      if math.abs(sin1 - sin2) > 1e-9 then
        g.setStroke(new BasicStroke(width * 2e-3f * math.sqrt(1 - math.max(math.abs(sin1), math.abs(sin2))).toFloat))
        if increasing == (sin1 < sin2)
        then g.draw(line(x + sin1 * innerR, botY, x + sin2 * innerR, topY))
        else g.draw(line(x - sin1 * innerR, botY, x - sin2 * innerR, topY))

      g.setStroke(thinStroke)

    override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
      assert(width == img.getWidth)
      assert(height == img.getHeight)
      g.setColor(background)
      g.fillRect(0, 0, width, height)
      g.setStroke(thinStroke)

      g.setColor(motorColor)
      g.fillRect(droneXMid - motorWidth / 2, droneYTop + legW, motorWidth, motorHeight)
      g.fillRect(droneXMid - motorWidth / 2, droneYDuctBot - motorHeight - legW, motorWidth, motorHeight)

      propAngleTop += math.Pi / 9 * bound(0, 1, (time - timeRotationStarts) / 2)
      propAngleBot += math.Pi / 8.2 * bound(0, 1, (time - timeRotationStarts) / 2.1)

      val propAngleSinTA = math.sin(propAngleTop)
      val propAngleSinTB = math.sin(propAngleTop + math.Pi / 5)
      val propAngleSinBA = math.sin(propAngleBot)
      val propAngleSinBB = math.sin(propAngleBot + math.Pi / 5)

      drawPropeller(g, droneXMid, topPropY, topPropY + propHeight, propR, propAngleSinTA, propAngleSinTB, increasing = true)
      drawPropeller(g, droneXMid, botPropY - propHeight, botPropY, propR, propAngleSinBA, propAngleSinBB, increasing = false)

      g.setColor(ductColor)
      g.fillRect(droneXLeft, droneYTop, droneXRight - droneXLeft, droneYDuctBot - droneYTop)

      g.setColor(legColor)
      g.fillRect(droneXLeft, droneYTop, legW, droneYLegBot - droneYTop)
      g.fillRect(droneXRight - legW, droneYTop, legW, droneYLegBot - droneYTop)

      val angle = math.Pi / 10 * bound(0, 1, (time - timeAngleChangeStarts) / 4.5)
      val sinA = math.sin(angle)
      val cosA = math.cos(angle)
      val flapLength = (droneYFlapSpace * bound(0.7, 0.98, (time - timeEnlargementStarts) * 0.3)).toInt

      g.setColor(flapColor)
      val origTransform = g.getTransform
      g.rotate(angle, droneXMid, droneYFlapTop + droneYFlapSpace * 0.35)
      g.fillRect(droneXMid - flapW / 2, droneYFlapTop, flapW, flapLength)
      g.setTransform(origTransform)

      val virtualMountW = (flapW * 3) / 4
      g.setColor(Color.BLACK)
      g.fillOval(droneXMid - virtualMountW, (droneYFlapTop + droneYFlapSpace * 0.35).toInt - virtualMountW,
        2 * virtualMountW + 1, 2 * virtualMountW + 1)

      g.setColor(legColor)
      g.drawRect((droneXRight + droneXLeft - legW) / 2, droneYTop, legW, droneYLegBot - droneYTop)
      g.drawRect(droneXLeft, droneYTop, droneXRight - droneXLeft, legW)
      g.drawRect(droneXLeft, droneYDuctBot - legW, droneXRight - droneXLeft, legW)

      val forceLength = sinA * flapLength
      if time >= timeAngleChangeStarts && forceLength > flapW then
        val excLengthDown = flapLength / 2.0 - droneYFlapSpace * 0.35
        val midY = droneYFlapTop + droneYFlapSpace * 0.35 + excLengthDown * math.cos(angle)
        val midX = droneXMid - excLengthDown * math.sin(angle)

        // arm drawing
        val otherX = droneXMid
        val otherY = (droneYDuctBot + droneYTop) / 2.0
        val hp = math.hypot(midX - otherX, midY - otherY)
        val dx = (otherX - midX) / hp
        val dy = (midY - otherY) / hp

        val moveLeft = (droneXMid - droneXLeft) * 1.3
        val topArmX = otherX - moveLeft * dy
        val topArmY = otherY - moveLeft * dx
        val botArmX = midX - moveLeft * dy
        val botArmY = midY - moveLeft * dx

        val forceFontSize = 24f * img.getWidth / 1280
        g.setColor(armColor)
        g.setStroke(dashedStroke)
        g.draw(line(topArmX, topArmY, otherX, otherY))
        g.draw(line(botArmX, botArmY, midX, midY))
        g.fill(bdArrow(topArmX, topArmY, botArmX, botArmY, width * 2e-3f))
        g.setStroke(thinStroke)
        drawLArm(g, forceFontSize, (topArmX + botArmX) / 2.0 + forceFontSize * 0.2, (topArmY + botArmY) / 2.0)

        g.setColor(forceColor)
        g.fill(rightArrow(midX, midY, forceLength, flapW / 2))
        drawFRoll(g, forceFontSize, midX + forceLength + 3 * (flapW / 2), midY)
    end consume
  end coaxPainter
end V_2024_04_15_p1
