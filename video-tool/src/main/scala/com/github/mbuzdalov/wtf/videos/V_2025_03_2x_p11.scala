package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2025_03_2x_p11:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 33f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(40, 10, 40))

    val finalMsgFontSize = 36f * width / 1280
    val finalMsgStep = finalMsgFontSize * 1.5f
    val finalMsgCF = TextMessage.ColorFont(finalMsgFontSize, new Color(240, 200, 240))

    val allGraphics = GraphicsConsumer.compose(
      ScaleRotateCropBack(scale = t => 1.0 + t / 200, rotate = t => 0.0),
      TopBlanket(0.4, new Color(255, 255, 255, 200), 1.6, 2, 12.1, 12.5),
      TextMessage("What is more, the servo arm got broken:",
        msgCF, width * 0.5, 0.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(2, 12.1),
      TextMessage("the middle ring and the actual arm were disconnected,",
        msgCF, width * 0.5, 1.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(2, 12.1),
      TextMessage("and the part of the flap rotated freely on the servo.",
        msgCF, width * 0.5, 2.5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(2, 12.1),
      TextMessage("My first hypothesis was that this happened before Test 5, and",
        msgCF, width * 0.5, 4 * msgStep, HA.Center, VA.Center)
        .enabledBetween(7, 12.1),
      TextMessage("on high servo outputs this slipped and resulted in poor control.",
        msgCF, width * 0.5, 5 * msgStep, HA.Center, VA.Center)
        .enabledBetween(7, 12.1),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 12.5, timeOff = 13),

      TextMessage("But the logs told a different story.",
        finalMsgCF, width * 0.5, height * 0.08, HA.Center, VA.Center).enabledBetween(13, 42),
      TextMessage("In the first three flights,",
        finalMsgCF, width * 0.5, height * 0.08 + 1.5 * finalMsgStep, HA.Center, VA.Center).enabledBetween(15, 42),
      TextMessage("SN1 could not determine the absolute yaw direction",
        finalMsgCF, width * 0.5, height * 0.08 + 2.5 * finalMsgStep, HA.Center, VA.Center).enabledBetween(15, 42),
      TextMessage("in the absence of a compass, that is, from GPS and accels/gyros.",
        finalMsgCF, width * 0.5, height * 0.08 + 3.5 * finalMsgStep, HA.Center, VA.Center).enabledBetween(15, 42),
      TextMessage("In the fourth flight, it managed to do that,",
        finalMsgCF, width * 0.5, height * 0.08 + 5 * finalMsgStep, HA.Center, VA.Center).enabledBetween(22, 42),
      TextMessage("but only after the Landing mode had already been engaged.",
        finalMsgCF, width * 0.5, height * 0.08 + 6 * finalMsgStep, HA.Center, VA.Center).enabledBetween(22, 42),
      TextMessage("In flights 5 and 6, the direction was found before switching to Land,",
        finalMsgCF, width * 0.5, height * 0.08 + 7.5 * finalMsgStep, HA.Center, VA.Center).enabledBetween(28, 42),
      TextMessage("so it was trying to keep position with GPS while landing.",
        finalMsgCF, width * 0.5, height * 0.08 + 8.5 * finalMsgStep, HA.Center, VA.Center).enabledBetween(28, 42),
      TextMessage("Position holding is done with a different set of PIDs,",
        finalMsgCF, width * 0.5, height * 0.08 + 10 * finalMsgStep, HA.Center, VA.Center).enabledBetween(35, 42),
      TextMessage("which were at defaults and seemed too large.",
        finalMsgCF, width * 0.5, height * 0.08 + 11 * finalMsgStep, HA.Center, VA.Center).enabledBetween(35, 42),
    )

    props.runImage(42, allGraphics)
  end apply
  