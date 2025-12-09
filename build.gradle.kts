// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.android.lint) apply false
}
allprojects{
    configurations.all {
        resolutionStrategy.force(libs.okHttp3.core)
        resolutionStrategy.force(libs.test.objenesis)
    }
}

tasks.register("clean").configure {
    delete("build")
}
