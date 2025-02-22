package com.ecommerce.buildlogic.plugins

import com.android.build.api.dsl.LibraryExtension
import com.ecommerce.buildlogic.ext.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class EcommerceComposeLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            apply(plugin = "com.android.library")
            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}