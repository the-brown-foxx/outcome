import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "com.thebrownfoxx"
version = "0.3.0"

kotlin {
    explicitApi()

    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/the-brown-foxx/outcome")
            credentials {
                val properties = gradleLocalProperties(projectDir, providers)
                username = properties.getProperty("gpr.user", System.getenv("USERNAME"))
                password = properties.getProperty("gpr.key", System.getenv("TOKEN"))
            }
        }
    }
}