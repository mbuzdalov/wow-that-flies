package com.github.mbuzdalov.wtf

import java.awt.{Graphics2D, RenderingHints}

object GraphicsUtils:
  def setHints(g: Graphics2D): Unit =
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    