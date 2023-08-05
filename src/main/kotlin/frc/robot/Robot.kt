package frc.robot

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.robot.util.ExtendedSubsystem
import frc.robot.util.getLogger

object Robot : TimedRobot() {
    private val logger = getLogger()

    private val subsystems: List<ExtendedSubsystem> = listOf(
        OI
    )

    override fun robotInit() {
        DriverStation.silenceJoystickConnectionWarning(true)

        // Handle all post-creation initializations
        subsystems.forEach { it.initialize() }

        logger.info("Robot Initialized.")
    }

    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
    }

    override fun disabledInit() {
        subsystems.forEach { it.onDisabledInit() }
    }

    override fun autonomousInit() {
        subsystems.forEach { it.onAutonomousInit() }
    }

    override fun autonomousPeriodic() {

    }

    override fun teleopInit() {
        subsystems.forEach { it.onTeleopInit() }
    }

    override fun testInit() {
        CommandScheduler.getInstance().cancelAll()
    }

    // Unused Methods

    override fun disabledPeriodic() {}
    override fun teleopPeriodic() {}
    override fun testPeriodic() {}
    override fun simulationInit() {}
    override fun simulationPeriodic() {}
}