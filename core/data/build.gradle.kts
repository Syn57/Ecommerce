import com.ecommerce.buildlogic.utils.Modules

plugins {
    alias(libs.plugins.ecommerce.library)
}

android {
    namespace = "com.ecommerce.data"
}

dependencies {
    implementation(project(Modules.Core.DOMAIN))
    implementation(project(Modules.Core.DATABASE))
    implementation(project(Modules.Core.DATASTORE))
    implementation(project(Modules.Core.NETWORK))
    implementation(project(Modules.Core.MODEL))
}
