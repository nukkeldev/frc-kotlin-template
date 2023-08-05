package frc.robot

import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.util.ExtendedSubsystem
import frc.robot.util.getLogger
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sign

object OI : ExtendedSubsystem() {
    private val logger = getLogger()

    private const val DEADZONE = 0.09

    private val driveController = CommandXboxController(0)

    init {

    }

    private fun deadband(value: Double) =
        if (value.absoluteValue > DEADZONE) {
            (value - DEADZONE * value.sign) / (1.0 - DEADZONE)
        } else 0.0

    private fun desensitizedPowerBased(value: Double, power: Double): Double =
        deadband(value) * value.absoluteValue.pow(power - 1)
}