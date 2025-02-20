plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("jacoco")
    id("io.gitlab.arturbosch.detekt")
}

apply(from = "../shared_dependencies.gradle")
//apply(plugin = "io.gitlab.arturbosch.detekt")

private val coverageExclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*"
)

android {
    namespace = "com.luthfi.ecommerce.core"
    compileSdk = 33

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
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "BASE_URL", "\"http://192.168.101.126:5000\"")
        buildConfigField("String", "API_KEY", "\"6f8856ed-9189-488f-9011-0ff4b6c08edc\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("com.google.firebase:firebase-messaging-ktx:23.3.1")
    implementation("androidx.test:core-ktx:1.5.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Chucker
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    //Detekt auto formatting
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")

}