package com.github.mbuzdalov.wtf.videos

import java.awt.Color

import com.github.mbuzdalov.wtf.widgets.*
import com.github.mbuzdalov.wtf.widgets.TextMessage.{HorizontalAlignment as HA, VerticalAlignment as VA}
import com.github.mbuzdalov.wtf.{BasicProperties, GraphicsConsumer}

object V_2024_04_21_p2:
  def apply(props: BasicProperties): Unit =
    val width = props.width
    val height = props.height

    val msgFontSize = 36f * width / 1280
    val msgStep = msgFontSize * 1.5f
    val msgCF = TextMessage.ColorFont(msgFontSize, new Color(10, 10, 50), new Color(255, 255, 255, 100))

    val tailFontSize = 25f * width / 1280
    val tailStep = tailFontSize * 1.5f
    val tailCF = TextMessage.ColorFont(tailFontSize, new Color(240, 240, 0))
    val goodCF = TextMessage.ColorFont(tailFontSize, new Color(0, 210, 0))
    val badCF = TextMessage.ColorFont(tailFontSize, new Color(190, 0, 0))

    val allGraphics = GraphicsConsumer.compose(
      TextMessage("After running AutoTune on all axes,",
        msgCF, width * 0.5f, height * 0.1f, HA.Center, VA.Center)
        .enabledBetween(1, 7),
      TextMessage("it is a good idea to repeat, because",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 1f, HA.Center, VA.Center)
        .enabledBetween(1, 7),
      TextMessage("if yaw is sloppy, bad pitch could prevent roll",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 2f,
        HA.Center, VA.Center)
        .enabledBetween(1, 7),
      TextMessage("converging at the best parameters it can do.",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 3f,
        HA.Center, VA.Center)
        .enabledBetween(1, 7),

      TextMessage("This video is filmed on April 21st,",
        msgCF, width * 0.5f, height * 0.1f,
        HA.Center, VA.Center)
        .enabledBetween(13, 26),
      TextMessage("and this time it is roll tuning.",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 1f,
        HA.Center, VA.Center)
        .enabledBetween(13, 26),
      TextMessage("I used two cameras this time,",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 2.5f,
        HA.Center, VA.Center)
        .enabledBetween(17, 26),
      TextMessage("but it did not help much,",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 3.5f,
        HA.Center, VA.Center)
        .enabledBetween(17, 26),
      TextMessage("and there is even more time when CX7 is invisible.",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 4.5f,
        HA.Center, VA.Center)
        .enabledBetween(17, 26),
      TextMessage("Maybe it's good time to tell more about Ardupilot's AutoTune.",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 6f,
        HA.Center, VA.Center)
        .enabledBetween(22, 26),

      TextMessage("Twitches are of two types, targeting either rate or angle.",
        msgCF, width * 0.5f, height * 0.1f,
        HA.Center, VA.Center)
        .enabledBetween(28, 49),
      TextMessage("Rate twitches command a target rate of 90 degrees/second.",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 1f,
        HA.Center, VA.Center)
        .enabledBetween(32, 49),
      TextMessage("Angle twitches command a target angle of 20 degrees.",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 2f,
        HA.Center, VA.Center)
        .enabledBetween(36, 49),
      TextMessage("For both of them, two things are recorded",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 3.5f,
        HA.Center, VA.Center)
        .enabledBetween(40, 49),
      TextMessage("about the actually observed rate or angle:",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 4.5f,
        HA.Center, VA.Center)
        .enabledBetween(40, 49),
      TextMessage("the maximum value, and the minimum value",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 5.5f,
        HA.Center, VA.Center)
        .enabledBetween(43, 49),
      TextMessage("after reaching that maximum value (the 'bounce-back').",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 6.5f,
        HA.Center, VA.Center)
        .enabledBetween(43, 49),

      TextMessage("The rate P-term is changed to get high enough",
        msgCF, width * 0.5f, height * 0.12f,
        HA.Center, VA.Center)
        .enabledBetween(53, 72),
      TextMessage("so that the maximum rate gets within 80-100% to the target rate,",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 1f,
        HA.Center, VA.Center)
        .enabledBetween(53, 72),
      TextMessage("but low enough to not exceed the target rate.",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 2f,
        HA.Center, VA.Center)
        .enabledBetween(53, 72),
      TextMessage("The rate D-term is in charge of the bounce-back:",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 3.5f,
        HA.Center, VA.Center)
        .enabledBetween(60, 72),
      TextMessage("the greater D, the greater the bounce-back,",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 4.5f,
        HA.Center, VA.Center)
        .enabledBetween(60, 72),
      TextMessage("and we want bounce-back to be XX% of the target rate.",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 5.5f,
        HA.Center, VA.Center)
        .enabledBetween(60, 72),
      TextMessage("The value of XX is called aggressiveness, default is 10%.",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 7f,
        HA.Center, VA.Center)
        .enabledBetween(60, 72),

      TextMessage("After P and D-terms are iterated to be within the limits,",
        msgCF, width * 0.5f, height * 0.12f,
        HA.Center, VA.Center)
        .enabledBetween(79, 91),
      TextMessage("the next is the angle P-term,",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 1f,
        HA.Center, VA.Center)
        .enabledBetween(79, 91),
      TextMessage("which is in charge of tracking the angle.",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 2f,
        HA.Center, VA.Center)
        .enabledBetween(79, 91),
      TextMessage("ATC_ANG_XXX_P is increased so that the maximum angle",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 3.5f,
        HA.Center, VA.Center)
        .enabledBetween(86, 91),
      TextMessage("in the angle twitch is 110% of the target angle,",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 4.5f,
        HA.Center, VA.Center)
        .enabledBetween(86, 91),
      TextMessage("then it gets backed off by 10%.",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 5.5f,
        HA.Center, VA.Center)
        .enabledBetween(86, 91),

      TextMessage("Finally, last twitches measure maximum angle acceleration",
        msgCF, width * 0.5f, height * 0.12f,
        HA.Center, VA.Center)
        .enabledBetween(102, 109),
      TextMessage("and write it down to ATC_ACCEL_X_MAX,",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 1f,
        HA.Center, VA.Center)
        .enabledBetween(102, 109),
      TextMessage("so that the desired rates do not change faster",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 2f,
        HA.Center, VA.Center)
        .enabledBetween(102, 109),
      TextMessage("than what the copter can handle.",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 3f,
        HA.Center, VA.Center)
        .enabledBetween(102, 109),

      TextMessage("All these phases map to different behavior,",
        msgCF, width * 0.5f, height * 0.2f,
        HA.Center, VA.Center)
        .enabledBetween(125, 130),
      TextMessage("which you will learn to recognize after few runs.",
        msgCF, width * 0.5f, height * 0.2f + msgStep * 1f,
        HA.Center, VA.Center)
        .enabledBetween(125, 130),

      TextMessage("As you can see, the air pressure is NOT negligible.",
        msgCF, width * 0.5f, height * 0.12f,
        HA.Center, VA.Center)
        .enabledBetween(158, 163),
      TextMessage("What can fly away, will fly away.",
        msgCF, width * 0.5f, height * 0.12f + msgStep * 1f,
        HA.Center, VA.Center)
        .enabledBetween(158, 163),

      TextMessage("The angle P-term is currently being tuned,",
        msgCF, width * 0.5f, height * 0.5f,
        HA.Center, VA.Center)
        .enabledBetween(216, 220),
      TextMessage("observe the 'long' angle-targeting twitches.",
        msgCF, width * 0.5f, height * 0.5f + msgStep * 1f,
        HA.Center, VA.Center)
        .enabledBetween(216, 220),

      TextMessage("The final twitch was short,",
        msgCF, width * 0.5f, height * 0.1f,
        HA.Center, VA.Center)
        .enabledBetween(238, 242),
      TextMessage("this was the acceleration measurement,",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 1f,
        HA.Center, VA.Center)
        .enabledBetween(238, 242),
      TextMessage("and we are done with pitch.",
        msgCF, width * 0.5f, height * 0.1f + msgStep * 2f,
        HA.Center, VA.Center)
        .enabledBetween(238, 242),

      TextMessage("Now it's time to test the updated gains.",
        msgCF, width * 0.5f, height * 0.7f,
        HA.Center, VA.Center)
        .enabledBetween(246, 251),

      TextMessage("If you backed up the config,",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center)
        .enabledBetween(258, 262),
      TextMessage("you can save the gains and decide later.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center)
        .enabledBetween(258, 262),

      TextMessage("The second round actually did not change much,",
        msgCF, width * 0.5f, height * 0.4f,
        HA.Center, VA.Center)
        .enabledBetween(263, 267),
      TextMessage("so the config change is mainly due to the first round.",
        msgCF, width * 0.5f, height * 0.4f + msgStep * 1f,
        HA.Center, VA.Center)
        .enabledBetween(263, 267),

      Fade(timeOn = 0.5, timeOff = 0),
      Fade(timeOn = 267.5, timeOff = 268),

      TextMessage("The resulting changes were:",
        tailCF, width * 0.5f, height * 0.07f,
        HA.Center, VA.Center).enabledBetween(268, 280),

      TextMessage("ATC_RAT_RLL_{P,I}: 0.22 ➞ 0.129",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 1.5f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("ATC_RAT_RLL_D: 0.005 ➞ 0.001",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("Not too nice: minimum D-term value set!",
        badCF, width * 0.5f, height * 0.07f + tailStep * 2.5f, HA.Left, VA.Center)
        .enabledBetween(272, 280),
      TextMessage("ATC_ANG_RLL_P: 16 ➞ 6.338",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 3.5f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("ATC_ACCEL_R_MAX: 30000 ➞ 65803",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 4.5f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("Nice!",
        goodCF, width * 0.5f, height * 0.07f + tailStep * 4.5f, HA.Left, VA.Center)
        .enabledBetween(272, 280),

      TextMessage("ATC_RAT_PIT_{P,I}: 0.22 ➞ 0.114",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 6f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("ATC_RAT_PIT_D: 0.005 ➞ 0.00515",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 7f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("Nice guess here!",
        tailCF, width * 0.5f, height * 0.07f + tailStep * 7f, HA.Left, VA.Center)
        .enabledBetween(272, 280),
      TextMessage("ATC_ANG_PIT_P: 16 ➞ 6.274",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 8f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("ATC_ACCEL_P_MAX: 30000 ➞ 32300",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 9f, HA.Left, VA.Center)
        .enabledBetween(268, 280),

      TextMessage("ATC_RAT_YAW_P: 0.3 ➞ 0.473",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 10.5f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("Tightened yaw yet a little bit",
        tailCF, width * 0.5f, height * 0.07f + tailStep * 10.5f, HA.Left, VA.Center)
        .enabledBetween(272, 280),
      TextMessage("ATC_RAT_YAW_I: 0.03 ➞ 0.0473",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 11.5f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("ATC_RAT_YAW_D: 0 ➞ 0.119",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 12.5f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("Quite a large D-term value was set; seems OK",
        tailCF, width * 0.5f, height * 0.07f + tailStep * 12.5f, HA.Left, VA.Center)
        .enabledBetween(272, 280),
      TextMessage("ATC_RAT_YAW_FLTE: 2.5 ➞ 1.28",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 13.5f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("ATC_ANG_YAW_P: 4.5 ➞ 14.67",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 14.5f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("Very nice!",
        goodCF, width * 0.5f, height * 0.07f + tailStep * 14.5f, HA.Left, VA.Center)
        .enabledBetween(272, 280),
      TextMessage("ATC_ACCEL_Y_MAX: 54000 ➞ 96140",
        tailCF, width * 0.1f, height * 0.07f + tailStep * 15.5f, HA.Left, VA.Center)
        .enabledBetween(268, 280),
      TextMessage("I like the way yaw is tuned!",
        goodCF, width * 0.5f, height * 0.07f + tailStep * 15.5f, HA.Left, VA.Center)
        .enabledBetween(272, 280),
    )

    props.runVideo(allGraphics)
  end apply
