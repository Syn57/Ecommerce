plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("jacoco")
    id("io.gitlab.arturbosch.detekt")
}

apply(from = "../shared_dependencies.gradle")

private val coverageExclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*"
)

android {
    namespace = "com.luthfi.ecommerce"
    compileSdk = 34

    configure<JacocoPluginExtension> {
        toolVersion = "0.8.10"
    }

    val jacocoTestReport = tasks.create("jacocoTestReport")

    androidComponents.onVariants { variant ->
        val testTaskName = "test${variant.name.capitalize()}UnitTest"

        val reportTask =
            tasks.register("jacoco${testTaskName.capitalize()}Report", JacocoReport::class) {
                dependsOn(testTaskName)

                reports {
                    html.required.set(true)
                }

                classDirectories.setFrom(
                    fileTree("$buildDir/tmp/kotlin-classes/${variant.name}") {
                        exclude(coverageExclusions)
                    }
                )

                sourceDirectories.setFrom(
                    files("$projectDir/src/main/java")
                )
                executionData.setFrom(file("$buildDir/jacoco/$testTaskName.exec"))
//                executionData.setFrom(file("$buildDir/outputs/unit_test_code_coverage/${variant.name}UnitTest/$testTaskName.exec"))
            }

        jacocoTestReport.dependsOn(reportTask)
    }

    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }


    defaultConfig {
        applicationId = "com.luthfi.ecommerce"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }


    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core"))
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.firebase:firebase-messaging:23.3.1")
    implementation("com.google.firebase:firebase-analytics:21.5.0")
    implementation("androidx.window:window:1.2.0")
    implementation("androidx.window:window-core:1.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    //splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    //fragment & activity
    implementation("androidx.activity:activity-ktx:1.8.1")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")

    //lottie
    implementation("com.airbnb.android:lottie:6.2.0")

    //shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //livedata
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    //Swipe refresh layout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //Detekt auto formatting
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")

    //Compose
    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.material3:material3")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.compiler:compiler:1.5.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    implementation("com.github.a914-gowtham:compose-ratingbar:1.3.11")

    implementation("com.google.accompanist:accompanist-pager:0.22.0-rc")

    implementation("androidx.navigation:navigation-compose:2.7.5")

    implementation("com.github.jd-alexander:LikeButton:0.2.3")

}