package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.{Fade, Plot, Sticks, TextMessage}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_06_23_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height
    val reader = props.createLogReader()

    val autoArmedTimes = reader.autoArmedTimes
    require(autoArmedTimes.size == 1)
    val logTimeOffset = autoArmedTimes(0) - props.armTime

    val stickSize = 101 * width / 1280
    val stickGap = 5 * width / 1280
    val sticks = new Sticks(reader, logTimeOffset, stickSize,
      width - 2 * (stickSize + stickGap), width - (stickSize + stickGap), stickGap)

    val rpWidth = width / 3
    val rpHeight = height / 2
    val rpGap = 5 * width / 1280
    val fontSize = 13f * width / 1280
    val rpBackground = new Color(255, 255, 255, 150)
    val pitchPlot = Plot.createRollPitchPlot(reader, logTimeOffset, "Pitch", 4, 90,
      rpGap, height - rpHeight - rpGap, rpWidth, rpHeight, fontSize, rpBackground, 4)

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 50))

    val allGraphics = GraphicsConsumer.compose(sticks, pitchPlot.enabledBetween(15, Double.PositiveInfinity),
      TextMessage("June 23, 2024.",
        msgCF, width * 0.21f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(1, 15),
      TextMessage("Test #2: side wind using a hairdryer.",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(1, 15),
      TextMessage("This is a poor man's solution,",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 2.5f, HA.Center, VA.Center)
        .enabledBetween(8, 15),
      TextMessage("because the flow is quite narrow,",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 3.5f, HA.Center, VA.Center)
        .enabledBetween(8, 15),
      TextMessage("but this is the best I can do.",
        msgCF, width * 0.21f, height * 0.06f + msgStep * 4.5f, HA.Center, VA.Center)
        .enabledBetween(8, 15),

      TextMessage("The hairdryer is currently off,",
        msgCF, width * 0.22f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(17, 25),
      TextMessage("while I'm setting throttle",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(17, 25),
      TextMessage("to be suitable for the test.",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(17, 25),

      TextMessage("The hairdryer is turned on.",
        msgCF, width * 0.22f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(39, 45),
      TextMessage("Observe the change in",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 1.5f, HA.Center, VA.Center)
        .enabledBetween(40, 45),
      TextMessage("the flap's behavior.",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 2.5f, HA.Center, VA.Center)
        .enabledBetween(40, 45),

      TextMessage("The flap definitely wants",
        msgCF, width * 0.22f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(48, 57),
      TextMessage("to compensate some force",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(48, 57),
      TextMessage("resulting from the wind",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(48, 57),
      TextMessage("which tries to rotate",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 3f, HA.Center, VA.Center)
        .enabledBetween(48, 57),
      TextMessage("the craft clockwise.",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 4f, HA.Center, VA.Center)
        .enabledBetween(48, 57),

      TextMessage("When CX7 is upside down,",
        msgCF, width * 0.22f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(63, 70),
      TextMessage("this force does not seem",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(63, 70),
      TextMessage("to have much effect!",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 2f, HA.Center, VA.Center)
        .enabledBetween(63, 70),


      TextMessage("I forgot to add",
        msgCF, width * 0.22f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(84, 90),
      TextMessage("some weight to the stand...",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(84, 90),

      TextMessage("The hairdryer is still on,",
        msgCF, width * 0.22f, height * 0.06f, HA.Center, VA.Center)
        .enabledBetween(140, 145),
      TextMessage("which is enough to rotate the craft.",
        msgCF, width * 0.22f, height * 0.06f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(140, 145),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 160.5, timeOff = 161),
    )

    props.runVideo(allGraphics)
  end apply
  
