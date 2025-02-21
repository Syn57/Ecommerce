plugins {
    `kotlin-dsl`
}

group = "com.ecommerce.aview.buildlogic"

tasks {
    validatePlugins {
        enableStricterValidation.set(true)
        failOnWarning.set(true)
    }
}

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        create("ecommerceApplicationPlugin") {
            id = libs.plugins.ecommerce.application.get().pluginId
            implementationClass = "com.ecommerce.buildlogic.plugins.EcommerceApplicationConventionPlugin"
        }
        create("ecommerceComposeApplicationPlugin") {
            id = libs.plugins.ecommerce.app.compose.get().pluginId
            implementationClass = "com.ecommerce.buildlogic.plugins.EcommerceComposeApplicationConventionPlugin"
        }
        create("ecommerceComposeLibraryPlugin") {
            id = libs.plugins.ecommerce.lib.compose.get().pluginId
            implementationClass = "com.ecommerce.buildlogic.plugins.EcommerceComposeLibraryConventionPlugin"
        }
        create("ecommerceLibraryPlugin") {
            id = libs.plugins.ecommerce.library.get().pluginId
            implementationClass = "com.ecommerce.buildlogic.plugins.EcommerceLibraryConventionPlugin"
        }
        create("ecommerceFeaturePlugin") {
            id = libs.plugins.ecommerce.feature.get().pluginId
            implementationClass = "com.ecommerce.buildlogic.plugins.EcommerceFeatureConventionPlugin"
        }
        create("ecommerceRoomPlugin") {
            id = libs.plugins.ecommerce.room.get().pluginId
            implementationClass = "com.ecommerce.buildlogic.plugins.EcommerceRoomConventionPlugin"
        }
    }
}
