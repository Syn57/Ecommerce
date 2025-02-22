plugins {
    alias(libs.plugins.ecommerce.library)
}

android {
    namespace = "com.ecommerce.uiassets"
}

dependencies {
    implementation(libs.androidx.appcompat)
}
