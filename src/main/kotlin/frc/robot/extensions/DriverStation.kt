package frc.robot.extensions

import edu.wpi.first.wpilibj.DriverStation

fun isRedAlliance() =
    DriverStation.getAlliance() == DriverStation.Alliance.Red

fun isBlueAlliance() =
    DriverStation.getAlliance() == DriverStation.Alliance.Blue