import com.android.build.gradle.LibraryExtension
import com.grappim.docuvault.buildlogic.configureFlavors
import com.grappim.docuvault.buildlogic.configureKotlinAndroid
import com.grappim.docuvault.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlinx.kover")
            }

            extensions.configure<LibraryExtension> {
                defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                configureKotlinAndroid(this)
                configureFlavors(this)
            }
        }
    }
}
