package com.github.mbuzdalov.wtf
import java.awt.{BorderLayout, Frame, Graphics}
import java.awt.image.BufferedImage
import javax.swing.{JFrame, JPanel, SwingUtilities, WindowConstants}

import scala.compiletime.uninitialized

class Player extends FrameConsumer:
  private var img: BufferedImage = uninitialized
  private var lastTime: Double = Double.NaN
  private var panel: JPanel = uninitialized

  SwingUtilities.invokeAndWait(() => setupPlayer())

  private class ImageDrawingPanel extends JPanel(true):
    override def paint(g: Graphics): Unit =
      if img != null then synchronized(g.drawImage(img, 0, 0, getWidth, getHeight, this))

  private def setupPlayer(): Unit =
    val frame = new JFrame("WTF Player")
    frame.setSize(1920, 1080)
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    frame.setExtendedState(Frame.MAXIMIZED_BOTH)
    frame.setLayout(new BorderLayout())
    frame.setVisible(true)
    panel = new ImageDrawingPanel
    frame.add(panel)

  override def consume(img: BufferedImage, time: Double, frameNo: Long): Unit =
    val currTime = System.nanoTime() * 1e-9
    if lastTime.isNaN then
      lastTime = currTime
    else if currTime < lastTime + time then
      Thread.sleep(((lastTime + time - currTime) * 1e3).toInt)
      lastTime = System.nanoTime() * 1e-9
    if this.img == null then
      this.img = new BufferedImage(img.getWidth, img.getHeight, img.getType)
    synchronized(img.copyData(this.img.getRaster))
    this.panel.repaint()
end Player
