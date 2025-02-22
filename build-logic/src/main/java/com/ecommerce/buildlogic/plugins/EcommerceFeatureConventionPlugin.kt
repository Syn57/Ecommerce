package com.ecommerce.buildlogic.plugins

import com.android.build.gradle.LibraryExtension
import com.ecommerce.buildlogic.ext.libs
import com.ecommerce.buildlogic.utils.Modules
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class EcommerceFeatureConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureFeaturePlugins()
            configureBuildFeatures()
            configureFeatureDependencies()
        }
    }

    private fun Project.configureFeaturePlugins() {
        apply(plugin = "org.jetbrains.kotlin.kapt")
        apply(plugin = "androidx.navigation.safeargs")
        apply(plugin = "kotlin-parcelize")
        apply(plugin = "com.google.gms.google-services")
        apply(plugin = "com.google.firebase.crashlytics")
    }

    private fun Project.configureBuildFeatures() {
        extensions.configure<LibraryExtension> {
            buildFeatures {
                dataBinding = true
                viewBinding = true
            }
        }
    }

    private fun Project.configureFeatureDependencies() {
        dependencies {
            // Modules
            add("implementation", project(Modules.Core.DATA))
            add("implementation", project(Modules.Core.DATABASE))
            add("implementation", project(Modules.Core.DOMAIN))
            add("implementation", project(Modules.Core.MODEL))
            add("implementation", project(Modules.Core.NETWORK))
            add("implementation", project(Modules.Core.UI_ASSETS))

            // External library
            add("implementation", libs.findLibrary("androidx-core-ktx").get())
            add("implementation", libs.findLibrary("androidx-appcompat").get())
            add("implementation", libs.findLibrary("androidx-constraintlayout").get())
            add("implementation", libs.findLibrary("androidx-material3-android").get())
            add("implementation", libs.findLibrary("koin-core").get())
            add("implementation", libs.findLibrary("koin-android").get())
            add("implementation", libs.findLibrary("kotlinx-coroutines-core").get())
            add("implementation", libs.findLibrary("kotlinx-coroutines-android").get())
            add("implementation", libs.findLibrary("retrofit-core").get())
            add("implementation", libs.findLibrary("retrofit-kotlin-serialization").get())
            add("implementation", libs.findLibrary("kotlinx-coroutines-android").get())
            add("implementation", libs.findLibrary("lifecycle-viewmodel-ktx").get())
            add("implementation", libs.findLibrary("lifecycle-livedata-ktx").get())
            add("implementation", libs.findLibrary("fragment-ktx").get())
            add("implementation", libs.findLibrary("gson").get())
            add("implementation", libs.findLibrary("navigation-fragment-ktx").get())
            add("implementation", libs.findLibrary("navigation-ui-ktx").get())
        }
    }
}
