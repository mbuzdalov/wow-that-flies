package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import scala.language.implicitConversions

import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_21_p1:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 100))

    val allGraphics = GraphicsConsumer.compose(
      TextMessage("If we have troubles running AutoTune outdoors,",
        msgCF, width * 0.5f, height * 0.1f,
        HA.Center, VA.Center, 1, 8),
      TextMessage("why not trying to do it at home?",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 1f,
        HA.Center, VA.Center, 1, 8),
      TextMessage("Warning: only do it when you know how it works!",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 2.5f,
        HA.Center, VA.Center, 4, 8),
      TextMessage("A lot of care is required!",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 3.5f,
        HA.Center, VA.Center, 4, 8),

      TextMessage("This video was filmed in the evening of April 20th.",
        msgCF, width * 0.5f, height * 0.65f,
        HA.Center, VA.Center, 10, 13),

      TextMessage("AutoTune is started, tuning pitch this time.",
        msgCF, width * 0.5f, height * 0.5f,
        HA.Center, VA.Center, 16, 25),
      TextMessage("The normal order is 'roll, pitch, yaw',",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 1.5f,
        HA.Center, VA.Center, 18, 25),
      TextMessage("but I did not film all the tuning runs.",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 2.5f,
        HA.Center, VA.Center, 18, 25),
      TextMessage("Neither I saved the logs: for successful runs I don't.",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 4f,
        HA.Center, VA.Center, 21, 25),

      TextMessage("Safety is the priority,",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 30, 42),
      TextMessage("so I cannot always keep the copter visible.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center, 30, 42),
      TextMessage("AutoTune performs twitches which move the copter a lot,",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 2.5f,
        HA.Center, VA.Center, 33, 42),
      TextMessage("and my job is to override that to avoid any collisions.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 3.5f,
        HA.Center, VA.Center, 33, 42),
      TextMessage("However, the more interventions you do,",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 5f,
        HA.Center, VA.Center, 38, 42),
      TextMessage("the less time is left for tuning,",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 6f,
        HA.Center, VA.Center, 38, 42),
      TextMessage("so this is a very ad-hoc activity.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 7f,
        HA.Center, VA.Center, 38, 42),

      TextMessage("Twitches always alternate directions,",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 46, 53),
      TextMessage("except one case when they do not,",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center, 46, 53),
      TextMessage("which will be fixed soon.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 2f,
        HA.Center, VA.Center, 46, 53),
      TextMessage("This helps to predict where the copter",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 3.5f,
        HA.Center, VA.Center, 50, 53),
      TextMessage("is going, and to act accordingly.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 4.5f,
        HA.Center, VA.Center, 50, 53),

      TextMessage("The time between the twitches depends on",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 54, 67),
      TextMessage("how reliably the copter can level itself.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center, 54, 67),
      TextMessage("Each twitch can adjust the parameters only slightly,",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 2.5f,
        HA.Center, VA.Center, 59, 67),
      TextMessage("the more to change, the more twitches to do.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 3.5f,
        HA.Center, VA.Center, 59, 67),
      TextMessage("So the better the existing tuning,",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 5f,
        HA.Center, VA.Center, 64, 67),
      TextMessage("the quicker AutoTune runs.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 6f,
        HA.Center, VA.Center, 64, 67),

      TextMessage("Twitches can be 'long' or 'short',",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 77, 83),
      TextMessage("depending on where you are in the tuning,",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center, 77, 83),
      TextMessage("so be prepared.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 2f,
        HA.Center, VA.Center, 77, 83),

      TextMessage("If you have to override the controls",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 88, 94),
      TextMessage("to avoid collision or to reposition the copter,",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center, 88, 94),
      TextMessage("it stops twitching and has to redo",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 2f,
        HA.Center, VA.Center, 88, 94),
      TextMessage("the interrupted twitch, if any.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 3f,
        HA.Center, VA.Center, 88, 94),

      TextMessage("So, after gaining some experience,",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center, 177, 184),
      TextMessage("one can safely run AutoTune on small copters",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center, 177, 184),
      TextMessage("in quite confined spaces, like this room.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 2f,
        HA.Center, VA.Center, 177, 184),

      TextMessage("AutoTune has finished.",
        msgCF, width * 0.5f, height * 0.7f,
        HA.Center, VA.Center, 284, 290),
      TextMessage("Now it's time to test the new gains.",
        msgCF, width * 0.5f, height * 0.7f + msgStep * 1f,
        HA.Center, VA.Center, 284, 290),
      TextMessage("You can switch between old and new gains to compare them.",
        msgCF, width * 0.5f, height * 0.7f,
        HA.Center, VA.Center, 292, 297),

      TextMessage("If you like the new gains, land and disarm",
        msgCF, width * 0.5f, height * 0.3f,
        HA.Center, VA.Center, 308, 312),
      TextMessage("with the tumbler on, and new gains are saved for you.",
        msgCF, width * 0.5f, height * 0.3f + msgStep * 1f,
        HA.Center, VA.Center, 308, 312),

      TextMessage("I only filmed pitch tuning that day.",
        msgCF, width * 0.5f, height * 0.2f,
        HA.Center, VA.Center, 313, 321),
      TextMessage("Roll was tuned before, yaw was tuned after that.",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 1f,
        HA.Center, VA.Center, 313, 321),
      TextMessage("Together, it was a whole new feeling to fly CX7.",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 2f,
        HA.Center, VA.Center, 317, 321),

      Fade(timeOn = 320.5, timeOff = 321),
    )

    props.runVideo(allGraphics)
  end apply
