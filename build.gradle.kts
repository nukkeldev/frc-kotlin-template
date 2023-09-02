import edu.wpi.first.deployutils.deploy.artifact.FileTreeArtifact
import edu.wpi.first.gradlerio.GradleRIOPlugin
import edu.wpi.first.gradlerio.deploy.roborio.FRCJavaArtifact
import edu.wpi.first.gradlerio.deploy.roborio.RoboRIO
import edu.wpi.first.toolchain.NativePlatforms
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    java
    idea
    id("edu.wpi.first.GradleRIO") version "2023.4.3"
}

val javaVersion = JavaVersion.VERSION_17

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

val group = "frc.robot"
val robotMainClass = "$group.Main"

repositories {
    mavenLocal()
    mavenCentral()

    // TODO: The published version of 2023.4.3, does not register WPILib nor vendordep repos, so we have to declare them manually.
    // TODO: Already fixed on the `main` branch of GradleRIO (compile it locally if you wanna delete these)
    maven("https://frcmaven.wpi.edu/artifactory/release")
    maven("https://maven.ctr-electronics.com/release/")
    maven("https://maven.revrobotics.com/")
    maven("https://maven.photonvision.org/repository/internal")
}

// Define my targets (RoboRIO) and artifacts (deployable files)
// This is added by GradleRIO's backing project DeployUtils.
deploy {
    targets {
        register<RoboRIO>(name = "roborio") {
            // The team number is loaded either from the .wpilib/wpilib_preferences.json
            // or from the command line. If not found, an exception will be thrown.
            // You can use getTeamOrDefault(team) instead of getTeamNumber if you
            // want to store a team number in this file.
            team = project.frc.teamNumber
            directory = "/home/lvuser/deploy"

//            debug.set(project.frc.getDebugOrDefault(false))

            this.artifacts {
                register<FRCJavaArtifact>("frcJava") {
                    dependsOn(tasks.jar.get())
                    setJarTask(tasks.jar.get())
                }

                register<FileTreeArtifact>("frcStaticFileDeploy") {
                    files.set(project.fileTree("src/main/deploy"))
                }
            }
        }
    }
}

wpi {
    // Simulation configuration (e.g. environment variables).
    with(sim) {
        addGui().defaultEnabled.set(true)
        addDriverstation()
    }

    with(java) {
        // Configure jar and deploy tasks
        wpi.java.configureExecutableTasks(tasks.jar.get())
        wpi.java.configureTestTasks(tasks.test.get())

        debugJni.set(false)
    }
}

val includeDesktopSupport = true

dependencies {
    with(wpi.java) {
        deps.wpilib().forEach { implementation(it.get()) }
        vendor.java().forEach { implementation(it.get()) }

        deps.wpilibJniDebug(NativePlatforms.roborio).forEach { "roborioDebug"(it.get()) }
        vendor.jniDebug(NativePlatforms.roborio).forEach { "roborioDebug"(it.get()) }

        deps.wpilibJniRelease(NativePlatforms.roborio).forEach { "roborioRelease"(it.get()) }
        vendor.jniRelease(NativePlatforms.roborio).forEach { "roborioRelease"(it.get()) }

        deps.wpilibJniDebug(NativePlatforms.desktop).forEach { nativeDebug(it) }
        vendor.jniDebug(NativePlatforms.desktop).forEach { nativeDebug(it) }

        deps.wpilibJniRelease(NativePlatforms.desktop).forEach { nativeRelease(it) }
        vendor.jniRelease(NativePlatforms.desktop).forEach { nativeRelease(it) }
    }

    // Pretty sure these are for external simulation (Increases JAR size significantly)
    // wpi.sim.enableDebug().forEach { implementation(it.get()) }
    // wpi.sim.enableRelease().forEach { implementation(it.get()) }

    // Logging
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
    implementation("com.lmax:disruptor:3.4.4") // Async
}

tasks {
    jar {
        group = "build"
        description = """
            Adding all libraries into the main jar ("fat jar") in order to make them all available at runtime.
            Also adding the manifest so WPILib knows where to look for our Robot Class.  
        """.trimIndent()
        dependsOn(configurations.runtimeClasspath)

        GradleRIOPlugin.javaManifest(robotMainClass)(manifest)

        from(
            configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
        )
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    withType<JavaCompile> {
        // Configure string concat to always inline compile
        options.compilerArgs.add("-XDstringConcat=inline")
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = javaVersion.majorVersion
        }
    }
}

idea {
    project {
        // The project.sourceCompatibility setting is not always picked up, so we set explicitly
        languageLevel = IdeaLanguageLevel(javaVersion)
    }
    module {
        // Improve development & (especially) debugging experience (and IDEA's capabilities) by having libraries' source & javadoc attached
        isDownloadJavadoc = true
        isDownloadSources = true
        // Exclude the .vscode directory from indexing and search
        excludeDirs.add(file(".vscode"))
    }
}