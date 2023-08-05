@file:Suppress("FunctionName")

package frc.robot.extensions

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d

fun Double.radR2D(): Rotation2d = Rotation2d.fromRadians(this)
fun Double.degR2D(): Rotation2d = Rotation2d.fromDegrees(this)

fun Pose2d(x: Double, y: Double) = Pose2d(x, y, Rotation2d())
fun Pose2dIn(x: Double, y: Double, rot: Rotation2d = Rotation2d()) = Pose2d(x.inToM(), y.inToM(), rot)
fun Trans2dIn(x: Double, y: Double) = Translation2d(x.inToM(), y.inToM())