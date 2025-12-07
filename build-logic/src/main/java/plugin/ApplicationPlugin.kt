package plugin

import Config
import com.android.build.gradle.BaseExtension
import manager.configAndroidDefault
import manager.createFlavor
import manager.nameSpace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class ApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            configure<BaseExtension> {
                createFlavor()
                defaultConfig {
                    applicationId = Config.applicationId
                }
                namespace = target.nameSpace()

                project.configure<com.android.build.api.dsl.ApplicationExtension> {
                    lint {
                        abortOnError = false
                        warningsAsErrors = true
                        checkAllWarnings = true
                        checkReleaseBuilds = false
                        checkDependencies = true
                    }
                }
            }
            target.configAndroidDefault()
        }
    }
}