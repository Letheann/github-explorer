package plugin

import Config
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            // Plugins
            plugins.apply("org.jetbrains.kotlin.multiplatform")
            plugins.apply("com.android.library")

            // Version Catalog
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // Kotlin Multiplatform config
            extensions.configure<KotlinMultiplatformExtension> {
                // Android target
                androidTarget {
                    compilations.all {
                        kotlinOptions {
                            jvmTarget = "17"
                        }
                    }
                }

                // iOS targets
                val xcfName = "sharedKit"
                iosX64 { binaries.framework { baseName = xcfName } }
                iosArm64 { binaries.framework { baseName = xcfName } }
                iosSimulatorArm64 { binaries.framework { baseName = xcfName } }

                // Source sets
                sourceSets {
                    val commonMain = getByName("commonMain")
                    val commonTest = getByName("commonTest")
                    val androidMain = getByName("androidMain")
                    val iosMain = maybeCreate("iosMain").apply {
                        dependsOn(commonMain)
                    }

                    // iOS targets depend on iosMain
                    getByName("iosX64Main").dependsOn(iosMain)
                    getByName("iosArm64Main").dependsOn(iosMain)
                    getByName("iosSimulatorArm64Main").dependsOn(iosMain)

                    // Dependencies
                    commonMain.dependencies {
                        implementation(libs.findLibrary("ktor-client-core").orElseThrow())
                        implementation(libs.findLibrary("ktor-client-content-negotiation").orElseThrow())
                        implementation(libs.findLibrary("ktor_serialization_kotlinx_json").orElseThrow())
                        implementation(libs.findLibrary("kotlinx-serialization-json").orElseThrow())
                        implementation(libs.findLibrary("kotlinx-coroutines-core").orElseThrow())
                    }

                    androidMain.dependencies {
                        implementation(libs.findLibrary("ktor-client-okhttp").orElseThrow())
                        implementation(libs.findBundle("koin").orElseThrow())
                    }

                    iosMain.dependencies {
                        implementation("io.ktor:ktor-client-darwin:2.3.5")
                    }

                    commonTest.dependencies {
                        implementation(libs.findLibrary("kotlin-test").orElseThrow())
                        implementation(libs.findLibrary("test_coroutines").orElseThrow())
                    }
                }
            }

            // Android Library config
            extensions.configure<LibraryExtension> {
                namespace = Config.kmpModule
                compileSdk = Config.compileSdk

                defaultConfig {
                    minSdk = Config.minSdkVersion
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
        }
    }
}
