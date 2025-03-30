package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p14a:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val allGraphics = GraphicsConsumer.compose(
      TopBlanket(0.1f, new Color(240, 240, 240, 200), 1.0, 1.4, 4.5, 4.9),
      TextMessage("Replay of the flight, the FPV feed (not used to fly)",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center).enabledBetween(1.5, 4.5),

      TopBlanket(0.16f, new Color(240, 240, 240, 200), 11.0, 11.4, 17.5, 17.9),
      TextMessage("This looks like SN1 rushes a lot backwards and to the right!",
        msgCF, width * 0.5, height * 0.05, HA.Center, VA.Center).enabledBetween(11.5, 17.5),
      TextMessage("Pitch reached 11 degrees, roll was more than 35 degrees.",
        msgCF, width * 0.5, height * 0.05 + msgStep, HA.Center, VA.Center).enabledBetween(11.5, 17.5),

      TopBlanket(0.5, new Color(240, 240, 240, 200), 19.0, 19.4, 34.5, 34.9),
      TextMessage("The logs showed that SN1 thought it was facing north,",
        msgCF, width * 0.5, 0.6 * msgStep, HA.Center, VA.Center).enabledBetween(19.5, 34.5),
      TextMessage("whereas it was actually facing south! (Same yesterday on flight 6)",
        msgCF, width * 0.5, 1.6 * msgStep, HA.Center, VA.Center).enabledBetween(19.5, 34.5),
      TextMessage("Apparently, Gaussian Sum Filtering is not reliable",
        msgCF, width * 0.5, 3 * msgStep, HA.Center, VA.Center).enabledBetween(24.5, 34.5),
      TextMessage("when the craft ascends almost vertically and then lands,",
        msgCF, width * 0.5, 4 * msgStep, HA.Center, VA.Center).enabledBetween(24.5, 34.5),
      TextMessage("which is close to one of the use cases of SN1.",
        msgCF, width * 0.5, 5 * msgStep, HA.Center, VA.Center).enabledBetween(24.5, 34.5),
      TextMessage("Okay, next time, I will make the compass usable. See you then!",
        msgCF, width * 0.5, 6.5 * msgStep, HA.Center, VA.Center).enabledBetween(29.5, 34.5),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 35.5, timeOff = 36.0),
    )

    props.runVideo(allGraphics)
  end apply
