import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "com.thebrownfoxx"
version = "0.0.1"

kotlin {
    explicitApi()
}

dependencies {
    compileOnly(libs.lintApi)
}

tasks.withType<Jar> {
    manifest {
        attributes("Lint-Registry-v2" to "com.thebrownfoxx.outcome.lint.OutcomeIssueRegistry")
    }
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