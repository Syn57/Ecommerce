import com.ecommerce.buildlogic.utils.Modules

plugins {
    alias(libs.plugins.ecommerce.library)
}

android {
    namespace = "com.ecommerce.domain"
}

dependencies {
    implementation(project(Modules.Core.MODEL))
}
