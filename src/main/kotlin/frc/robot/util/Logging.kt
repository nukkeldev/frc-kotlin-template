package frc.robot.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <T : Any> T.getLogger(): Logger =
    LoggerFactory.getLogger(this::class.java)

fun String.getLogger(): Logger =
    LoggerFactory.getLogger(this)