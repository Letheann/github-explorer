import java.io.File
import java.io.FileInputStream
import java.util.Properties

object Config {
    const val applicationId = "com.desafio.githubexplorer"
    const val applicationIdTest = "com.desafio.githubexplorer"
    const val kmpModule = "com.desafio.kmp"
    const val resConfig = "pt"

    val versionName: String = getCurrentVersionName()
    val versionCode: Int = getCurrentVersionCode()

    const val compileSdk = 34
    const val minSdkVersion = 21
    const val targetSdkVersion = 34

    private fun getCurrentVersionName(): String {
        return try {
            getVersionProperties()
                ?.getProperty("versionName")
                ?.replace("\"".toRegex(), "")
                ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    private fun getCurrentVersionCode(): Int {
        return try {
            getVersionProperties()
                ?.getProperty("versionCode")
                ?.replace("\"", "")
                ?.toInt()
                ?: 1
        } catch (e: Exception) {
            1
        }
    }

    private fun getVersionProperties(): Properties? {
        return try {
            val versionProperties = Properties()
            val possiblePaths = listOf(
                File("local.properties"),
                File("../local.properties"),
                File("../../local.properties")
            )

            val file = possiblePaths.firstOrNull { it.exists() }

            if (file != null) {
                versionProperties.load(FileInputStream(file))
                versionProperties
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
