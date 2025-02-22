plugins {
    alias(libs.plugins.ecommerce.library)
    alias(libs.plugins.ecommerce.feature)
    alias(libs.plugins.ecommerce.lib.compose)
}

android {
    namespace = "com.ecommerce.onboarding"
}

dependencies {
    implementation(libs.androidx.material3)
}