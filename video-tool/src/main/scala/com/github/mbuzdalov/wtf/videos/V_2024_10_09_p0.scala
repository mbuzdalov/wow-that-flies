package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.util.PiecewiseLinearFunction
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_10_09_p0:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(240, 240, 0), new Color(0, 0, 0, 120))
    val msgCrF = TextMessage.ColorFont(15f * width / 1280, Color.WHITE)

    val blink16 = PiecewiseLinearFunction:
      for i <- 0 to 17; ph <- 0 to 1 yield
        val t = i * 16.0 / 17
        (t + 0.01 * ph) -> (if i % 2 == ph then 0.0 else 1.0)

    val blink13 = PiecewiseLinearFunction:
      for t <- 17 to 30; ph <- 0 to 1 yield
        (t + 0.01 * ph) -> (if t % 2 == ph then 1.0 else 0.0)

    val allGraphics = GraphicsConsumer.compose(
      TextMessage("Tired of your rockets",
        msgCF, width * 0.18, height * 0.15, HA.Center, VA.Center)
        .withAlpha(blink16),
      TextMessage("falling after landing?",
        msgCF, width * 0.18, height * 0.15 + msgStep, HA.Center, VA.Center)
        .withAlpha(blink16),

      TextMessage("Credit: bps.space",
        msgCrF, width * 0.99, height * 0.99, HA.Right, VA.Bottom)
        .enabledBetween(5, 10),
      TextMessage("Credit: SpaceX",
        msgCrF, width * 0.99, height * 0.99, HA.Right, VA.Bottom)
        .enabledBetween(10, 16.2),

      TextMessage("Cannot afford",
        msgCF, width * 0.12, height * 0.15, HA.Center, VA.Center)
        .withAlpha(blink13),
      TextMessage("a catch tower?",
        msgCF, width * 0.12, height * 0.15 + msgStep, HA.Center, VA.Center)
        .withAlpha(blink13),
      TextMessage("Credit:",
        msgCrF, width * 0.025, height * 0.85, HA.Left, VA.Bottom)
        .enabledBetween(16.2, 30),

      Fade(timeOn = 0.5, timeOff = 0),
    )

    props.runVideo(allGraphics)
  end apply
