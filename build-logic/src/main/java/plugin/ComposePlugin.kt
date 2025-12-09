package plugin

import com.android.build.gradle.BaseExtension
import extensions.androidTestImplementation
import extensions.debugImplementation
import extensions.getLibrary
import extensions.getVersion
import extensions.implementation
import extensions.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class ComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        // Ativa o plugin oficial do Compose Compiler
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        // Configuração para app
        pluginManager.withPlugin("com.android.application") {
            extensions.configure<com.android.build.gradle.AppExtension> {
                configureCompose(this, libs)
            }
        }

        // Configuração para library
        pluginManager.withPlugin("com.android.library") {
            extensions.configure<com.android.build.gradle.LibraryExtension> {
                configureCompose(this, libs)
            }
        }

        dependencies {
            implementation(platform(libs.getLibrary("compose.bom")))
            implementation(libs.getLibrary("compose.material"))
            implementation(libs.getLibrary("compose.material3"))
            implementation(libs.getLibrary("compose.foundation"))
            implementation(libs.getLibrary("compose.ui"))
            implementation(libs.getLibrary("compose.navigation"))
            implementation(libs.getLibrary("koin.compose"))
            implementation(libs.getLibrary("compose.ui.tooling.preview"))
            implementation(libs.getLibrary("compose.coil"))
            implementation(libs.getLibrary("compose.viewmodel"))
            implementation(libs.getLibrary("compose.lifecycle"))
            implementation(libs.getLibrary("compose.icons.extended"))

            debugImplementation(libs.getLibrary("compose.ui.tooling"))
            debugImplementation(libs.getLibrary("compose.manifest"))

            androidTestImplementation(libs.getLibrary("compose.ui.navigation"))
            testImplementation(libs.getLibrary("compose.ui.test"))
            testImplementation(libs.getLibrary("compose.uitest"))
            testImplementation(libs.getLibrary("compose.viewmodel"))
            testImplementation(libs.getLibrary("compose.runtime.livedata"))
        }
    }
}

private fun configureCompose(android: BaseExtension, libs: VersionCatalog) {
    android.buildFeatures.compose = true

    android.composeOptions {
        kotlinCompilerExtensionVersion =
            libs.findVersion("kotlin.compiler.extension.version").get().toString()
    }
}
