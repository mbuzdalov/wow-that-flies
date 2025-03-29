package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, LeftBlanket, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_10_09_p4:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50))

    val allGraphics = GraphicsConsumer.compose(
      LeftBlanket(0.4f, new Color(240, 240, 240, 200), 0.5, 0.9, 8.1, 8.5),

      TextMessage("October 09th, 2024.",
        msgCF, width * 0.2, height * 0.4, HA.Center, VA.Center)
        .enabledBetween(2, 8),
      TextMessage("First outdoor test.",
        msgCF, width * 0.2, height * 0.4 + msgStep, HA.Center, VA.Center)
        .enabledBetween(2, 8),
      TextMessage("Can I land into the cage?",
        msgCF, width * 0.2, height * 0.4 + 2 * msgStep, HA.Center, VA.Center)
        .enabledBetween(4, 8),

      LeftBlanket(0.4f, new Color(240, 240, 240, 200), 24.1, 24.4, 28.1, 28.4),
      TextMessage("Oops! A near miss!",
        msgCF, width * 0.2, height * 0.4, HA.Center, VA.Center)
        .enabledBetween(24.5, 28),

      LeftBlanket(0.4f, new Color(240, 240, 240, 200), 30.1, 30.4, 60.1, 60.4),
      TextMessage("Second approach...",
        msgCF, width * 0.2, height * 0.2, HA.Center, VA.Center)
        .enabledBetween(30.5, 60),
      TextMessage("... this time OK!",
        msgCF, width * 0.2, height * 0.2 + msgStep, HA.Center, VA.Center)
        .enabledBetween(36.3, 60),
      TextMessage("Let's try it again!",
        msgCF, width * 0.2, height * 0.2 + 3 * msgStep, HA.Center, VA.Center)
        .enabledBetween(45, 60),
      TextMessage("But this time, let's",
        msgCF, width * 0.2, height * 0.2 + 4 * msgStep, HA.Center, VA.Center)
        .enabledBetween(48, 60),
      TextMessage("lift off from the cage.",
        msgCF, width * 0.2, height * 0.2 + 5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(48, 60),
      TextMessage("And maybe let's use",
        msgCF, width * 0.2, height * 0.2 + 7 * msgStep, HA.Center, VA.Center)
        .enabledBetween(55, 60),
      TextMessage("more power this time...",
        msgCF, width * 0.2, height * 0.2 + 8 * msgStep, HA.Center, VA.Center)
        .enabledBetween(55, 60),

      LeftBlanket(0.3f, new Color(240, 240, 240, 200), 84.1, 84.4, 92.1, 92.4),
      TextMessage("Punch tests were fun!",
        msgCF, width * 0.15, height * 0.3, HA.Center, VA.Center)
        .enabledBetween(84.5, 92),
      TextMessage("A max vertical speed",
        msgCF, width * 0.15, height * 0.3 + 1.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(86, 92),
      TextMessage("of ~7 m/s was seen.",
        msgCF, width * 0.15, height * 0.3 + 2.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(86, 92),
      TextMessage("Maybe that's not",
        msgCF, width * 0.15, height * 0.3 + 4 * msgStep, HA.Center, VA.Center)
        .enabledBetween(89, 92),
      TextMessage("the limit...",
        msgCF, width * 0.15, height * 0.3 + 5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(89, 92),

      LeftBlanket(0.4f, new Color(240, 240, 240, 200), 95.1, 95.4, 109.1, 109.4),
      TextMessage("Go for landing...",
        msgCF, width * 0.2, height * 0.3, HA.Center, VA.Center)
        .enabledBetween(95.5, 109),
      TextMessage("Not ideal, but this",
        msgCF, width * 0.2, height * 0.3 + 1.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(101, 109),
      TextMessage("is what the cage is for!",
        msgCF, width * 0.2, height * 0.3 + 2.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(101, 109),
      TextMessage("Until next time...",
        msgCF, width * 0.2, height * 0.3 + 4 * msgStep, HA.Center, VA.Center)
        .enabledBetween(105, 109),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 109.5, timeOff = 110),
    )

    props.runVideo(allGraphics)
  end apply
