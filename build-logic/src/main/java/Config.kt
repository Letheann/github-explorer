import java.io.File
import java.io.FileInputStream
import java.util.Properties

object Config {
    const val applicationId = "com.desafio.githubexplorer"
    const val applicationIdTest = "com.desafio.githubexplorer"
    const val kmpModule = "com.desafio.kmp"
    const val resConfig = "pt"
    val versionName = getCurrentVersionName()
    val versionCode = getCurrentVersionCode()

    const val compileSdk = 34
    const val minSdkVersion = 21
    const val targetSdkVersion = 34


    private fun getCurrentVersionName(): String =
        getVersionProperties()
            .getProperty("versionName")
            .replace("\"".toRegex(), "")

    private fun getCurrentVersionCode(): Int =
        getVersionProperties()
            .getProperty("versionCode")
            .replace("\"", "")
            .toInt()

    private fun getVersionProperties(): Properties {
        val versionProperties = Properties()
        val file = File("local.properties")
        versionProperties.load(FileInputStream(file))
        return versionProperties
    }
}
