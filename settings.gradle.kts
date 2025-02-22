pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Ecommerce"
include(":app")

include(":core")
include(":core:uiassets")
include(":core:model")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:database")

include(":features")
include(":features:home")
include(":core:datastore")
