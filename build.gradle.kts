plugins {
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.kotlinMultiplatform) apply  false
    alias(libs.plugins.kotlinJvm) apply  false
    alias(libs.plugins.vanniktech.mavenPublish) apply false
}
