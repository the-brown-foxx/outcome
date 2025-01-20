@file:OptIn(ExperimentalWasmDsl::class)

import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "com.thebrownfoxx"
version = "0.3.1"

kotlin {
    explicitApi()

    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()
    macosX64()
    macosArm64()
    mingwX64()
    js {
        browser()
        nodejs()
    }
    wasmJs {
        browser()
        nodejs()
        d8()
    }
    wasmWasi {
        nodejs()
    }
}

mavenPublishing {
    coordinates(
        groupId = group.toString(),
        artifactId = "outcome",
        version = version.toString(),
    )

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    pom {
        name = "Outcome"
        description = "An alternative to Kotlin's Result type"
        inceptionYear = "2024"
        url = "https://github.com/thebrownfoxx/outcome"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "thebrownfoxx"
                name = "Hamuel Agulto"
                url = "https://github.com/thebrownfoxx"
            }
        }
        scm {
            url = "https://github.com/thebrownfoxx/outcome"
            connection = "scm:git:git://github.com/thebrownfoxx/outcome.git"
            developerConnection = "scm:git:ssh://git@github.com/thebrownfoxx/outcome.git"
        }
    }
}