plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

@Suppress("DSL_SCOPE_VIOLATION")
dependencies {
    implementation(libs.gradle.plugin)
    implementation(libs.kotlin.plugin)
}

gradlePlugin {
    plugins {
        register("applicationPlugin") {
            id = "applicationPlugin"
            implementationClass = "plugin.ApplicationPlugin"
        }
        register("composePlugin") {
            id = "composePlugin"
            implementationClass = "plugin.ComposePlugin"
        }
        register("kmpPlugin") {
            id = "kmpPlugin"
            implementationClass = "plugin.KmpPlugin"
        }
    }
}