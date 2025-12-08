package manager

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import java.util.Locale

internal fun Project.configAndroidDefault() {
    apply(plugin = "kotlin-android")
    apply(plugin = "com.google.devtools.ksp")
    if (this.project.name != "core-local-storage") {
        apply(plugin = "kotlin-kapt")
    }
    apply(plugin = "kotlin-parcelize")

    configure<BaseExtension> {
        buildFeatures.buildConfig = true
        compileSdkVersion(Config.compileSdk)
        buildToolsVersion(Config.buildTools)
        testOptions.unitTests.isIncludeAndroidResources = true

        defaultConfig {
            minSdk = Config.minSdkVersion
            targetSdk = Config.targetSdkVersion
            versionCode = Config.versionCode
            versionName = Config.versionName
            vectorDrawables.useSupportLibrary = true
            multiDexEnabled = true
            testApplicationId = Config.applicationIdTest
            addResourceConfiguration(Config.resConfig)
        }

        flavorDimensions("type")

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }

        testOptions {
            unitTests.isIncludeAndroidResources = true
            unitTests.isReturnDefaultValues = true
        }

        packagingOptions {
            resources.excludes.add("META-INF/DEPENDENCIES")
            resources.excludes.add("META-INF/LICENSE**")
            resources.excludes.add("META-INF/licenses/**")
            resources.excludes.add("META-INF/NOTICE.txt")
            resources.excludes.add("META-INF/NOTICE.md")
            resources.excludes.add("META-INF/gradle-plugins")
            resources.excludes.add("META-INF/maven/br.com.quasar/NotaFiscalComp/pom.xml")
            resources.excludes.add("META-INF/maven/br.com.quasar/NotaFiscalComp/pom.properties")
            resources.excludes.add("META-INF/maven/br.com.quasar/QuasarUtils/pom.xml")
            resources.excludes.add("META-INF/maven/br.com.quasar/QuasarUtils/pom.properties")
            resources.excludes.add("res/values/dimens.xml")
            resources.excludes.add("res/values-w820dp/dimens.xml")
            resources.excludes.add("res/values/strings.xml")
            resources.excludes.add("res/drawable-ldpi/ic_launcher.png")
            resources.excludes.add("res/drawable-mdpi/ic_launcher.png")
            resources.excludes.add("res/drawable-hdpi/ic_launcher.png")
            resources.excludes.add("res/drawable-xhdpi/ic_launcher.png")
            resources.excludes.add("res/drawable-xxhdpi/ic_launcher.png")
            resources.excludes.add("res/drawable-xxxhdpi/ic_launcher.png")
            resources.pickFirsts.add("publicsuffixes.gz")
        }
    }
}

internal fun Project.nameSpace(): String = Config.applicationId.plus(project.name)
    .replace("githubexplorer", "")
    .replace(":", ".")
    .replace("'", "")
    .replace("-", "")