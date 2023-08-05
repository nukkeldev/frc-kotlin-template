pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.0"
    }

    repositories {
        mavenLocal()
        gradlePluginPortal()
        val frcYear = "2023"
        // Throws an Unresolved Reference if the OperatingSystem is not fully qualified
        val frcHome = if (org.gradle.internal.os.OperatingSystem.current().isWindows) {
            var publicFolder = System.getenv("PUBLIC")
            if (publicFolder == null) {
                publicFolder = "C:\\Users\\Public"
            }
            val homeRoot = File(publicFolder, "wpilib")
            File(homeRoot, frcYear)
        } else {
            val userFolder = System.getProperty("user.home")
            val homeRoot = File(userFolder, "wpilib")
            File(homeRoot, frcYear)
        }
        val frcHomeMaven = File(frcHome, "maven")
        maven {
            name = "frcHome"
            url = frcHomeMaven.toURI()
        }
    }
}