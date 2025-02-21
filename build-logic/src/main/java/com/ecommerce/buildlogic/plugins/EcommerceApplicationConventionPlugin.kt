package com.ecommerce.buildlogic.plugins

import com.android.build.api.dsl.ApplicationExtension
import com.ecommerce.buildlogic.ext.configureKotlinAndroid
import com.ecommerce.buildlogic.utils.AppConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class EcommerceApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")
            // TODO: To be removed after all feature has been refactored
            apply(plugin = "androidx.navigation.safeargs")
            apply(plugin = "com.google.devtools.ksp")
            apply(plugin = "kotlin-parcelize")
            apply(plugin = "com.google.gms.google-services")
            apply(plugin = "com.google.firebase.crashlytics")
            apply(plugin = "jacoco")
            apply(plugin = "io.gitlab.arturbosch.detekt")
            extensions.configure<ApplicationExtension>{
                configureKotlinAndroid(this)
                with(defaultConfig) {
                    targetSdk = AppConfig.TARGET_SDK
                    applicationId = AppConfig.APP_ID
                    versionCode = AppConfig.VERSION_CODE
                    versionName = AppConfig.VERSION_NAME
                }
            }
        }
    }
}
