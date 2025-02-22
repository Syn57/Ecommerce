// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.5")
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
//    id("com.android.application") version "8.1.2" apply false
//    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    alias(libs.plugins.compose) apply false
//    id("com.android.library") version "8.1.2" apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.ksp) apply false
    id("io.gitlab.arturbosch.detekt") version ("1.23.1")
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}