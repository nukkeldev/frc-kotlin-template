package frc.robot.util

import edu.wpi.first.wpilibj2.command.SubsystemBase

abstract class ExtendedSubsystem : SubsystemBase() {
    open fun initialize(): ExtendedSubsystem = this
    open fun onDisabledInit() {}
    open fun onAutonomousInit() {}
    open fun onTeleopInit() {}
}