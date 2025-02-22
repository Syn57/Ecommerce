import com.ecommerce.buildlogic.utils.Modules

plugins {
    alias(libs.plugins.ecommerce.library)
    alias(libs.plugins.ecommerce.lib.compose)
}

android {
    namespace = "com.ecommerce.uicomponent"

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(project(Modules.Core.DATA))

//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
    implementation(libs.fragment.ktx)
}