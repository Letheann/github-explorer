plugins {
    id("applicationPlugin")
    id("composePlugin")
}

android {
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("include" to "*.jar", "dir" to "libs")))

    implementation(libs.bundles.kotlin)
    implementation(libs.lifecycle.runtime.ktx)

    //Android Support
    implementation(libs.bundles.androidSupport)
    implementation(libs.bundles.androidSupportDesign)

    // ViewModel
    implementation(libs.viewModel.lifecycleExtensions)
    implementation(libs.viewModel.core)

    //koin
    implementation(libs.bundles.koin)

    // android tests
    androidTestImplementation(libs.bundles.androidTest)
    testImplementation(libs.bundles.unitTest)

    //KMP shared
    implementation(project(":shared"))
}

