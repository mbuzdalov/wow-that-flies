package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, GlowingRectangle, RightBlanket, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_01_09_p0:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val textLeft = 1.02 / 3 * width
    val textMid = 0.5 * width
    val textTop = height * 0.07
    val allGraphics = GraphicsConsumer.compose(
      RightBlanket(2.0 / 3, new Color(0xdbcbbc).brighter(), -1, -0.5, 20.1, 20.5),
      TextMessage("In the hindsight, this was obvious.",
        msgCF, textLeft, textTop, HA.Left, VA.Center)
        .enabledBetween(1, 20),
      TextMessage("The center of gravity of this machine is pretty much",
        msgCF, textLeft, textTop + msgStep * 1.5, HA.Left, VA.Center)
        .enabledBetween(3, 20),
      TextMessage("at the geometric center, vertically and horizontally.",
        msgCF, textLeft, textTop + msgStep * 2.5, HA.Left, VA.Center)
        .enabledBetween(3, 20),
      TextMessage("If you look at this image on the left,",
        msgCF, textLeft, textTop + msgStep * 4, HA.Left, VA.Center)
        .enabledBetween(7, 20),
      TextMessage("you will see that the area above the center",
        msgCF, textLeft, textTop + msgStep * 5, HA.Left, VA.Center)
        .enabledBetween(7, 20),
      TextMessage("is much bigger than below.",
        msgCF, textLeft, textTop + msgStep * 6, HA.Left, VA.Center)
        .enabledBetween(7, 20),
      TextMessage("When flying forward, this creates a parasitic",
        msgCF, textLeft, textTop + msgStep * 7.5, HA.Left, VA.Center)
        .enabledBetween(13, 20),
      TextMessage("moment in pitch, increasing with the speed.",
        msgCF, textLeft, textTop + msgStep * 8.5, HA.Left, VA.Center)
        .enabledBetween(13, 20),
      TextMessage("The solution?",
        msgCF, textLeft, textTop + msgStep * 10, HA.Left, VA.Center)
        .enabledBetween(17, 20),

      GlowingRectangle(7.5, 1, 20.5, 230 * width / 3840, 150 * height / 2160, (1160 - 230) * width / 3840, (950 - 150) * height / 2160, Color(150, 200, 150, 160)),
      GlowingRectangle(7.5, 1, 20.5, 350 * width / 3840, 1020 * height / 2160, (990 - 350) * width / 3840, (1500 - 1020) * height / 2160, Color(150, 150, 200, 160)),

      TextMessage("With new compartments",
        msgCF, textMid, textTop, HA.Center, VA.Center)
        .enabledBetween(22, 30),
      TextMessage("that contain and cover",
        msgCF, textMid, textTop + msgStep * 1, HA.Center, VA.Center)
        .enabledBetween(22, 30),
      TextMessage("the batteries, the lift is",
        msgCF, textMid, textTop + msgStep * 2, HA.Center, VA.Center)
        .enabledBetween(22, 30),
      TextMessage("hopefully uniform and",
        msgCF, textMid, textTop + msgStep * 3, HA.Center, VA.Center)
        .enabledBetween(22, 30),
      TextMessage("centered nicely, which",
        msgCF, textMid, textTop + msgStep * 4, HA.Center, VA.Center)
        .enabledBetween(22, 30),
      TextMessage("allows bigger pitch",
        msgCF, textMid, textTop + msgStep * 5, HA.Center, VA.Center)
        .enabledBetween(22, 30),
      TextMessage("and better flying.",
        msgCF, textMid, textTop + msgStep * 6, HA.Center, VA.Center)
        .enabledBetween(22, 30),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 30.5, timeOff = 31),
    )

    props.runImage(31, allGraphics)
  end apply
  