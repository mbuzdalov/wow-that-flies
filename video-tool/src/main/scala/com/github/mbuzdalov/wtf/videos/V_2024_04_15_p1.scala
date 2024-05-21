package com.github.mbuzdalov.wtf.videos

import java.awt.geom.Path2D
import java.awt.{BasicStroke, Color, Font, Graphics2D}
import java.awt.image.BufferedImage

import scala.compiletime.uninitialized

import com.github.mbuzdalov.wtf.widgets.Fade
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}


object V_2024_04_15_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val allGraphics = GraphicsConsumer.compose(
      coaxPainter(width),
      Fade(timeOff = 20.0, timeOn = 19.5),
    )
    props.runEmpty(20, allGraphics)
  end apply

  private def bound(min: Double, max: Double, value: Double): Double = math.min(max, math.max(min, value))

  private def coaxPainter(width: Int): GraphicsConsumer = new GraphicsConsumer:
    private val timeRotationStarts = 2
    private val timeAngleChangeStarts = 5
    private val timeEnlargementStarts = 10

    private val background = new Color(120, 110, 100)
    private val legColor = new Color(20, 20, 150)
    private val ductColor = new Color(20, 20, 150, 100)
    private val flapColor = new Color(240, 220, 200)
    private val forceColor = new Color(150, 20, 10)
    private val motorColor = new Color(150, 170, 40)
    private val propColor = new Color(150, 150, 150)
    private val propOutlineColor = new Color(75, 75, 75)
    private var lastMainFont, lastSubFont: Font = uninitialized
    private var propAngleTop = -math.Pi / 9
    private var propAngleBot = -math.Pi / 9

    private val thinStroke = new BasicStroke(width * 1e-3f)
    private val propOutlineStroke = new BasicStroke(width * 2e-3f)

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

    private def drawFRoll(g: Graphics2D, fontSize: Float, baseX: Double, baseY: Double): Unit =
      if lastMainFont == null then lastMainFont = new Font(Font.SANS_SERIF, Font.PLAIN, (fontSize + 0.5).toInt)
      if lastSubFont == null then lastSubFont = new Font(Font.SANS_SERIF, Font.PLAIN, (fontSize * 0.7 + 0.5).toInt)
      g.setFont(lastMainFont)
      g.drawString("F", baseX.toFloat, baseY.toFloat)
      g.setFont(lastSubFont)
      g.drawString("roll", (baseX + fontSize / 2).toFloat, (baseY + fontSize / 5).toFloat)

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
        g.setStroke(new BasicStroke(width * 2e-3f * (1 - math.max(math.abs(sin1), math.abs(sin2))).toFloat))
        if increasing == (sin1 < sin2)
        then g.drawLine((x + sin1 * innerR).toInt, botY, (x + sin2 * innerR).toInt, topY)
        else g.drawLine((x - sin1 * innerR).toInt, botY, (x - sin2 * innerR).toInt, topY)

      g.setStroke(thinStroke)

    override def consume(img: BufferedImage, g: Graphics2D, time: Double, frameNo: Long): Unit =
      val w = img.getWidth
      val h = img.getHeight
      g.setColor(background)
      g.fillRect(0, 0, w, h)
      g.setStroke(thinStroke)

      val droneXLeft = 5 * w / 8
      val droneXRight = 7 * w / 8
      val droneXMid = (droneXLeft + droneXRight) / 2
      val droneYTop = h / 8
      val droneYDuctBot = 3 * h / 8
      val droneYLegBot = 9 * h / 16
      val motorWidth = (droneXRight - droneXLeft) / 5
      val legW = (droneXRight - droneXLeft) / 20
      val flapW = legW / 2
      val motorHeight = (droneYDuctBot - droneYTop - 2 * legW) / 4
      val propHeight = 3 * motorHeight / 4
      val droneYFlapTop = droneYDuctBot + 2 * flapW
      val droneYFlapSpace = droneYLegBot - droneYFlapTop

      g.setColor(motorColor)
      g.fillRect(droneXMid - motorWidth / 2, droneYTop + legW, motorWidth, motorHeight)
      g.fillRect(droneXMid - motorWidth / 2, droneYDuctBot - motorHeight - legW, motorWidth, motorHeight)

      propAngleTop += math.Pi / 9 * bound(0, 1, (time - timeRotationStarts) / 2)
      propAngleBot += math.Pi / 8.2 * bound(0, 1, (time - timeRotationStarts) / 2.1)

      val propR = (droneXRight - droneXLeft) / 2 - legW
      val propAngleSinTA = math.sin(propAngleTop)
      val propAngleSinTB = math.sin(propAngleTop + math.Pi / 5)
      val propAngleSinBA = math.sin(propAngleBot)
      val propAngleSinBB = math.sin(propAngleBot + math.Pi / 5)
      val topPropY = droneYTop + legW + motorHeight
      val botPropY = droneYDuctBot - legW - motorHeight

      drawPropeller(g, droneXMid, topPropY, topPropY + propHeight, propR, propAngleSinTA, propAngleSinTB, increasing = true)
      drawPropeller(g, droneXMid, botPropY - propHeight, botPropY, propR, propAngleSinBA, propAngleSinBB, increasing = false)

      g.setColor(ductColor)
      g.fillRect(droneXLeft, droneYTop, droneXRight - droneXLeft, droneYDuctBot - droneYTop)

      g.setColor(legColor)
      g.fillRect(droneXLeft, droneYTop, legW, droneYLegBot - droneYTop)
      g.fillRect(droneXRight - legW, droneYTop, legW, droneYLegBot - droneYTop)

      val angle = bound(0, math.Pi / 10, (time - timeAngleChangeStarts) / 2.0)
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
        g.setColor(forceColor)
        g.fill(rightArrow(midX, midY, forceLength, flapW / 2))
        drawFRoll(g, 24f * img.getWidth / 1280, midX + forceLength + 3 * (flapW / 2), midY)
    end consume
  end coaxPainter
end V_2024_04_15_p1
