import java.time.Instant

plugins {
    id("java")
}

group = "games.voided.locatables"
version = "0.1.0"

val commitShort = providers.provider { "localbuild" }
val commitFull = providers.provider { "localbuild" }

val buildTime = providers.provider {
    Instant.now().toString()
}

tasks {
    processResources {
        val gitProps = mapOf(
            "short_git" to commitShort.get(),
            "long_git" to commitFull.get(),
            "build_time" to buildTime.get(),
            "version" to version.toString()
        )
        // build-properties is not used by the locatables library itself, but is included in the jar so that platforms can access it.
        filesMatching("build-properties/locatable-lib.yml") {
            expand(gitProps)
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":logging"))
}