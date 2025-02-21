plugins {
    alias(libs.plugins.ecommerce.library)
    alias(libs.plugins.ecommerce.feature)
    alias(libs.plugins.ecommerce.lib.compose)
}

android {
    namespace = "com.ecommerce.features.home"
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}